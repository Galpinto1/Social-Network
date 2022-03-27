package bgu.spl.net.impl.BGS;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.BGS.Message.*;
import java.util.Arrays;

public class BidiEncDec implements MessageEncoderDecoder<Message> {

    //fields
    private byte[] messageBytes = new byte[1<<10];
    private byte[] opCodeBytes = new byte[2];
    int len = 0;
    short opCode = 0;
    int opCodeRead = 0;

    public Message decodeNextByte(byte nextByte){
        if(opCodeRead < 2) {
            opCodeBytes[opCodeRead] = nextByte;
            opCodeRead++;
            if(opCodeRead == 2) {
                opCode = bytesToShort(opCodeBytes);
            }
        }

        else{
            if(nextByte == ';') {
                Message msg  = findMessageType();
                reset();
                return msg;
            }
            pushByte(nextByte);
        }
        return null;
    }

    public byte[] encode(Message message){
        reset();
        return message.encode();
    }

    private void pushByte(Byte nextByte){
        if(len >= messageBytes.length) {
            messageBytes = Arrays.copyOf(messageBytes, len*2);
        }
        messageBytes[len] = nextByte;
        len++;
    }

    private short bytesToShort(byte[] bytes) {
        short output = (short)((bytes[0] & 0xff) << 8);
        output += (short)(bytes[1] & 0xff);
        return output;
    }

    private Message findMessageType(){
        if(opCode == 1)
            return new RegisterMessage(messageBytes);
        else if(opCode == 2)
            return new LoginMessage(messageBytes);
        else if (opCode == 3)
            return new LogoutMessage();
        else if(opCode == 4)
            return new FollowMessage(messageBytes);
        else if(opCode == 5)
            return new PostMessage(messageBytes);
        else if(opCode == 6)
            return new PMMessage(messageBytes);
        else if(opCode == 7)
            return new LOGSTATMessage();
        else if (opCode == 8)
            return new STATMessage(messageBytes);
        else if (opCode == 12)
            return new BlockMessage(messageBytes);
        return null;
    }
    private void reset(){
        messageBytes = new byte[1<<10];
        opCodeBytes = new byte[2];
        len = 0;
        opCode = 0;
        opCodeRead = 0;
    }
}
