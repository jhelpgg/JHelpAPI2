/*
 * Copyright:
 * License :
 *  The following code is deliver as is.
 *  I take care that code compile and work, but I am not responsible about any  damage it may  cause.
 *  You can use, modify, the code as your need for any usage.
 *  But you can't do any action that avoid me or other person use,  modify this code.
 *  The code is free for usage and modification, you can't change that fact.
 *  @author JHelp
 *
 */

package jhelp.engine2.render;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import jhelp.engine2.render.event.ActionCode;
import jhelp.engine2.render.event.ActionListener;
import jhelp.engine2.render.event.JoystickCode;
import jhelp.engine2.render.event.JoystickStatus;
import jhelp.util.debug.Debug;
import jhelp.util.list.ArrayObject;
import jhelp.util.preference.Preferences;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.Future;
import jhelp.util.thread.FutureStatus;
import jhelp.util.thread.Mutex;
import jhelp.util.thread.Promise;
import org.lwjgl.glfw.GLFW;

/**
 * Manage user interaction.<br>
 * It transforms key/joystick inputs into generic action.<br>
 * It manage generic action states
 */
public final class ActionManager
{
    /**
     * Describe an action
     */
    static class ActionDescription
    {
        /**
         * Action code
         */
        final ActionCode actionCode;
        /**
         * Indicates if action is consumable
         */
        boolean      consumable;
        /**
         * Joystick code
         */
        JoystickCode joystickCode;
        /**
         * Key code
         */
        int          keyCode;

        /**
         * Create an action description
         *
         * @param actionCode Action code
         */
        ActionDescription(@NotNull ActionCode actionCode)
        {
            this.actionCode = actionCode;
            this.consumable = false;
            this.keyCode = this.actionCode.defaultKeyCode();
            this.joystickCode = this.actionCode.defaultJoystickCode();
        }
    }

    /**
     * Suffix used for joystick preference suffix
     */
    private static final String PREFERENCE_JOYSTICK_SUFFIX = "_joystick";
    /**
     * Suffix used for key preference suffix
     */
    private static final String PREFERENCE_KEY_SUFFIX      = "_key";
    /**
     * List of possible action and their current association
     */
    private final ArrayObject<ActionDescription>    actionDescriptions;
    /**
     * Listeners of actions
     */
    private final ArrayObject<ActionListener>       actionListeners;
    /**
     * Current active key event
     */
    private final Set<Integer>                      activeKeys;
    /**
     * Indicates if capture joystick can be done
     */
    private final AtomicBoolean                     canCaptureJoystick;
    /**
     * Current active actions
     */
    private final ArrayObject<ActionCode>           currentActiveActions;
    /**
     * Current active joystick codes
     */
    private final Map<JoystickCode, JoystickStatus> currentJoystickCodes;
    /**
     * Current active joystick codes copy
     */
    private final Map<JoystickCode, JoystickStatus> currentJoystickCodesCopy;
    /**
     * Mutex for caputure key or joystick code
     */
    private final Mutex                             mutexCapture;
    /**
     * Promise for capture next joystick code
     */
    private       Promise<JoystickCode>             nextJoystickCode;
    /**
     * Promise for capture next key code
     */
    private       Promise<Integer>                  nextKeyCode;
    /**
     * Preferences where key/joystick map are stored
     */
    private final Preferences                       preferences;

    /**
     * Create action manager
     *
     * @param preferences Preferences where extract/store key/joystick mapping
     */
    ActionManager(@NotNull Preferences preferences)
    {
        this.preferences = preferences;
        this.actionDescriptions = new ArrayObject<>();
        this.actionListeners = new ArrayObject<>();
        this.currentActiveActions = new ArrayObject<>();
        this.activeKeys = new HashSet<>();
        this.currentJoystickCodes = new HashMap<>();
        this.currentJoystickCodesCopy = new HashMap<>();
        this.mutexCapture = new Mutex();
        this.canCaptureJoystick = new AtomicBoolean(false);

        for (JoystickCode joystickCode : JoystickCode.values())
        {
            this.currentJoystickCodes.put(joystickCode, JoystickStatus.RELEASED);
        }

        ActionDescription actionDescription;

        for (ActionCode actionCode : ActionCode.values())
        {
            actionDescription = new ActionDescription(actionCode);
            actionDescription.keyCode = this.preferences.getValue(actionCode.preferenceKey() +
                                                                  ActionManager.PREFERENCE_KEY_SUFFIX,
                                                                  actionCode.defaultKeyCode());
            actionDescription.joystickCode = this.preferences.getValue(actionCode.preferenceKey() +
                                                                       ActionManager.PREFERENCE_JOYSTICK_SUFFIX,
                                                                       actionCode.defaultJoystickCode());
            this.actionDescriptions.add(actionDescription);
        }
    }

