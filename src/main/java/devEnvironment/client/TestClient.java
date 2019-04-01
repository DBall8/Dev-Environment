package devEnvironment.client;

import networkManager.Connection;
import networkManager.protocols.BasicClientProtocol;

public class TestClient {
    public static void main(String[] args) {
        BasicClientProtocol protocol = new BasicClientProtocol(10);
        Connection client = new Connection("127.0.0.1", 8080, protocol);
        client.connect();

        client.sendMessage(new byte[]{0x12, 0x13, 0x14, 0x15});

//        client.close();

        while(true);
    }
}
