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
package jhelp.gui.action;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;
import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpImage;
import jhelp.util.resources.ResourceText;
import jhelp.util.thread.ThreadManager;

/**
 * Generic action that can be used with JButton, JRadioButton, JMenuItem, ... <br>
 * In UI, its often that you have a button, a menu and a short cut that do exactly the same action, for this its a good idea to
 * create the action once and used it every where at need. For example if text associate to action change or if the action have
 * to be disabled, you have to do it once with the generic action and changes are automatically shared to each component that
 * used it.<br>
 * More over here you can also, instead of giving directly the text, specify a resource text key and in doing so if the language
 * change, the action is automatically update, that will updated all linked components
 *
 * @author JHelp
 */
public abstract class GenericAction
        extends AbstractAction
{
    /**
     * Resource text key for action label
     */
    private       String       keyName;
    /**
     * Resource text key for action tooltip
     */
    private       String       keyTooltip;
    /**
     * Resource text for resolve action key name and tool tip key
     */
    private final ResourceText resourceText;

    /**
     * Create a new instance of GenericAction
     *
     * @param keyName Action text
     */
    public GenericAction(final @NotNull String keyName)
    {
        this(keyName, null, null, null, null);
    }

    /**
     * Create a new instance of GenericAction
     *
     * @param keyName         Key for action text
     * @param smallJHelpImage Action icon small version
     * @param largeJHelpImage Action icon large version
     * @param keyTooltip      Key for action tool tip text
     * @param resourceText    Resource text where found text for the keys
     */
    public GenericAction(
            final @NotNull String keyName, @Nullable JHelpImage smallJHelpImage, @Nullable JHelpImage largeJHelpImage,
            final @Nullable String keyTooltip, final @Nullable ResourceText resourceText)
    {
        if (keyName == null)
        {
            throw new NullPointerException("name mustn't be null");
        }

        this.resourceText = resourceText;

        if (this.resourceText != null)
        {
            this.resourceText.register(this::doResourceTextLanguageChanged);
        }

        this.keyName = keyName;
        this.keyTooltip = keyTooltip;
        this.putValue(Action.NAME, keyName);

        if (smallJHelpImage == null)
        {
            smallJHelpImage = largeJHelpImage;
        }

        if (largeJHelpImage == null)
        {
            largeJHelpImage = smallJHelpImage;
        }

        if (smallJHelpImage != null)
        {
            this.putValue(Action.SMALL_ICON, smallJHelpImage);
        }

        if (largeJHelpImage != null)
        {
            this.putValue(Action.LARGE_ICON_KEY, largeJHelpImage);
        }

        if (keyTooltip != null)
        {
            this.putValue(Action.SHORT_DESCRIPTION, keyTooltip);
        }
    }

    /**
     * Create a new instance of GenericAction
     *
     * @param keyName Action text
     * @param icon    Action icon
     */
    public GenericAction(final @NotNull String keyName, final @Nullable JHelpImage icon)
    {
        this(keyName, icon, icon, null, null);
    }

    /**
     * Create a new instance of GenericAction
     *
     * @param keyName         Action text
     * @param smallJHelpImage Action icon small version
     * @param largeJHelpImage Action icon large version
     */
    public GenericAction(
            final @NotNull String keyName, final @Nullable JHelpImage smallJHelpImage,
            final @Nullable JHelpImage largeJHelpImage)
    {
        this(keyName, smallJHelpImage, largeJHelpImage, null, null);
    }

    /**
     * Create a new instance of GenericAction
     *
     * @param keyName         Key for action text
     * @param smallJHelpImage Action icon small version
     * @param largeJHelpImage Action icon large version
     * @param resourceText    Resource text where found text for the key
     */
    public GenericAction(
            final @NotNull String keyName, final @Nullable JHelpImage smallJHelpImage,
            final @Nullable JHelpImage largeJHelpImage, final @Nullable ResourceText resourceText)
    {
        this(keyName, smallJHelpImage, largeJHelpImage, null, resourceText);
    }

    /**
     * Create a new instance of GenericAction
     *
     * @param keyName         Action text
     * @param smallJHelpImage Action icon small version
     * @param largeJHelpImage Action icon large version
     * @param keyTooltip      Action tool tip text
     */
    public GenericAction(
            final @NotNull String keyName, final @Nullable JHelpImage smallJHelpImage,
            final @Nullable JHelpImage largeJHelpImage,
            final @Nullable String keyTooltip)
    {
        this(keyName, smallJHelpImage, largeJHelpImage, keyTooltip, null);
    }

    /**
     * Create a new instance of GenericAction
     *
     * @param keyName      Key for action text
     * @param icon         Action icon
     * @param resourceText Resource text where found text for the keys
     */
    public GenericAction(
            final @NotNull String keyName, final @Nullable JHelpImage icon, final @Nullable ResourceText resourceText)
    {
        this(keyName, icon, icon, null, resourceText);
    }

    /**
     * Create a new instance of GenericAction
     *
     * @param keyName    Action text
     * @param icon       Action icon
     * @param keyTooltip Action tool tip text
     */
    public GenericAction(
            final @NotNull String keyName, final @Nullable JHelpImage icon, final @Nullable String keyTooltip)
    {
        this(keyName, icon, icon, keyTooltip, null);
    }

    /**
     * Create a new instance of GenericAction
     *
     * @param keyName      Key for action text
     * @param icon         Action icon
     * @param keyTooltip   Key for action tool tip text
     * @param resourceText Resource text where found text for the keys
     */
    public GenericAction(
            final @NotNull String keyName, final @Nullable JHelpImage icon, final @Nullable String keyTooltip,
            final @Nullable ResourceText resourceText)
    {
        this(keyName, icon, icon, keyTooltip, resourceText);
    }

    /**
     * Create a new instance of GenericAction
     *
     * @param keyName      Key for action text
     * @param resourceText Resource text where found text for the keys
     */
    public GenericAction(final @NotNull String keyName, final @Nullable ResourceText resourceText)
    {
        this(keyName, null, null, null, resourceText);
    }

    /**
     * Create a new instance of GenericAction
     *
     * @param keyName    Action text
     * @param keyTooltip Action tool tip text
     */
    public GenericAction(final @NotNull String keyName, final @Nullable String keyTooltip)
    {
        this(keyName, null, null, keyTooltip, null);
    }

    /**
     * Create a new instance of GenericAction
     *
     * @param keyName      Key for action text
     * @param keyTooltip   Key for action tool tip text
     * @param resourceText Resource text where found text for the keys
     */
    public GenericAction(
            final @NotNull String keyName, final @Nullable String keyTooltip, final @Nullable ResourceText resourceText)
    {
        this(keyName, null, null, keyTooltip, resourceText);
    }

    /**
     * Called if resource text associated change of language
     *
     * @param resourceText Resource text that changed
     */
    private void doResourceTextLanguageChanged(final @NotNull ResourceText resourceText)
    {
        this.putValue(Action.NAME, resourceText.getText(this.keyName));

        if (this.keyTooltip != null)
        {
            this.putValue(Action.SHORT_DESCRIPTION, resourceText.getText(this.keyTooltip));
        }
    }

    /**
     * Called when action have to do its action
     *
     * @param actionEvent Action event description
     */
    protected abstract void doActionPerformed(@NotNull ActionEvent actionEvent);

    /**
     * Called when action have to do its action <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param actionEvent Action event description
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    @Override
    public final void actionPerformed(final @NotNull ActionEvent actionEvent)
    {
        ThreadManager.parallel(this::doActionPerformed, actionEvent);
    }

    /**
     * Change large and small icons in same time
     *
     * @param icon New large and small icons
     */
    public final void icons(final @Nullable JHelpImage icon)
    {
        this.putValue(Action.SMALL_ICON, icon);
        this.putValue(Action.LARGE_ICON_KEY, icon);
    }

    /**
     * Large icon
     *
     * @return Large icon
     */
    public final @Nullable JHelpImage largeIcon()
    {
        return (JHelpImage) this.getValue(Action.LARGE_ICON_KEY);
    }

    /**
     * Change large icon
     *
     * @param icon New large icon
     */
    public final void largeIcon(final @Nullable JHelpImage icon)
    {
        this.putValue(Action.LARGE_ICON_KEY, icon);
    }

    /**
     * Action name
     *
     * @return Action name
     */
    public final @NotNull String name()
    {
        return this.keyName;
    }

    /**
     * Change action text
     *
     * @param name New text
     */
    public final void name(final @NotNull String name)
    {
        if (name == null)
        {
            throw new NullPointerException("name mustn't be null");
        }

        this.putValue(Action.NAME, name);
    }

    /**
     * Action name currently show to the user
     *
     * @return Action name currently show to the user
     */
    public final @NotNull String printName()
    {
        return (String) this.getValue(Action.NAME);
    }

    /**
     * Tool tip text may show to the user
     *
     * @return Tool tip text may show to the user
     */
    public final @Nullable String printToolTip()
    {
        return (String) this.getValue(Action.SHORT_DESCRIPTION);
    }

    /**
     * Change a value <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param key   Value key
     * @param value Value value
     * @see AbstractAction#putValue(String, Object)
     */
    @Override
    public final void putValue(final @NotNull String key, @Nullable Object value)
    {
        if (key == null)
        {
            throw new NullPointerException("key mustn't be null");
        }

        if (Action.NAME.equals(key))
        {
            if (value == null)
            {
                throw new NullPointerException("value for name mustn't be null");
            }

            final String name = value.toString();
            this.keyName = name;

            if (this.resourceText == null)
            {
                value = name;
            }
            else
            {
                value = this.resourceText.getText(name);
            }
        }
        else if (Action.SHORT_DESCRIPTION.equals(key))
        {
            if (value != null)
            {
                final String name = value.toString();
                this.keyTooltip = name;

                if (this.resourceText == null)
                {
                    value = name;
                }
                else
                {
                    value = this.resourceText.getText(name);
                }
            }
            else
            {
                this.keyTooltip = null;
            }
        }
        else if ((Action.SMALL_ICON.equals(key)) || (Action.LARGE_ICON_KEY.equals(key)))
        {
            if (value != null)
            {
                if (!(value instanceof Icon))
                {
                    throw new IllegalArgumentException(
                            "A javax.swing.Icon or a jhelp.util.gui.JHelpImage should be associate to the key " + key);
                }

                value = JHelpImage.toJHelpImage((Icon) value);
            }
        }
        else if (!Action.ACCELERATOR_KEY.equals(key) && !"enabled".equals(key))
        {
            Debug.verbose("The key ", key, " is not managed here");
            return;
        }

        super.putValue(key, value);
    }

    /**
     * Defined short cut
     *
     * @return Defined short cut OR {@code null} if none
     */
    public final @Nullable KeyStroke shortcut()
    {
        return (KeyStroke) this.getValue(Action.ACCELERATOR_KEY);
    }

    /**
     * Define/change shortcut
     *
     * @param keyStroke Short cut
     */
    public final void shortcut(final @Nullable KeyStroke keyStroke)
    {
        this.putValue(Action.ACCELERATOR_KEY, keyStroke);
    }

    /**
     * Small icon
     *
     * @return Small icon
     */
    public final @Nullable JHelpImage smallIcon()
    {
        return (JHelpImage) this.getValue(Action.SMALL_ICON);
    }

    /**
     * Change small icon
     *
     * @param icon New small icon
     */
    public final void smallIcon(final @Nullable JHelpImage icon)
    {
        this.putValue(Action.SMALL_ICON, icon);
    }

    /**
     * Tool tip key
     *
     * @return Tool tip key
     */
    public final @Nullable String toolTip()
    {
        return this.keyTooltip;
    }

    /**
     * Change tool tip text or key
     *
     * @param toolTip New tool tip text or key
     */
    public final void toolTip(final @Nullable String toolTip)
    {
        this.putValue(Action.SHORT_DESCRIPTION, toolTip);
    }
}