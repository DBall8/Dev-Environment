package devEnvironment.local;

import devEnvironment.*;
import gameEngine.Entity;
import gameEngine.GameEngine;
import Global.Settings;
import gameEngine.userInput.KeyBinding;
import gameEngine.userInput.MouseBinding;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import physicsEngine.Material;
import physicsEngine.PhysicsObject;
import physicsEngine.PhysicsWorld;
import physicsEngine.math.MalformedPolygonException;
import physicsEngine.math.Point;
import physicsEngine.math.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameLocal extends GameEngine {

    public static final boolean DEBUG = false;
    public static final int MIN_EDGE_DIST = 100;

    PhysicsWorld world;
    MouseBinding mouseBinding;
    List<Body> bodies = new ArrayList<>();
    List<Player> players = new ArrayList<>();

    Group debugGroup = new Group();

    Environment environment;
    Map map;

    @Override
    protected void onInitialize()
    {
        setWindowWidth(Settings.WINDOW_WIDTH);
        setWindowHeight(Settings.WINDOW_HEIGHT);
//        showFramerate();
        world = new PhysicsWorld(Settings.GRAVITY);
        world.addDebugView(debugGroup);
        world.setUpdatesPerSecond(120);
        world.setCollisionPrecision(10);
        setFramesPerSecond(Settings.FRAMERATE);
        world.setAirResistanceScale(Settings.AIR_RESISTANCE_FACTOR);
        world.setFocusDistance((int)(Settings.WINDOW_WIDTH * 1.2));

        Bullet.setPlayerList(players);

        environment = new Environment(world, new physicsEngine.callback.Callback<Body>() {
            @Override
            public void run(Body paramter) {
                addBody(paramter);
            }
        }, new physicsEngine.callback.Callback<Body>() {
            @Override
            public void run(Body paramter) {
                removeBody(paramter);
            }
        });
    }

    @Override
    protected void onStart()
    {
        try {
            Polygon p = new Polygon(new Point[]{
                    new Point(-20, -20),
                    new Point(100, 0),
                    new Point(-20, 20)
            });

//            Player player1 = new Player(50, 100, p, Material.Wood, world);

        Player player1 = new Player(50, 100, 40, /*80,*/ Material.Wood, world);
            addBody(player1);
            players.add(player1);
            player1.generateKeyBindings(getUserInputHandler(), environment, (short)1);
            player1.getCollisionBox().setDebug();
        } catch (Exception e){}



        Player player2 = new Player(500, 500, 40, 40, Material.Wood, world);
        player2.setRotation((int)Math.PI/2);
        addBody(player2);
        players.add(player2);
        player2.generateKeyBindings(getUserInputHandler(), environment, (short)2);

        map = new Map(environment);
        map.initialize();

        try {
            Polygon p = new Polygon(new Point[]{
                    new Point(0, 0),
                    new Point(100, 100),
                    new Point(100, 0),
            });
            Body testPoly = new Body(600, 600, p, Material.Bouncy, world);
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

        if(DEBUG) {
            mouseBinding = getUserInputHandler().createMouseListener(MouseButton.PRIMARY);
            addEntity(new MouseDebug());
        }

    }

    @Override
    protected void onUpdateStart()
    {
        int cameraX = getCamera().getCameraX();
        int cameraY = getCamera().getCameraY();
        Player p1 = players.get(0);
        if (Settings.WINDOW_WIDTH - p1.getScreenX() < MIN_EDGE_DIST)
        {
//            System.out.format("CAM: %d, P: %d Diff: %d\n", (int)cameraX, (int)p1.getScreenX(),(int)(Settings.WINDOW_WIDTH - p1.getScreenX()));
            cameraX = (int)(p1.getWorldX() - Settings.WINDOW_WIDTH /2 + MIN_EDGE_DIST);
        }
        else if (p1.getScreenX() < MIN_EDGE_DIST)
        {
            cameraX = (int)(p1.getWorldX() + Settings.WINDOW_WIDTH /2 - MIN_EDGE_DIST);
        }

        if(Settings.FROM_ABOVE)
        {
            if (Settings.WINDOW_HEIGHT - p1.getScreenY() < MIN_EDGE_DIST)
            {
                cameraY = (int)(p1.getWorldY() - Settings.WINDOW_HEIGHT /2 + MIN_EDGE_DIST);
            }
            else if (p1.getScreenY() < MIN_EDGE_DIST)
            {
                cameraY = (int)(p1.getWorldY() + Settings.WINDOW_HEIGHT /2 - MIN_EDGE_DIST);
            }
        }
        getCamera().setCameraPosition(cameraX, cameraY);

        world.setFocusPoint(cameraX, cameraY);
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

    }

    @Override
    protected void onClose()
    {

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
        world.removeObject(body.getCollisionBox());
    }

    class MouseDebug extends Entity
    {
        Circle circle;
        Line line;

        MouseDebug()
        {
            circle = new Circle(10, Color.RED);
            addVisual(circle);
            line = new Line(0,0,0,0);
            line.setStrokeWidth(2);
            line.setStroke(Color.RED);
            addVisual(line);
        }

        @Override
        public void update()
        {
            setLocation(mouseBinding.getMouseX(), mouseBinding.getMouseY());
            PhysicsObject p1Box = players.get(0).getCollisionBox();
            line.setEndX(p1Box.getX() - mouseBinding.getMouseX());
            line.setEndY(p1Box.getY() - mouseBinding.getMouseY());
        }
    }
}
