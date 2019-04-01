package devEnvironment.server;

import networkManager.Connection;
import networkManager.Server;
import networkManager.callback.Callback;
import networkManager.protocols.BasicServerProtocol;

public class TestServer {

    public static void main(String[] args) {
        BasicServerProtocol protocol = new BasicServerProtocol(10);
        Server server = new Server(8080, 4, protocol);
        server.setNewConnectionCallback(new Callback<Connection>() {
            @Override
            public void run(Connection parameter) {
            }
        });
        server.start();

        while(true);
    }
}
