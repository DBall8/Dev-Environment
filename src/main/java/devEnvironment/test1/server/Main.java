package devEnvironment.test1.server;

import Global.Settings;
import devEnvironment.*;
import devEnvironment.test1.ClientMessage;
import devEnvironment.test1.ClientMessageProtocol;
import gameEngine.GameEngine;
import gameEngine.userInput.MouseBinding;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import networkManager.Server;
import physicsEngine.Material;
import physicsEngine.PhysicsWorld;
import physicsEngine.math.MalformedPolygonException;
import physicsEngine.math.Point;
import physicsEngine.math.Polygon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends GameEngine
{
    PhysicsWorld world;
    MouseBinding mouseBinding;
    List<Body> bodies = new ArrayList<>();
    List<Player> players = new ArrayList<>();
    HashMap<String, RemoteControlledPlayer> playerMap = new HashMap<>();

    Group debugGroup = new Group();

    Environment environment;

    ClientMessageProtocol clientMessageProtocol;
    Server server;

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

        server = new Server(8080, 10);
        clientMessageProtocol = new ClientMessageProtocol(server, 30);
    }

    @Override
    protected void onStart()
    {
        mouseBinding = getUserInputHandler().createMouseClickBinding(MouseButton.PRIMARY);

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

        server.start();
    }

    @Override
    protected void onUpdateStart()
    {
        handleClientMessages(clientMessageProtocol.getReceivedMessages());
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
        server.close();
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

    private void handleClientMessages(List<ClientMessage> messages)
    {
        for(ClientMessage message: messages)
        {
            if(!playerMap.containsKey(message.getSenderIp()))
            {
                RemoteControlledPlayer newPlayer = new RemoteControlledPlayer(message.getSenderIp(), 50, 50, 40, 40, Material.Wood, world);
                newPlayer.generateKeyBindings(getUserInputHandler(), environment, (short)0);
                playerMap.put(message.getSenderIp(), newPlayer);
                addBody(newPlayer);
                players.add(newPlayer);
            }

            playerMap.get(message.getSenderIp()).applyMessage(message);
        }
    }
}
