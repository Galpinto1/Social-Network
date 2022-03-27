package bgu.spl.net.impl.BGS.Message;
import bgu.spl.net.impl.BGS.BidiMessagingProtocolImpl;
import java.nio.charset.StandardCharsets;


public class NotificationMessage extends Message{
    //fields
    byte type;
    String postingUser;
    String content;
    int zeroCounter;

    //constructor
    public NotificationMessage(byte type,String postingUser,String content){
        super((short)9);
        this.type = type;
        this.postingUser = postingUser;
        this.content = content;
        zeroCounter = 2;
    }

    public byte[] encode(){
        byte[] opCodeArr = shortToBytes(this.op);
        String spaced = postingUser+" ";
        byte[] postingUserArr = spaced.getBytes(StandardCharsets.UTF_8);
        byte[] contentArr = this.content.getBytes(StandardCharsets.UTF_8);
        byte[] output = new byte[opCodeArr.length + postingUserArr.length + contentArr.length + 4];
        int ind = 0;

        for(byte b : opCodeArr) {
            output[ind] = b;
            ind++;
        }

        output[ind] = this.type;
        ind++;

        for(byte b : postingUserArr) {
            output[ind] = b;
            ind++;
        }

        output[ind] = '\0';
        ind++;

        for(byte b : contentArr) {
            output[ind] = b;
            ind++;
        }
        output[ind] = '\0';
        ind++;
        output[ind] = ';';

        return output;
    }

    public void process(BidiMessagingProtocolImpl protocol){}
}
