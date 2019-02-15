package devEnvironment;

import physicsEngine.Material;

import java.util.Timer;
import java.util.TimerTask;

public class Bullet extends Body {
    private final static int WIDTH = 5;
    private final static int HEIGHT = 20;
    private final static int FORCE = 40;
    private final static int LIFETIME_SEC = 1;

    public Bullet(float x, float y, float orientation, DevEnvironment environment)
    {
        super(x, y, WIDTH, HEIGHT, Material.Metal, environment.world);
        setRotation(orientation);
        collisionBox.applyForceInDirection(FORCE, orientation);

        Bullet me = this;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                environment.removeBody(me);
                timer.cancel();
            }
        }, LIFETIME_SEC * 1000);
    }
}
