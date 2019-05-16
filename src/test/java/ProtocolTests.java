import devEnvironment.test1.ClientMessage;
//import devEnvironment.test1.client.ClientProtocol;
//import devEnvironment.test1.server.ServerProtocol;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProtocolTests {

//    @Test
//    public void mousePositionTest()
//    {
//        ServerProtocol serverProtocol = new ServerProtocol();
//        ClientProtocol clientProtocol = new ClientProtocol();
//
//        for(short i=0; i<Short.MAX_VALUE; i++)
//        {
////            byte part1 = (byte)(i >> 8);
////            byte part2 = (byte)i;
////
////            System.out.format("Part 1: %d, Part 2: %d\n", part1, part2);
////            System.out.format("Part 1: 0x%x, Part 2: 0x%x\n", part1, part2);
////
////            short part2part2 = (short)(0 | (0xff & part2));
////            int whole = ((part1 << 8) | part2part2);
////            System.out.format("Original: %d, Result: %d\n", i, whole);
////            System.out.format("Original: 0x%x, Result: 0x%x\n", i, whole);
////
////            assertEquals(i, whole);
//            ClientMessage sentMessage = new ClientMessage((byte)0, (short)i, (short)i);
//
//
//            byte[] messageBytes = clientProtocol.convertToMessage(sentMessage);
//            ClientMessage receivedMessage = convertBytesToMessage(messageBytes);
//
//            assertEquals(receivedMessage.getMouseY(), sentMessage.getMouseY());
//            assertEquals(receivedMessage.getMouseX(), sentMessage.getMouseX());
//        }
//
//
//    }

    private ClientMessage convertBytesToMessage(byte[] bytes)
    {
        byte inputs = bytes[0];
        short mouseX = (short)(bytes[1] << 8);
        short unsignedExpansionX = (short)(0 | (0xff & bytes[2]));
        mouseX |= unsignedExpansionX;
        short mouseY = (short)(bytes[3] << 8);
        short unsignedExpansionY = (short)(0 | (0xff & bytes[4]));
        mouseY |= unsignedExpansionY;

        return new ClientMessage(inputs, mouseX, mouseY);
    }
}
