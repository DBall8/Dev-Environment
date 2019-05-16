package devEnvironment.test1;

import networkManager.NetworkConnection;
import networkManager.protocols.MessageConversionError;
import networkManager.protocols.Protocol;

import java.util.ArrayList;
import java.util.List;


public class ClientMessageProtocol extends Protocol<ClientMessage>
{
    private List<ClientMessage> receivedMessages = new ArrayList<>();
    private int numMessagesReceived = 0;

    public ClientMessageProtocol(NetworkConnection connection, int maxMessageLength)
    {
        super(connection, maxMessageLength);
    }

    @Override
    protected ClientMessage convertBytesToMessage(byte[] bytes) throws MessageConversionError{

        if(bytes.length < 5)
        {
            throw new MessageConversionError("Message was too short.", bytes);
        }
        byte inputs = bytes[0];
        short mouseX = (short)(bytes[1] << 8);
        short unsignedExpansionX = (short)(0 | (0xff & bytes[2]));
        mouseX |= unsignedExpansionX;
        short mouseY = (short)(bytes[3] << 8);
        short unsignedExpansionY = (short)(0 | (0xff & bytes[4]));
        mouseY |= unsignedExpansionY;

//            System.out.format("0x%x%x\n", buffer.get(3), buffer.get(4));
//            System.out.format("BECOMES 0x%x\n", (short)(buffer.get(3) << 8));
//            System.out.format("FINALLY 0x%x\n", mouseY);

        return new ClientMessage(inputs, mouseX, mouseY);

    }

    @Override
    protected void handleMessageReceived(String ip, ClientMessage message)
    {
        message.setSenderIp(ip);
        addMessageReceived(message);
    }

    @Override
    public byte[] convertMessageToBytes(ClientMessage clientMessage) throws MessageConversionError
    {
        byte[] message = new byte[ClientMessage.LENGTH + 1];
        message[0] = clientMessage.getInputs();
        message[1] = (byte)(clientMessage.getMouseX() >> 8);
        message[2] = (byte)(clientMessage.getMouseX());
        message[3] = (byte)(clientMessage.getMouseY() >> 8);
        message[4] = (byte)(clientMessage.getMouseY());

//        System.out.format("0x%x%x\n", message[3], message[4]);

        return message;
    }

    private synchronized void addMessageReceived(ClientMessage message)
    {
        receivedMessages.add(message);
        numMessagesReceived++;
    }

    public synchronized List<ClientMessage> getReceivedMessages()
    {
        List<ClientMessage> messages = new ArrayList<>(numMessagesReceived);
        for (ClientMessage message: receivedMessages)
        {
            messages.add(message);
        }

        receivedMessages.clear();
        numMessagesReceived = 0;

        return messages;
    }
}
