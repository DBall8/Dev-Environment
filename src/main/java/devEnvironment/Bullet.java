package devEnvironment;

import physicsEngine.Material;
import physicsEngine.PhysicsObject;
import physicsEngine.callback.Callback;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Bullet extends Body {
    private final static int WIDTH = 5;
    private final static int HEIGHT = 20;
    private final static int FORCE = 40;
    private final static int LIFETIME_SEC = 1;

    private static List<Player> playerList;

    private Player owner;

    public Bullet(Player p, Environment environment)
    {
        super(p.collisionBox.getX(), p.collisionBox.getY(), WIDTH, HEIGHT, Material.Metal, environment.getWorld());
        owner = p;
        collisionBox.ignore(p.collisionBox);
        p.collisionBox.ignore(collisionBox);
        setRotation(p.collisionBox.getOrientation());
        collisionBox.applyForceInDirection(FORCE, collisionBox.getOrientation());

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
