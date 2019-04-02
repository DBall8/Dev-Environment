package devEnvironment.test1.client;

import devEnvironment.test1.ClientMessage;
import networkManager.protocols.Protocol;

public class ClientProtocol implements Protocol
{
    private final static byte END_MSG = (byte)0xff;

    @Override
    public void handleByteReceived(String senderIp, byte receivedByte)
    {

    }

    @Override
    public byte[] prepareMessage(byte[] message)
    {
        return message;
    }

    public byte[] convertToMessage(ClientMessage clientMessage)
    {
        byte[] message = new byte[ClientMessage.LENGTH + 1];
        message[0] = clientMessage.getInputs();
        message[1] = (byte)(clientMessage.getMouseX() >> 8);
        message[2] = (byte)(clientMessage.getMouseX());
        message[3] = (byte)(clientMessage.getMouseY() >> 8);
        message[4] = (byte)(clientMessage.getMouseY());
        message[5] = END_MSG;

        System.out.println(clientMessage.getMouseY());

        return message;
    }
}
