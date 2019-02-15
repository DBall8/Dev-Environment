package devEnvironment;

import gameEngine.Entity;
import gameEngine.GameEngine;
import Global.Settings;
import gameEngine.callback.Callback;
import gameEngine.userInput.MouseBinding;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import physicsEngine.Material;
import physicsEngine.PhysicsWorld;
import physicsEngine.math.MalformedPolygonException;
import physicsEngine.math.Point;
import physicsEngine.math.Polygon;

import java.util.ArrayList;
import java.util.List;

public class DevEnvironment extends GameEngine {

    PhysicsWorld world;
    MouseBinding mouseBinding;
    List<Body> bodies = new ArrayList<>();
    Body player;

    Group debugGroup = new Group();

    @Override
    protected void onInitialize()
    {
        setWindowWidth(Settings.WINDOW_WIDTH);
        setWindowHeight(Settings.WINDOW_HEIGHT);
        world = new PhysicsWorld(Settings.GRAVITY);
        world.addDebugView(debugGroup);
        world.setUpdatesPerFrame(30);
        setFramesPerSecond(Settings.FRAMERATE);
    }

    @Override
    protected void onStart()
    {
        mouseBinding = userInputHandler.createMouseClickBinding(MouseButton.PRIMARY);

        Body body = new Body(50, 50, 40, 40, Material.Wood, world);
        addBody(body);
        body.generateKeyBindings(userInputHandler, this);
        player = body;

        Wall wall1 = new Wall(-30, Settings.WINDOW_HEIGHT / 2, 80, Settings.WINDOW_HEIGHT, world);
        addEntity(wall1);

        Wall wall2 = new Wall(Settings.WINDOW_WIDTH + 30, Settings.WINDOW_HEIGHT / 2, 80, Settings.WINDOW_HEIGHT, world);
        addEntity(wall2);

        Wall wall3 = new Wall(Settings.WINDOW_WIDTH / 2, -30, Settings.WINDOW_WIDTH - 20, 80, world);
        addEntity(wall3);

        Wall wall4 = new Wall(Settings.WINDOW_WIDTH / 2, Settings.WINDOW_HEIGHT + 30, Settings.WINDOW_WIDTH - 20, 80, world);
        addEntity(wall4);

        try {
            Polygon p = new Polygon(new Point[]{
                    new Point(0, 0),
                    new Point(100, 100),
                    new Point(100, 0),
            });
            Body testPoly = new Body(600, 600, p, world);
            addBody(testPoly);
        } catch (MalformedPolygonException e) {
            e.printStackTrace();
        }

        try {
            Polygon p = new Polygon(new Point[]{
                    new Point(0, 0),
                    new Point(0, 100),
                    new Point(100, 100),
                    new Point(100, 0),
            });
            Body testPoly = new Body(600, 600, p, world);
            addBody(testPoly);
        } catch (MalformedPolygonException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onUpdateStart()
    {
        float alpha = world.update(1.0f/ getFramesPerSecond());
        alphaAdjust(alpha);
    }

    synchronized private void alphaAdjust(float alpha)
    {
        for(Body b: bodies)
        {
            b.alphaAdjust(alpha);
        }
    }

    @Override
    protected void onUpdateFinish()
    {

        if(mouseBinding.isClicked()) {
            mouseBinding.consumeClick();

            /* Spawn triangle
            try {
                float size = (float)Math.random()*75 + 25;
                Polygon p = new Polygon(new Point[]{
                        new Point(0, 0),
                        new Point(size, size),
                        new Point(size, 0),
                });
                Body newBody = new Body(mouseBinding.getMouseX(), mouseBinding.getMouseY(), p, world);
                newBody.setMaterial(Material.Rock);
                float randAngle = (float) (Math.random() * 2.0f * Math.PI);
                newBody.setRotation(randAngle);
                addBody(newBody);
            } catch (MalformedPolygonException e) {
                e.printStackTrace();
            }
            */
        }
    }

    synchronized void addBody(Body body)
    {
        addEntity(body);
        bodies.add(body);
    }

    synchronized void removeBody(Body body)
    {
        removeEntity(body);
       bodies.remove(body);
       world.removeObject(body.collisionBox);
    }
}
