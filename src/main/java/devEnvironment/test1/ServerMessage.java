package devEnvironment.test1;

import devEnvironment.Player;

import java.util.List;

public class ServerMessage {
    byte pnum;
    short xpos;
    short ypos;
    float orientation;

    public ServerMessage(List<Player> playerList)
    {
        this.pnum = pnum;
        this.xpos = xpos;
        this.ypos = ypos;
        this.orientation = orientation;
    }

    public ServerMessage(byte[] byteArray)
    {

    }

    class PlayerData
    {
        
    }
}
