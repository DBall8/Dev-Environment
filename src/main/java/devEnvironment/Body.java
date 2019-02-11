package devEnvironment;

import gameEngine.Entity;
import gameEngine.KeyBinding;
import gameEngine.UserInputHandler;
import Global.Settings;
import physicsEngine.Material;
import physicsEngine.PhysicsObject;
import physicsEngine.PhysicsWorld;
import physicsEngine.math.Point;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class Body extends Entity
{
    private final static int RADIUS = 20;
    private final static int MAX_AXIS_VELOCITY = 20;
    private final static float ACCELERATION = 1.0f * (60.0f / Settings.getFramerate());
    public final static float JUMP_STRENGTH = 20;

    protected final static float ORIENT_SIZE = 2;

    protected PhysicsObject collisionBox;
    private Shape shape;
    protected KeyBinding up, down, left, right, boost;

    // Rectangle constructor
    public Body(float x, float y, float width, float height, Material material, PhysicsWorld world)
    {
        collisionBox = world.addBox(x, y, width, height, material);
        shape = new Rectangle(-width/2, -height/2, width, height);

        setColor(material);

        Rectangle orient = new Rectangle(-ORIENT_SIZE/2, -height/2, ORIENT_SIZE, height/2);
        orient.setFill(Color.BLACK);

        addVisual(shape);
        addVisual(orient);
    }

    // Circle constructor
    public Body(float x, float y, float radius, Material material, PhysicsWorld world)
    {
        collisionBox = world.addCircle(x, y, radius, material);
        shape = new Circle(0, 0, radius);

        setColor(material);

        Rectangle orient = new Rectangle(-ORIENT_SIZE/2, -radius, ORIENT_SIZE, radius);
        orient.setFill(Color.BLACK);

        addVisual(shape);
        addVisual(orient);
    }

    // Polygon constructor
    public Body(float x, float y, physicsEngine.math.Polygon polygon, PhysicsWorld world) {

        collisionBox = world.addPolygon(x, y, polygon);

        Point[] centeredPoints = polygon.getPoints();
        double[] polyPoints = new double[centeredPoints.length*2];
        for(int i=0; i<centeredPoints.length; i++)
        {
            polyPoints[2*i] = (centeredPoints[i].getX());
            polyPoints[2*i + 1] = (centeredPoints[i].getY());
        }
        shape = new Polygon(polyPoints);
        shape.setFill(Color.ORANGE);

        Line orient = new Line(0,0,centeredPoints[0].getX(), centeredPoints[0].getY());
        orient.setStrokeWidth(ORIENT_SIZE);
        orient.setFill(Color.BLACK);

        addVisual(shape);
        addVisual(orient);
    }

    @Override
    public void update()
    {
        x = collisionBox.getX();
        y = collisionBox.getY();
        orientation = (float)(collisionBox.getOrientation() * 180 / Math.PI);

        if(!isInputSet()) return;

        float yvel = collisionBox.getYvelocity();
        float xvel = collisionBox.getXvelocity();

        float xaccel = 0;
        float yaccel = 0;

        if (down.isPressed() && !up.isPressed() && yvel < MAX_AXIS_VELOCITY) {
            yaccel = ACCELERATION;
        } else if (!down.isPressed() && up.isPressed() && yvel > -MAX_AXIS_VELOCITY) {
            yaccel = -ACCELERATION;
        }

        if (right.isPressed() && !left.isPressed() && xvel < MAX_AXIS_VELOCITY) {
            xaccel = ACCELERATION;
        } else if (!right.isPressed() && left.isPressed() && xvel > -MAX_AXIS_VELOCITY) {
            xaccel = -ACCELERATION;
        }

        if(boost.isPressed())
        {
            xaccel *= 20.0f;
        }

        collisionBox.applyForce(xaccel, yaccel);
    }

    public void generateKeyBindings(UserInputHandler input)
    {
        up = input.createKeyBinding(KeyCode.W);
        down = input.createKeyBinding(KeyCode.S);
        left = input.createKeyBinding(KeyCode.A);
        right = input.createKeyBinding(KeyCode.D);
        boost = input.createKeyBinding(KeyCode.SPACE);
    }

    private boolean isInputSet()
    {
        return up != null &&
               down != null &&
               left != null &&
               right != null &&
               boost != null;
    }

    public void setColor(Color color)
    {
        shape.setFill(color);
    }

    public void setColor(Material material)
    {
        if(material.equals(Material.Wood))
        {
            shape.setFill(Color.GREEN);
        }
        else if(material.equals(Material.Rock))
        {
            shape.setFill(Color.BROWN);
        }
        else if(material.equals(Material.Metal))
        {
            shape.setFill(Color.LIGHTBLUE);
        }
        else if(material.equals(Material.Bouncy))
        {
            shape.setFill(Color.PINK);
        }
    }
}
