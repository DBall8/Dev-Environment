package devEnvironment.test1;

public class ClientMessage {
    private final static byte UP_BITMASK    = (byte)0x80;
    private final static byte DOWN_BITMASK  = (byte)0x40;
    private final static byte LEFT_BITMASK  = (byte)0x20;
    private final static byte RIGHT_BITMASK = (byte)0x10;
    private final static byte JUMP_BITMASK  = (byte)0x08;
    private final static byte SHOOT_BITMASK = (byte)0x04;
    private final static byte BOOST_BITMASK = (byte)0x02;

    public final static int LENGTH = 5;

    private byte inputs;

    private short mouseX = 0;
    private short mouseY = 0;

    private String senderIp = null;

    public ClientMessage(byte inputs, short mouseX, short mouseY)
    {
        this.inputs = inputs;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public ClientMessage(boolean isUp, boolean isDown, boolean isLeft, boolean isRight, boolean isJump, boolean isShoot,
                         boolean isBoost, short mouseX, short mouseY)
    {
        inputs = 0;
        if(isUp)    inputs |= UP_BITMASK;
        if(isDown)  inputs |= DOWN_BITMASK;
        if(isLeft)  inputs |= LEFT_BITMASK;
        if(isRight) inputs |= RIGHT_BITMASK;
        if(isJump)  inputs |= JUMP_BITMASK;
        if(isShoot) inputs |= SHOOT_BITMASK;
        if(isBoost) inputs |= BOOST_BITMASK;

        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public byte getInputs(){ return inputs; }

    public boolean isUp()       { return (inputs & UP_BITMASK)      != 0; }
    public boolean isDown()     { return (inputs & DOWN_BITMASK)    != 0; }
    public boolean isLeft()     { return (inputs & LEFT_BITMASK)    != 0; }
    public boolean isRight()    { return (inputs & RIGHT_BITMASK)   != 0; }
    public boolean isJump()     { return (inputs & JUMP_BITMASK)    != 0; }
    public boolean isShoot()    { return (inputs & SHOOT_BITMASK)   != 0; }
    public boolean isBoost()    { return (inputs & BOOST_BITMASK)   != 0; }
    public short getMouseX(){ return mouseX; }
    public short getMouseY(){ return mouseY; }

    public void setSenderIp(String ip){ this.senderIp = ip; }
    public String getSenderIp(){ return senderIp; }
}
