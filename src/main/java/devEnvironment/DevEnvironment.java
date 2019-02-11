package devEnvironment;

import gameEngine.GameEngine;
import Global.Settings;
import physicsEngine.Material;
import physicsEngine.PhysicsWorld;
import physicsEngine.math.MalformedPolygonException;
import physicsEngine.math.Point;
import physicsEngine.math.Polygon;

public class DevEnvironment extends GameEngine {

    PhysicsWorld world;

    @Override
    protected void onStart()
    {
        world = new PhysicsWorld();
        settings.setFramesPerSecond(Settings.getFramerate());

        Body body = new Body(50, 50, 40, 40, Material.Wood, world);
        addEntity(body);
        body.generateKeyBindings(userInputHandler);

        Wall wall1 = new Wall(-30, Settings.getWindowHeight() / 2, 80, Settings.getWindowHeight(), world);
        addEntity(wall1);

        Wall wall2 = new Wall(Settings.getWindowWidth() + 30, Settings.getWindowHeight() / 2, 80, Settings.getWindowHeight(), world);
        addEntity(wall2);

        Wall wall3 = new Wall(Settings.getWindowWidth() / 2, -30, Settings.getWindowWidth() - 20, 80, world);
        addEntity(wall3);

        Wall wall4 = new Wall(Settings.getWindowWidth() / 2, Settings.getWindowHeight() + 30, Settings.getWindowWidth() - 20, 80, world);
        addEntity(wall4);

        try {
            Polygon p = new Polygon(new Point[]{
                    new Point(0, 0),
                    new Point(100, 100),
                    new Point(100, 0),
            });
            Body testPoly = new Body(600, 600, p, world);
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
            Body testPoly = new Body(600, 600, p, world);
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
