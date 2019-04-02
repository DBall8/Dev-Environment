package devEnvironment.test1.client;

import Global.Settings;
import devEnvironment.*;
import devEnvironment.test1.ClientMessage;
import devEnvironment.test1.server.ServerProtocol;
import gameEngine.GameEngine;
import gameEngine.userInput.MouseBinding;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import networkManager.Connection;
import physicsEngine.Material;
import physicsEngine.PhysicsWorld;
import physicsEngine.math.MalformedPolygonException;
import physicsEngine.math.Point;
import physicsEngine.math.Polygon;

import java.util.ArrayList;
import java.util.List;

public class Main extends GameEngine
{
    PhysicsWorld world;
    MouseBinding mouseBinding;
    List<Body> bodies = new ArrayList<>();
    List<Player> players = new ArrayList<>();

    PlayerKeyListener keyListener;

    Group debugGroup = new Group();

    Environment environment;

    ClientProtocol protocol;
    Connection connection;

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

        protocol = new ClientProtocol();
        connection = new Connection("127.0.0.1", 8080, protocol);
    }

    @Override
    protected void onStart()
    {
        keyListener = new PlayerKeyListener(userInputHandler);
        mouseBinding = userInputHandler.createMouseClickBinding(MouseButton.PRIMARY);

        Wall wall1 = new Wall(-30, Settings.WINDOW_HEIGHT / 2, 80, Settings.WINDOW_HEIGHT, world);
        addEntity(wall1);

        Wall wall2 = new Wall(Settings.WINDOW_WIDTH + 30, Settings.WINDOW_HEIGHT / 2, 80, Settings.WINDOW_HEIGHT, world);
        addEntity(wall2);

        Wall wall3 = new Wall(Settings.WINDOW_WIDTH / 2, -30, Settings.WINDOW_WIDTH - 20, 80, world);
        addEntity(wall3);

        Wall wall4 = new Wall(Settings.WINDOW_WIDTH / 4, Settings.WINDOW_HEIGHT + 30, Settings.WINDOW_WIDTH/2 - 20, 80, world);
        addEntity(wall4);

        Wall wall5 = new Wall(3 * Settings.WINDOW_WIDTH / 4, Settings.WINDOW_HEIGHT + 30, Settings.WINDOW_WIDTH/2 - 20, 80, world);
        addEntity(wall5);

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

        connection.connect();
    }

    @Override
    protected void onUpdateStart()
    {
        ClientMessage message = keyListener.getAsMessage();
        connection.sendMessage(protocol.convertToMessage(message));
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
        float alpha = world.update(1.0f/ getFramesPerSecond());
        alphaAdjust(alpha);
    }

    @Override
    protected void onClose()
    {
        connection.close();
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
}