    private @Nullable ActionDescription associated(JoystickCode joystickCode)
    {
        return this.actionDescriptions.any(actionDescription -> joystickCode == actionDescription.joystickCode)
                                      .thenDo(future ->
                                              {
                                                  if (future.status() == FutureStatus.RESULT)
                                                  {
                                                      return future.value();
                                                  }

                                                  return null;
                                              })
                                      .value();
    }

    @ThreadOpenGL
    private void fireActionEvent(ActionCode... actionCodes)
    {
        synchronized (this.actionListeners)
        {
            this.actionListeners.forEach(
                    (ConsumerTask<ActionListener>) actionListener -> actionListener.actionsActive(actionCodes));
        }
    }

    /**
     * Press a joystick input
     *
     * @param joystickCode Joystick code
     * @return Indicates if event is consumed
     */
    private boolean pressJoystick(JoystickCode joystickCode)
    {
        final boolean consumed = this.mutexCapture.playInCriticalSection(
                () ->
                {
                    if (this.nextJoystickCode != null)
                    {
                        if (this.canCaptureJoystick.get())
                        {
                            this.nextJoystickCode.setResult(joystickCode);
                            this.nextJoystickCode = null;

                            for (Map.Entry<JoystickCode, JoystickStatus> entry : this.currentJoystickCodesCopy.entrySet())
                            {
                                this.currentJoystickCodes.put(entry.getKey(), entry.getValue());
                            }

                            this.canCaptureJoystick.set(false);
                        }
                        else
                        {
                            this.canCaptureJoystick.set(this.currentJoystickCodes.values()
                                                                                 .stream()
                                                                                 .allMatch(joystickStatus ->
                                                                                                   joystickStatus ==
                                                                                                   JoystickStatus.RELEASED));
                        }

                        return true;
                    }

                    return false;
                });

        if (consumed)
        {
            return true;
        }

        this.currentJoystickCodes.put(joystickCode, this.currentJoystickCodes.get(joystickCode).press());
        return false;
    }

    /**
     * Called when key event happen
     *
     * @param keyCode Key code
     * @param action  Key action: {@link GLFW#GLFW_PRESS}, {@link GLFW#GLFW_REPEAT} or {@link GLFW#GLFW_RELEASE}
     */
    @ThreadOpenGL
    void keyEvent(int keyCode, int action)
    {
        if (action == GLFW.GLFW_PRESS)
        {
            final boolean consumed = this.mutexCapture.playInCriticalSection(
                    () ->
                    {
                        if (this.nextKeyCode != null)
                        {
                            this.nextKeyCode.setResult(keyCode);
                            this.nextKeyCode = null;
                            this.activeKeys.remove(keyCode);
                            return true;
                        }

                        return false;
                    });

            if (consumed)
            {
                return;
            }

            this.activeKeys.add(keyCode);
        }
        else if (action == GLFW.GLFW_RELEASE)
        {
            this.activeKeys.remove(keyCode);
        }
    }

