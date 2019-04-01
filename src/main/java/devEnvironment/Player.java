package devEnvironment;

import Global.Settings;
import gameEngine.Ability;
import gameEngine.callback.Callback;
import gameEngine.userInput.KeyBinding;
import gameEngine.userInput.MouseBinding;
import gameEngine.userInput.UserInputHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import physicsEngine.Material;
import physicsEngine.PhysicsWorld;
import physicsEngine.math.Formulas;
import physicsEngine.math.Point;
import physicsEngine.math.Polygon;
import physicsEngine.math.Vec2;

import java.util.Timer;
import java.util.TimerTask;

public class Player extends Body {

    private final static int RADIUS = 20;
    private final static float MAX_FORWARD_VELOCITY = 10 * (60.0f / Settings.FRAMERATE);
    private final static float MAX_SIDE_VELOCITY = 20 * (60.0f / Settings.FRAMERATE);
    private final static float MAX_BACK_VELOCITY = 20 * (60.0f / Settings.FRAMERATE);
    private final static float MAX_ROTATIONAL_VELOCITY = (float)(Math.PI / 45) * (60.0f / Settings.FRAMERATE);
    private final static float FORWARD_ACCELERATION = 2.0f * (60.0f / Settings.FRAMERATE);
    private final static float SIDE_ACCELERATION = 0.5f * (60.0f / Settings.FRAMERATE);
    private final static float BACK_ACCELERATION = 0.25f * (60.0f / Settings.FRAMERATE);
    private final static float DEACCELERATION = 0.4f * (60.0f / Settings.FRAMERATE);
    private final static float RACCEL = 40.0f * (60.0f / Settings.FRAMERATE);
    public final static float JUMP_STRENGTH = 40f;
    public final static float JUMP_COOLDOWN = 1f;
    private final static float SHOOT_COOLDOWN = 0.05f;
    private final static float DAMAGE_SHOW_DURATION = 2;

    protected KeyBinding up, down, left, right, jump, boost;
    protected MouseBinding mouse;
    protected Ability jumpAbility;
    protected Ability shootAbility;

    Timer damageTimer = new Timer(true);
    TimerTask damageTask = null;

    public Player(float x, float y, float width, float height, Material material, PhysicsWorld world) {
        super(x, y, width, height, material, world);
    }

    public Player(float x, float y, float radius, Material material, PhysicsWorld world) {
        super(x, y, radius, material, world);
    }

    public Player(float x, float y, Polygon polygon, PhysicsWorld world) {
        super(x, y, polygon, world);
    }

