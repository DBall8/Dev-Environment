package DevEnvironment;

import GameEngine.Entity;
import GameManager.GameManager;
import PhysicsEngine.Material;
import PhysicsEngine.PhysicsObject;
import PhysicsEngine.PhysicsWorld;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class NewWall extends Entity {

    public NewWall(int x, int y, int width, int height, PhysicsWorld world)
    {
        PhysicsObject collisionBox = world.addBox(x, y, width, height, Material.Static);
        Rectangle shape = new Rectangle(x - width/2, y - height/2, width, height);
        shape.setFill(Color.BLACK);

        addVisual(shape);
    }

    @Override
    public void update() {

    }
}
