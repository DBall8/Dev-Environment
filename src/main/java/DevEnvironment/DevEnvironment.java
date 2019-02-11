package DevEnvironment;

import GameEngine.GameEngine;
import Global.Settings;
import PhysicsEngine.Material;
import PhysicsEngine.PhysicsWorld;
import PhysicsEngine.math.MalformedPolygonException;
import PhysicsEngine.math.Point;
import PhysicsEngine.math.Polygon;
import entities.PolygonBody;
import entities.Wall;

public class DevEnvironment extends GameEngine {

    PhysicsWorld world;

    @Override
    protected void onStart()
    {
        world = new PhysicsWorld();
        settings.setFramesPerSecond(Settings.getFramerate());

        NewBody body = new NewBody(50, 50, 40, 40, Material.Wood, world);
        addEntity(body);
        body.generateKeyBindings(userInputHandler);

        NewWall wall1 = new NewWall(-30, Settings.getWindowHeight() / 2, 80, Settings.getWindowHeight(), world);
        addEntity(wall1);

        NewWall wall2 = new NewWall(Settings.getWindowWidth() + 30, Settings.getWindowHeight() / 2, 80, Settings.getWindowHeight(), world);
        addEntity(wall2);

        NewWall wall3 = new NewWall(Settings.getWindowWidth() / 2, -30, Settings.getWindowWidth() - 20, 80, world);
        addEntity(wall3);

        NewWall wall4 = new NewWall(Settings.getWindowWidth() / 2, Settings.getWindowHeight() + 30, Settings.getWindowWidth() - 20, 80, world);
        addEntity(wall4);

        try {
            Polygon p = new Polygon(new Point[]{
                    new Point(0, 0),
                    new Point(100, 100),
                    new Point(100, 0),
            });
            NewBody testPoly = new NewBody(600, 600, p, world);
            addEntity(testPoly);
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
            NewBody testPoly = new NewBody(600, 600, p, world);
            addEntity(testPoly);
        } catch (MalformedPolygonException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onUpdateStart()
    {
        float alpha = world.update(1.0f/ settings.getFramesPerSecond());
    }

    @Override
    public int getWindowWidth() {
        return Settings.getWindowWidth();
    }

    @Override
    public int getWindowHeight() {
        return Settings.getWindowHeight();
    }
}
