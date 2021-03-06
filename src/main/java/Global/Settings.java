package Global;


public class Settings {
    public static final int FRAMERATE = 120;
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 900;

    public static final float AIR_RESISTANCE_FACTOR = 1;
    public static final boolean FROM_ABOVE = false;
    public static final float GRAVITY = FROM_ABOVE ? 0 : 10; // default 10
}