    /**
     * Post action to listeners
     */
    @ThreadOpenGL
    void publishActions()
    {
        //Collect joystick status
        if (GLFW.glfwJoystickPresent(GLFW.GLFW_JOYSTICK_1))
        {
            final FloatBuffer axes = GLFW.glfwGetJoystickAxes(GLFW.GLFW_JOYSTICK_1);

            if (axes != null)
            {
                final int     position  = axes.position();
                final float[] axesValue = new float[axes.limit() - position];
                axes.get(axesValue);
                axes.position(position);
                final int max = Math.min(axesValue.length - 1, JoystickCode.MAX_AXIS_INDEX);

                for (int index = 0; index <= max; index++)
                {
                    if (axesValue[index] < -0.25f)
                    {
                        this.currentJoystickCodes.put(JoystickCode.obtainAxis(index, true), JoystickStatus.RELEASED);

                        if (this.pressJoystick(JoystickCode.obtainAxis(index, false)))
                        {
                            return;
                        }
                    }
                    else if (axesValue[index] > 0.25f)
                    {
                        this.currentJoystickCodes.put(JoystickCode.obtainAxis(index, false), JoystickStatus.RELEASED);

                        if (this.pressJoystick(JoystickCode.obtainAxis(index, true)))
                        {
                            return;
                        }
                    }
                    else
                    {
                        this.currentJoystickCodes.put(JoystickCode.obtainAxis(index, true), JoystickStatus.RELEASED);
                        this.currentJoystickCodes.put(JoystickCode.obtainAxis(index, false), JoystickStatus.RELEASED);
                    }
                }
            }

            final ByteBuffer buttons = GLFW.glfwGetJoystickButtons(GLFW.GLFW_JOYSTICK_1);

            if (buttons != null)
            {
                final int    position      = buttons.position();
                final byte[] buttonsStatus = new byte[buttons.limit() - position];
                buttons.get(buttonsStatus);
                buttons.position(position);
                final int max = Math.min(buttonsStatus.length - 1, JoystickCode.MAX_BUTTON_INDEX);

                for (int index = 0; index <= max; index++)
                {
                    if (buttonsStatus[index] == GLFW.GLFW_PRESS)
                    {
                        if (this.pressJoystick(JoystickCode.obtainButton(index)))
                        {
                            return;
                        }
                    }
                    else
                    {
                        this.currentJoystickCodes.put(JoystickCode.obtainButton(index), JoystickStatus.RELEASED);
                    }
                }
            }
        }

        //Update actions active list
        this.actionDescriptions.consume(actionDescription ->
                                        {
                                            final JoystickStatus joystickStatus =
                                                    this.currentJoystickCodes.get(actionDescription.joystickCode);

                                            if (this.activeKeys.contains(actionDescription.keyCode) ||
                                                joystickStatus == JoystickStatus.PRESSED ||
                                                (joystickStatus == JoystickStatus.REPEATED &&
                                                 !actionDescription.consumable))
                                            {
                                                if (!this.currentActiveActions.contains(actionDescription.actionCode))
                                                {
                                                    this.currentActiveActions.add(actionDescription.actionCode);
                                                }
                                            }
                                            else
                                            {
                                                this.currentActiveActions.remove(actionDescription.actionCode);
                                            }
                                        });

        //Publish active actions
        if (!this.currentActiveActions.isEmpty())
        {
            this.fireActionEvent(this.currentActiveActions.toArray(new ActionCode[this.currentActiveActions.size()]));
            this.actionDescriptions.consume(actionDescription ->
                                            {
                                                if (actionDescription.consumable)
                                                {
                                                    this.currentActiveActions.remove(actionDescription.actionCode);
                                                    this.activeKeys.remove(actionDescription.keyCode);
                                                }
                                            });
        }
    }

    /**
     * Associate a key code to an action.<br>
     * If an action was previously associated to this key code, it is returned
     *
     * @param actionCode Action to associate
     * @param keyCode    Key code
     * @return Previous associated action
     */
    public @Nullable ActionCode associate(@NotNull ActionCode actionCode, int keyCode)
    {
        Objects.requireNonNull(actionCode, "actionCode MUST NOT be null!");
        Future<ActionDescription> previous =
                this.actionDescriptions.any(actionDescription -> keyCode == actionDescription.keyCode)
                                       .waitFinish();

        this.actionDescriptions.any(actionDescription -> actionCode == actionDescription.actionCode)
                               .andConsume(actionDescription -> actionDescription.keyCode = keyCode)
                               .waitFinish();

        this.preferences.setValue(actionCode.preferenceKey() + ActionManager.PREFERENCE_KEY_SUFFIX, keyCode);

        if (previous.status() == FutureStatus.RESULT)
        {
            return previous.value().actionCode;
        }

        return null;
    }

    /**
     * Associate a joystick code to an action.<br>
     * If an action was previously associated to this joystick code, it is returned
     *
     * @param actionCode   Action to associate
     * @param joystickCode Joystick code
     * @return Previous associated action
     */
    public @Nullable ActionCode associate(@NotNull ActionCode actionCode, @NotNull JoystickCode joystickCode)
    {
        Objects.requireNonNull(actionCode, "actionCode MUST NOT be null!");
        Objects.requireNonNull(joystickCode, "joystickCode MUST NOT be null!");
        Future<ActionDescription> previous =
                this.actionDescriptions.any(actionDescription -> joystickCode == actionDescription.joystickCode)
                                       .waitFinish();

        this.actionDescriptions.any(actionDescription -> actionCode == actionDescription.actionCode)
                               .andConsume(actionDescription -> actionDescription.joystickCode = joystickCode)
                               .waitFinish();

        this.preferences.setValue(actionCode.preferenceKey() + ActionManager.PREFERENCE_JOYSTICK_SUFFIX, joystickCode);

        if (previous.status() == FutureStatus.RESULT)
        {
            return previous.value().actionCode;
        }

        return null;
    }

