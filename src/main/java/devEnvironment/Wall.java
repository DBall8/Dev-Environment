package devEnvironment;

import gameEngine.Entity;
import physicsEngine.Material;
import physicsEngine.PhysicsObject;
import physicsEngine.PhysicsWorld;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Wall extends Entity {

    PhysicsObject collisionBox;

    public Wall(int x, int y, int width, int height, PhysicsWorld world)
    {
        collisionBox = world.addBox(x, y, width, height, Material.Static);
        Rectangle shape = new Rectangle(x - width/2, y - height/2, width, height);
        shape.setFill(Color.BLACK);

        addVisual(shape);
    }

    @Override
    public void update() {

    }
}
