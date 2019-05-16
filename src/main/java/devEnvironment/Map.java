package devEnvironment;

import Global.Settings;
import physicsEngine.math.MalformedPolygonException;
import physicsEngine.math.Point;
import physicsEngine.math.Polygon;

import java.util.Set;

public class Map
{
    // Random terrain parameters
    final static int TOTAL_WORLD_WIDTH = 10000;
    final static int HEIGHT_VARIANCE_MAX = 10;
    final static int HEIGHT_VARIANCE_MIN = 5;
    final static int WIDTH_MIN = 1;
    final static int WIDTH_MAX = 100;
    final static int START_HEIGHT = 50;
    final static int DEPTH = 100;

    final static float TINY_VALUE = 1f;


    Environment environment;

    public Map(Environment environment)
    {
        this.environment = environment;
    }

    public void initialize()
    {
        // Left
        Wall wall1 = new Wall(-30, Settings.WINDOW_HEIGHT / 2, 80, Settings.WINDOW_HEIGHT, environment.getWorld());
        environment.addBody(wall1);

        // Right
//        Wall wall2 = new Wall(Settings.WINDOW_WIDTH + 30, Settings.WINDOW_HEIGHT / 2, 80, Settings.WINDOW_HEIGHT, world);
//        addEntity(wall2);

        // Top
        Wall wall3 = new Wall(Settings.WINDOW_WIDTH / 2, -30, Settings.WINDOW_WIDTH - 20, 80, environment.getWorld());
        environment.addBody(wall3);

        // Bottom L
        Wall wall4 = new Wall(Settings.WINDOW_WIDTH / 4, Settings.WINDOW_HEIGHT + 30, Settings.WINDOW_WIDTH/2 - 20, 80, environment.getWorld());
        environment.addBody(wall4);

        // Bottom R
        Wall wall5 = new Wall(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT + 30, Settings.WINDOW_WIDTH - 20, 80, environment.getWorld());
        environment.addBody(wall5);

        generateRandomTerrainSecondDeriv();
    }

    void generateRandomTerrainFirstDeriv()
    {
        int distance = Settings.WINDOW_WIDTH;
        int height = START_HEIGHT;
        while(distance < TOTAL_WORLD_WIDTH)
        {
            int deltaX = (int)((Math.random() * (WIDTH_MAX - WIDTH_MIN)) + WIDTH_MIN);
            int deltaY = (int)((Math.random() * (HEIGHT_VARIANCE_MAX - HEIGHT_VARIANCE_MIN) + HEIGHT_VARIANCE_MIN));
            if(Math.random() > 0.5)
            {
                deltaY *= -1;
            }

            if(height + deltaY < 0 || height + deltaY > Settings.WINDOW_HEIGHT)
            {
                deltaY *= -1;
            }

            try {
                Polygon p = new Polygon(new Point[]{
                        new Point(distance, Settings.WINDOW_HEIGHT),
                        new Point(distance, Settings.WINDOW_HEIGHT - height),
                        new Point(distance + deltaX + TINY_VALUE, Settings.WINDOW_HEIGHT - height - deltaY),
                        new Point(distance + deltaX + TINY_VALUE, Settings.WINDOW_HEIGHT)
                });

                Point bottomLeftCorner = p.getPoints()[0];

                Wall w = new Wall((int)(distance - bottomLeftCorner.getX()),
                                    (int)(Settings.WINDOW_HEIGHT - bottomLeftCorner.getY()),
                                     p,
                                     environment.getWorld());
                environment.addBody(w);

                distance += deltaX;
                height += deltaY;
            }
            catch (MalformedPolygonException e)
            {
                System.err.println("Terrain generation failure!");
            }
        }
    }

    void generateRandomTerrainSecondDeriv()
    {
        int distance = Settings.WINDOW_WIDTH;
        int height = START_HEIGHT;
        int deltaY = 0;
        while(distance < TOTAL_WORLD_WIDTH)
        {
            int deltaX = (int)((Math.random() * (WIDTH_MAX - WIDTH_MIN)) + WIDTH_MIN);
            int accelY = (int)((Math.random() * (HEIGHT_VARIANCE_MAX - HEIGHT_VARIANCE_MIN) + HEIGHT_VARIANCE_MIN));
            if(Math.random() > 0.5)
            {
                accelY *= -1;
            }

            if(height + deltaY + accelY < 0 || height + deltaY + accelY > Settings.WINDOW_HEIGHT)
            {
               deltaY = 0;
               accelY = 0;
            }

            try {
                Polygon p = new Polygon(new Point[]{
                        new Point(distance, Settings.WINDOW_HEIGHT + DEPTH),
                        new Point(distance, Settings.WINDOW_HEIGHT - height),
                        new Point(distance + deltaX + TINY_VALUE, Settings.WINDOW_HEIGHT - height - deltaY - accelY),
                        new Point(distance + deltaX + TINY_VALUE, Settings.WINDOW_HEIGHT + DEPTH)
                });

                Point bottomLeftCorner = p.getPoints()[0];

                Wall w = new Wall((int)(distance - bottomLeftCorner.getX()),
                        (int)(Settings.WINDOW_HEIGHT - bottomLeftCorner.getY()),
                        p,
                        environment.getWorld());
                environment.addBody(w);

                if(Settings.FROM_ABOVE)
                {
                    p = new Polygon(new Point[]{
                            new Point(distance, -DEPTH),
                            new Point(distance, height),
                            new Point(distance + deltaX + TINY_VALUE, height + deltaY + accelY),
                            new Point(distance + deltaX + TINY_VALUE, -DEPTH)
                    });

                    Point topLeftCorner = p.getPoints()[0];

                    w = new Wall((int)(distance - bottomLeftCorner.getX()),
                            (int)(bottomLeftCorner.getY()),
                            p,
                            environment.getWorld());
                    environment.addBody(w);
                }

                distance += deltaX;
                deltaY += accelY;
                height += deltaY;

                if(Settings.FROM_ABOVE && height > Settings.WINDOW_HEIGHT/2) return;
            }
            catch (MalformedPolygonException e)
            {
                System.err.println("Terrain generation failure!");
            }
        }
    }
}
