package devEnvironment.test1.server;

import devEnvironment.test1.ClientMessage;
import networkManager.Buffer;
import networkManager.protocols.Protocol;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ServerProtocol implements Protocol
{
    private final static byte END_MSG = (byte)0xff;

    private HashMap<String, List<ClientMessage>> latestMessages = new HashMap<>();
    private HashMap<String, Buffer> buffers = new HashMap<>();

    @Override
    public void handleByteReceived(String senderIp, byte receivedByte)
    {
        Buffer buffer;
        if(buffers.containsKey(senderIp))
        {
            buffer = buffers.get(senderIp);
        }
        else
        {
            buffer = new Buffer(ClientMessage.LENGTH);
            buffers.put(senderIp, buffer);
        }

        if(receivedByte == END_MSG || buffer.isFull())
        {
            byte inputs = buffer.get(0);
            short mouseX = (short)(buffer.get(1) << 8);
            mouseX |= buffer.get(2);
            short mouseY = (short)(buffer.get(3) << 8);
            mouseY |= buffer.get(4);

            System.out.format("0x%x%x\n", buffer.get(3), buffer.get(4));

            saveMessage(senderIp, new ClientMessage(inputs, mouseX, mouseY));
            buffer.reset();
        }
        else {
            buffer.put(receivedByte);
        }

    }

    @Override
    public byte[] prepareMessage(byte[] message) {
        return message;
    }

    private synchronized void saveMessage(String senderIp, ClientMessage message)
    {
        if(latestMessages.containsKey(senderIp))
        {
            latestMessages.get(senderIp).add(message);
        }
        else
        {
            List<ClientMessage> list = new LinkedList<>();
            list.add(message);
            latestMessages.put(senderIp, list);
        }
    }

    public synchronized List<MessageList> getMessages()
    {
        List<MessageList> allMessages = new LinkedList<>();
        for(Map.Entry<String, List<ClientMessage>> pair: latestMessages.entrySet())
        {
            String ip = pair.getKey();
            List<ClientMessage> messages = pair.getValue();
            MessageList messageList = new MessageList(ip);
            messageList.addMessages(messages);
            allMessages.add(messageList);
        }
        return allMessages;
    }

    public class MessageList
    {
        public String ip;
        public List<ClientMessage> messages;

        public MessageList(String ip)
        {
            this.ip = ip;
            messages = new LinkedList<>();
        }

        public void addMessages(List<ClientMessage> clientMessages)
        {
            for (ClientMessage m : clientMessages)
            {
                this.messages.add(m);
            }
            clientMessages.clear();
        }
    }
}
