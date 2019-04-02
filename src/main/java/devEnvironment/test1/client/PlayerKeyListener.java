package devEnvironment.test1.client;

import devEnvironment.test1.ClientMessage;
import gameEngine.userInput.KeyBinding;
import gameEngine.userInput.MouseBinding;
import gameEngine.userInput.UserInputHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class PlayerKeyListener
{
    KeyBinding up;
    KeyBinding down;
    KeyBinding left;
    KeyBinding right;
    KeyBinding jump;
    MouseBinding mouse;
    KeyBinding boost;

    public PlayerKeyListener(UserInputHandler input)
    {
        up = input.createKeyBinding(KeyCode.W);
        down = input.createKeyBinding(KeyCode.S);
        left = input.createKeyBinding(KeyCode.A);
        right = input.createKeyBinding(KeyCode.D);
        jump = input.createKeyBinding(KeyCode.SPACE);
        mouse = input.createMouseListener(MouseButton.PRIMARY);
        boost = input.createKeyBinding(KeyCode.SHIFT);
    }

    public ClientMessage getAsMessage()
    {
        return new ClientMessage(up.isPressed(), down.isPressed(), left.isPressed(), right.isPressed(), jump.isPressed(),
                                 mouse.isClicked(), boost.isPressed(), (short)mouse.getMouseX(), (short)mouse.getMouseY());
    }
}