    @Override
    public void update()
    {
        super.update();

        if(!isInputSet()) return;

        if(mouse.isPressed())
        {
            shootAbility.use();
        }

        if(Settings.FROM_ABOVE)
        {
            float angle = Formulas.getAngle(new Point(collisionBox.getX(), collisionBox.getY()), new Point(mouse.getMouseX(), mouse.getMouseY()));
//            float angleDiff = collisionBox.getOrientation() - angle;
//            angleDiff = Formulas.normalizeAngle(angleDiff);
//            if(angleDiff < Math.PI)
//            {
//                angleDiff = angleDiff - (float)(Math.PI * 2);
//            }
//
//            collisionBox.applyTorque(angleDiff);
            collisionBox.setOrientation(angle);

            float xComponent = Math.abs(Formulas.getXComponent(1, angle)) * 0.5f + 0.5f;
            float yComponent = Math.abs(Formulas.getYComponent(1, angle)) * 0.5f + 0.5f;

            float yvel = collisionBox.getYVelocity();
            float xvel = collisionBox.getXVelocity();

            float xAccel = 0;
            float yAccel = 0;
            if (down.isPressed() && !up.isPressed() && yvel < MAX_FORWARD_VELOCITY * yComponent) {
                yAccel = FORWARD_ACCELERATION * yComponent;
            } else if (!down.isPressed() && up.isPressed() && yvel > -MAX_FORWARD_VELOCITY * yComponent) {
                yAccel = -FORWARD_ACCELERATION * yComponent;
            }

            if (right.isPressed() && !left.isPressed() && xvel < MAX_FORWARD_VELOCITY * xComponent) {
                xAccel = FORWARD_ACCELERATION * xComponent;
            } else if (!right.isPressed() && left.isPressed() && xvel > -MAX_FORWARD_VELOCITY * xComponent) {
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

            /*
            // TODO tweak max accel calculation checks
            if (down.isPressed() && !up.isPressed() && velocity > -MAX_BACK_VELOCITY) {
                fAccel = -FORWARD_ACCELERATION;
            } else if (!down.isPressed() && up.isPressed() && velocity < MAX_FORWARD_VELOCITY) {
                fAccel = FORWARD_ACCELERATION;
            }
            if(fAccel != 0) {
                collisionBox.applyForceInDirection(fAccel, angle);
            }

            if (right.isPressed() && !left.isPressed() && velocity < MAX_SIDE_VELOCITY) {
                tAccel = SIDE_ACCELERATION;
            } else if (!right.isPressed() && left.isPressed() && velocity > -MAX_SIDE_VELOCITY) {
                tAccel = -SIDE_ACCELERATION;
            }
            if(tAccel != 0) {
                collisionBox.applyForceInDirection(tAccel, angle + (float)(Math.PI / 2.0f));
            }
            */

            /*
            float accel = 0;
            if (down.isPressed() && !up.isPressed() && velocity < MAX_AXIS_VELOCITY) {
                accel = -ACCELERATION;
            } else if (!down.isPressed() && up.isPressed() && velocity > -MAX_AXIS_VELOCITY) {
                accel = ACCELERATION;
            }

            float rVel = collisionBox.getAngularVelocity();
            float rAccel = 0;
            if (right.isPressed() && !left.isPressed() && rVel < MAX_ROTATIONAL_VELOCITY) {
                rAccel = RACCEL;
            } else if (!right.isPressed() && left.isPressed() && rVel > -MAX_ROTATIONAL_VELOCITY) {
                rAccel = -RACCEL;
            }
            else if(Math.abs(rVel) > 0)
            {
                rAccel = (rVel / Math.abs(rVel)) * -RACCEL;
            }
            */
//            collisionBox.applyTorque(rAccel);
//            collisionBox.applyForceInDirection(accel, collisionBox.getOrientation());

        }
        else
        {
            float yvel = collisionBox.getYVelocity();
            float xvel = collisionBox.getXVelocity();

            float xaccel = 0;
            float yaccel = 0;

            if (down.isPressed() && !up.isPressed() && yvel < MAX_FORWARD_VELOCITY) {
                yaccel = FORWARD_ACCELERATION;
            } else if (!down.isPressed() && up.isPressed() && yvel > -MAX_FORWARD_VELOCITY) {
                yaccel = -FORWARD_ACCELERATION;
            }

            if (right.isPressed() && !left.isPressed() && xvel < MAX_FORWARD_VELOCITY) {
                xaccel = FORWARD_ACCELERATION;
            } else if (!right.isPressed() && left.isPressed() && xvel > -MAX_FORWARD_VELOCITY) {
                xaccel = -FORWARD_ACCELERATION;
            }

            if(jump.isPressed())
            {
                jumpAbility.use();
            }

            if(boost.isPressed())
            {
                xaccel *= 20.0f;
            }

            collisionBox.applyForce(xaccel, yaccel);
        }


    }

    public void generateKeyBindings(UserInputHandler input, Environment environment, short playerNum)
    {
        switch (playerNum)
        {
            default:
            case 1:
                up = input.createKeyBinding(KeyCode.W);
                down = input.createKeyBinding(KeyCode.S);
                left = input.createKeyBinding(KeyCode.A);
                right = input.createKeyBinding(KeyCode.D);
                jump = input.createKeyBinding(KeyCode.SPACE);
                mouse = input.createMouseListener(MouseButton.PRIMARY);
                boost = input.createKeyBinding(KeyCode.SHIFT);
                break;
            case 2:
                up = input.createKeyBinding(KeyCode.UP);
                down = input.createKeyBinding(KeyCode.DOWN);
                left = input.createKeyBinding(KeyCode.LEFT);
                right = input.createKeyBinding(KeyCode.RIGHT);
                jump = input.createKeyBinding(KeyCode.ENTER);
                mouse = input.createMouseListener(MouseButton.SECONDARY);
                boost = input.createKeyBinding(KeyCode.SLASH);
                break;
        }

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

    public void initializeAbilities()
    {}

    private boolean isInputSet()
    {
        return up != null &&
                down != null &&
                left != null &&
                right != null &&
                jump != null &&
                boost != null;
    }

    public void takeHit()
    {
        setColor(Color.RED);
        if(damageTask != null)
        {
            damageTask.cancel();
            damageTimer.purge();
        }

        damageTask = new TimerTask() {
            @Override
            public void run() {
                setColor(Color.GREEN);
            }
        };
        damageTimer.schedule(damageTask, (int)(DAMAGE_SHOW_DURATION * 1000));
    }
}
