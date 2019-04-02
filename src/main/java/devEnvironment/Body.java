package devEnvironment;

import gameEngine.Ability;
import gameEngine.Entity;
import gameEngine.callback.Callback;
import gameEngine.userInput.KeyBinding;
import gameEngine.userInput.MouseBinding;
import gameEngine.userInput.UserInputHandler;
import Global.Settings;
import javafx.scene.input.MouseButton;
import physicsEngine.Material;
import physicsEngine.PhysicsObject;
import physicsEngine.PhysicsWorld;
import physicsEngine.math.Point;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import physicsEngine.math.Vec2;

public class Body extends Entity
{
    protected final static float ORIENT_SIZE = 2;

    protected PhysicsWorld world;
    protected PhysicsObject collisionBox;
    private Shape shape;

    // Rectangle constructor
    public Body(float x, float y, float width, float height, Material material, PhysicsWorld world)
    {
        this.world = world;
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
        this.world = world;
        collisionBox = world.addCircle(x, y, radius, material);
        shape = new Circle(0, 0, radius);

        setColor(material);

        Rectangle orient = new Rectangle(-ORIENT_SIZE/2, -radius, ORIENT_SIZE, radius);
        orient.setFill(Color.BLACK);

        addVisual(shape);
        addVisual(orient);
    }

    // Polygon constructor
    public Body(float x, float y, physicsEngine.math.Polygon polygon, PhysicsWorld world)
    {
        this.world = world;
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

    public Body(float x, float y, physicsEngine.math.Polygon polygon, Material material, PhysicsWorld world)
    {
        this.world = world;
        collisionBox = world.addPolygon(x, y, polygon, material);

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
    }

    public void alphaAdjust(float alpha)
    {
        x += collisionBox.getXVelocity() * alpha;
        y += collisionBox.getYVelocity() * alpha;
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
            shape.setFill(Color.DARKBLUE);
        }
        else if(material.equals(Material.Bouncy))
        {
            shape.setFill(Color.PINK);
        }
    }

    public void setMaterial(Material material)
    {
        collisionBox.setMaterial(material);
    }

    public void setRotation(float rotation)
    {
        collisionBox.setOrientation(rotation);
    }

    public PhysicsObject getCollisionBox(){ return collisionBox; }
}
