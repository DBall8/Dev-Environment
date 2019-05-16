import org.junit.Test;
import physicsEngine.Material;
import physicsEngine.PhysicsObject;
import physicsEngine.PhysicsWorld;
import physicsEngine.math.MalformedPolygonException;
import physicsEngine.math.Polygon;

import static org.junit.Assert.assertEquals;

public class AirResistance
{
    @Test
    public void testCrossSectionalArea()
    {
        try {
            Polygon p = new Polygon(new float[]{
                    -10, -2,
                    10, -2,
                    10, 2,
                    -10, 2
            });

            PhysicsWorld world = new PhysicsWorld();
            PhysicsObject obj = world.addPolygon(0, 0, p, Material.Metal);
            obj.setOrientation((float)Math.PI/2);
            assertEquals(obj.getCrossSectionalAreaX(), 20, 0.1);
            assertEquals(obj.getCrossSectionalAreaY(), 4, 0.1);

        }
        catch(MalformedPolygonException e)
        {
            e.printMessage();
        }
    }
}
