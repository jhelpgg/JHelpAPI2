# Keyboard and joystick management.

Each keyboard or joystick inputs are transform to a generic action.
The aim is to not care about keyboard/joystick mapping when we receive an
event, just concentrate on action itself.
So it is easy to change the keyboard or joystick mapping without change
the code associate to an event.

Possible actions are specified in: [jhelp.engine2.render.event.ActionCode](../../src/jhelp/engine2/render/event/ActionCode.java)

To register to action event, we need an [jhelp.engine2.render.ActionManager](../../src/jhelp/engine2/render/ActionManager.java)
To have the window 3D associated instance we do:

````java
        //Get action manager instance
        ActionManager actionManager = window3D.actionManager();
````

Then we register a listener to action events:

````java
        //Register an action events listener
        actionManager.registerActionListener(KeyboardJoystick::actionEvent);
````

And we implements the listener code:

````java
    /**
     * React to current active actions
     * @param actionCodes Current actives actions
     */
    private static void actionEvent(@NotNull ActionCode... actionCodes)
    {
        for (ActionCode actionCode : actionCodes)
        {
            switch (actionCode)
            {
                case ACTION_UP:
                    KeyboardJoystick.mainNode.translate(0, 0.1f, 0);
                    break;
                case ACTION_DOWN:
                    KeyboardJoystick.mainNode.translate(0, -0.1f, 0);
                    break;
                case ACTION_LEFT:
                    KeyboardJoystick.mainNode.translate(-0.1f, 0f, 0);
                    break;
                case ACTION_RIGHT:
                    KeyboardJoystick.mainNode.translate(0.1f, 0f, 0);
                    break;
                case ACTION_EXIT:
                    KeyboardJoystick.window3D.close();
                    break;
            }
        }
    }
````

The ActionCode array received are the current array of actions actives.
Active means, the user is currently press the keyboard key or joystick
button (or axis) associated to the ActionCode.

Complete code at: [Keyboard and joystick](../../samples/jhelp/engine2/tutorials/KeyboardJoystick.java)

Press arrow keys or joystick axis to move the cube.
Press escape key to close the window.

As you notice, task are repeated until user release the key.
But sometime we want just do action only once until action is press again.
For example if we want capture next keyboard key or next joystick event and
this is started by an action, we will capture always the trigger key.
By example, if we do:

````java
                case ACTION_BUTTON_1:
                    KeyboardJoystick.window3D.actionManager()
                                             .captureKeyCode()
                                             .andConsume(keyCode -> Debug.information("keyCode=", keyCode));
````

And press 'K' we will capture the next key several times
(depends on the number of time 'K' was pressed.)
But we want only one capture, to do this, we will make the **ACTION_BUTTON_1**
consumable. Make an action consumable, avoid repetition while pressed.

````java
        //Make the ACTION_BUTTON_1 consumable to avoid repetition
        actionManager.consumable(ActionCode.ACTION_BUTTON_1, true);
````

Now even if we press 'K' for a long time, we capture next keyboard key only once.
Remember, each time you need an ActionCode is not repeated while pressed
but trigger only one time on press, make this action consumable.

> Note: In ActionManager, you will find methods for change keyboard/joystick
  association. Each change are automatically saved in Window3D preferences.
  You'll find also methods for capture next keyboard code, next joystick event.
