package devEnvironment.test1.server;

import Global.Settings;
import devEnvironment.Bullet;
import devEnvironment.Environment;
import devEnvironment.Player;
import devEnvironment.test1.ClientMessage;
import gameEngine.Ability;
import gameEngine.callback.Callback;
import gameEngine.userInput.UserInputHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import physicsEngine.Material;
import physicsEngine.PhysicsWorld;
import physicsEngine.math.Formulas;
import physicsEngine.math.Point;
import physicsEngine.math.Polygon;
import physicsEngine.math.Vec2;

import java.util.LinkedList;
import java.util.List;

public class RemoteControlledPlayer extends Player
{
    private String ip;

    List<ClientMessage> latestMessages = new LinkedList<>();

    public RemoteControlledPlayer(String ip, float x, float y, float width, float height, Material material, PhysicsWorld world) {
        super(x, y, width, height, material, world);
        this.ip = ip;
    }

    public RemoteControlledPlayer(String ip, float x, float y, float radius, Material material, PhysicsWorld world) {
        super(x, y, radius, material, world);
        this.ip = ip;
    }

    public RemoteControlledPlayer(String ip, float x, float y, Polygon polygon, PhysicsWorld world) {
        super(x, y, polygon, world);
        this.ip = ip;
    }

    public void setLatestMessages(List<ClientMessage> latestMessages)
    {
        this.latestMessages = latestMessages;

    }

    @Override
    public void update() {
        super.update();
        for(ClientMessage message: latestMessages)
        {
            playerUpdate(message);
        }
        latestMessages.clear();
    }

    public void playerUpdate(ClientMessage message)
    {
        if(message.isShoot())
        {
            shootAbility.use();
        }

        if(Settings.FROM_ABOVE)
        {
            float angle = Formulas.getAngle(new Point(collisionBox.getX(), collisionBox.getY()), new Point(message.getMouseX(), message.getMouseY()));
            collisionBox.setOrientation(angle);

            float xComponent = Math.abs(Formulas.getXComponent(1, angle)) * 0.5f + 0.5f;
            float yComponent = Math.abs(Formulas.getYComponent(1, angle)) * 0.5f + 0.5f;

            float yvel = collisionBox.getYVelocity();
            float xvel = collisionBox.getXVelocity();

            float xAccel = 0;
            float yAccel = 0;
            if (message.isDown() && !message.isUp() && yvel < MAX_FORWARD_VELOCITY * yComponent) {
                yAccel = FORWARD_ACCELERATION * yComponent;
            } else if (!message.isDown() && message.isUp() && yvel > -MAX_FORWARD_VELOCITY * yComponent) {
                yAccel = -FORWARD_ACCELERATION * yComponent;
            }

            if (message.isRight() && !message.isLeft() && xvel < MAX_FORWARD_VELOCITY * xComponent) {
                xAccel = FORWARD_ACCELERATION * xComponent;
            } else if (!message.isRight() && message.isLeft() && xvel > -MAX_FORWARD_VELOCITY * xComponent) {
                xAccel = -FORWARD_ACCELERATION * xComponent;
            }

            // Slow down
            if(xvel > 0)
            {
                xAccel -= DEACCELERATION;
            }
            else if(xvel < 0)
            {
                xAccel += DEACCELERATION;
            }

            if(yvel > 0)
            {
                yAccel -= DEACCELERATION;
            }
            else if(yvel < 0)
            {
                yAccel += DEACCELERATION;
            }
            collisionBox.applyForce(xAccel, yAccel);
        }
        else
        {
            float yvel = collisionBox.getYVelocity();
            float xvel = collisionBox.getXVelocity();

            float xaccel = 0;
            float yaccel = 0;

            if (message.isDown() && !message.isUp() && yvel < MAX_FORWARD_VELOCITY) {
                yaccel = FORWARD_ACCELERATION;
            } else if (!message.isDown() && message.isUp() && yvel > -MAX_FORWARD_VELOCITY) {
                yaccel = -FORWARD_ACCELERATION;
            }

            if (message.isRight() && !message.isLeft() && xvel < MAX_FORWARD_VELOCITY) {
                xaccel = FORWARD_ACCELERATION;
            } else if (!message.isRight() && message.isLeft() && xvel > -MAX_FORWARD_VELOCITY) {
                xaccel = -FORWARD_ACCELERATION;
            }

            if(message.isJump())
            {
                jumpAbility.use();
            }

            if(message.isBoost())
            {
                xaccel *= 20.0f;
            }

            System.out.println(xaccel);
            collisionBox.applyForce(xaccel, yaccel);
        }
    }


    @Override
    public void generateKeyBindings(UserInputHandler input, Environment environment, short playerNum)
    {
        jumpAbility = new Ability(JUMP_COOLDOWN, new Callback<Void>() {
            @Override
            public void run(Void parameter) {
                Vec2 groundedVec;
                if((groundedVec = world.getGroundedVector(collisionBox)).y < 0)
                {
                    collisionBox.applyForce(0, JUMP_STRENGTH * groundedVec.y);
                }
            }
        });
        Player me = this;
        shootAbility = new Ability(SHOOT_COOLDOWN, new Callback<Void>() {
            @Override
            public void run(Void paramter) {
                environment.addBody(new Bullet(me, environment));
            }
        });

    }
}
