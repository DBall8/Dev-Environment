package devEnvironment;

import physicsEngine.PhysicsWorld;
import physicsEngine.callback.Callback;

public class Environment
{
    private PhysicsWorld world;

    private Callback<Body> addBodyCallback;
    private Callback<Body> removeBodyCallback;

    public Environment(PhysicsWorld world, Callback<Body> addBodyCallback, Callback<Body> removeBodyCallback)
    {
        this.world = world;
        this.addBodyCallback = addBodyCallback;
        this.removeBodyCallback = removeBodyCallback;
    }

    public void addBody(Body b)
    {
        addBodyCallback.run(b);
    }

    public void removeBody(Body b)
    {
        removeBodyCallback.run(b);
    }

    public PhysicsWorld getWorld(){ return  world; }
}