    /**
     * Capture the next joystick event
     *
     * @return Future that will contains the next joystick event
     */
    public Future<JoystickCode> captureJoystick()
    {
        return this.mutexCapture.playInCriticalSection(
                () ->
                {
                    if (this.nextJoystickCode == null)
                    {
                        this.nextJoystickCode = new Promise<>();
                        this.canCaptureJoystick.set(false);

                        for (Map.Entry<JoystickCode, JoystickStatus> entry : this.currentJoystickCodes.entrySet())
                        {
                            this.currentJoystickCodesCopy.put(entry.getKey(), entry.getValue());
                        }
                    }

                    return this.nextJoystickCode.future();
                });
    }

    /**
     * Capture the next key typed
     *
     * @return Future that will contains the next key code typed
     */
    public Future<Integer> captureKeyCode()
    {
        return this.mutexCapture.playInCriticalSection(
                () ->
                {
                    if (this.nextKeyCode == null)
                    {
                        this.nextKeyCode = new Promise<>();
                    }

                    return this.nextKeyCode.future();
                });
    }

    /**
     * Change consumable status of an action
     *
     * @param actionCode Action code
     * @param consumable New consumable value
     */
    public void consumable(@NotNull ActionCode actionCode, boolean consumable)
    {
        Objects.requireNonNull(actionCode, "actionCode MUST NOT be null!");
        this.actionDescriptions.any(actionDescription -> actionCode == actionDescription.actionCode)
                               .andConsume(actionDescription -> actionDescription.consumable = consumable);
    }

    /**
     * Indicates if an action code is currently consumable
     *
     * @param actionCode Action code
     * @return {@code true} if action code is currently consumable
     */
    public boolean consumable(@NotNull ActionCode actionCode)
    {
        Objects.requireNonNull(actionCode, "actionCode MUST NOT be null!");
        return this.actionDescriptions.any(actionDescription -> actionCode == actionDescription.actionCode)
                                      .thenDo(future ->
                                              {
                                                  if (future.status() == FutureStatus.RESULT)
                                                  {
                                                      return future.value().consumable;
                                                  }

                                                  Debug.exception(future.error());
                                                  return false;
                                              })
                                      .value();
    }

    /**
     * Joystick code associated to given action code
     *
     * @param actionCode Action to get its joystick code
     * @return Associated joystick code OR {@link JoystickCode#NONE} if no joystick code
     */
    public @NotNull JoystickCode joystickCode(@NotNull ActionCode actionCode)
    {
        return this.actionDescriptions.any(actionDescription -> actionDescription.actionCode == actionCode)
                                      .thenDo(future ->
                                              {
                                                  if (future.status() == FutureStatus.RESULT)
                                                  {
                                                      return future.value().joystickCode;
                                                  }

                                                  return JoystickCode.NONE;
                                              })
                                      .value();
    }

    /**
     * Key code associated to given action code
     *
     * @param actionCode Action to get its key code
     * @return Associated key code OR -1 if no key code
     */
    public int keyCode(@NotNull ActionCode actionCode)
    {
        return this.actionDescriptions.any(actionDescription -> actionDescription.actionCode == actionCode)
                                      .thenDo(future ->
                                              {
                                                  if (future.status() == FutureStatus.RESULT)
                                                  {
                                                      return future.value().keyCode;
                                                  }

                                                  return -1;
                                              })
                                      .value();
    }

    /**
     * Register an action listener
     *
     * @param actionListener Action listener to register
     */
    public void registerActionListener(@NotNull ActionListener actionListener)
    {
        if (actionListener == null)
        {
            return;
        }

        synchronized (this.actionListeners)
        {
            if (!this.actionListeners.contains(actionListener))
            {
                this.actionListeners.add(actionListener);
            }
        }
    }

    /**
     * Unregister an action listener
     *
     * @param actionListener Action listener to unregister
     */
    public void unregisterActionListener(@NotNull ActionListener actionListener)
    {
        synchronized (this.actionListeners)
        {
            this.actionListeners.remove(actionListener);
        }
    }
}
