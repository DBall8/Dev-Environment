package devEnvironment;

import javafx.scene.paint.Color;
import physicsEngine.Material;
import physicsEngine.PhysicsObject;
import physicsEngine.PhysicsWorld;
import physicsEngine.math.Polygon;

public class Wall extends Body {

    PhysicsObject collisionBox;

    public Wall(int x, int y, int width, int height, PhysicsWorld world)
    {
        super(x, y, width, height, Material.Static, world);
        setColor(Color.BLACK);
        this.x = x;
        this.y = y;
    }

    public Wall(int x, int y, Polygon p, PhysicsWorld world)
    {
        super(x, y, p, Material.Static, world);
        setColor(Color.BLACK);
        this.x = x;
        this.y = y;
    }

    @Override
    public void update() {

    }

    @Override
    public void alphaAdjust(float alpha)
    {
    }
}
