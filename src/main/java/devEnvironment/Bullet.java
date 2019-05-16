package devEnvironment;

import Global.Settings;
import physicsEngine.Material;
import physicsEngine.PhysicsObject;
import physicsEngine.callback.Callback;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Bullet extends Body {
    private final static int WIDTH = 5;
    private final static int HEIGHT = 20;
    private final static int FORCE = 80;
    private final static int LIFETIME_SEC = 1;
    private final static float ANGLE_VARIATION = 0.03f;
    private final static int RECOIL_DIVISOR_ABOVE = 6;
    private final static int RECOIL_DIVISOR_PLATFORM = 4;

    private static List<Player> playerList;

    private Player owner;

    public Bullet(Player p, Environment environment) {
        super(p.collisionBox.getX(), p.collisionBox.getY(), WIDTH, HEIGHT, Material.Metal, environment.getWorld());
        owner = p;
        collisionBox.ignore(p.collisionBox);
        p.collisionBox.ignore(collisionBox);

        float randomVariation = (float) (Math.random() * ANGLE_VARIATION * 2) - ANGLE_VARIATION;

        setRotation(p.collisionBox.getOrientation() + randomVariation);
        collisionBox.applyForceInDirection(FORCE, collisionBox.getOrientation());

        if (Settings.FROM_ABOVE) {
            owner.getCollisionBox().applyForceInDirection(-FORCE / RECOIL_DIVISOR_ABOVE, collisionBox.getOrientation());
        } else
        {
            owner.getCollisionBox().applyForceInDirection(-FORCE / RECOIL_DIVISOR_PLATFORM, collisionBox.getOrientation());
        }

        collisionBox.setCollisionCallback(new Callback<PhysicsObject>() {
            @Override
            public void run(PhysicsObject object) {
                for(Player p: playerList)
                {
                    if(p.equals(owner)) continue;
                    if(p.collisionBox.equals(object))
                    {
                        p.takeHit();
                    }

                }
            }
        });

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

    public static void setPlayerList(List<Player> playerList)
    {
        Bullet.playerList = playerList;
    }
}
