package bgu.spl.net.impl.BGS.Message;

import bgu.spl.net.impl.BGS.BidiMessagingProtocolImpl;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ErrorMessage extends Message{

    //fields
    short messageOp;

    //constructor
    public ErrorMessage(short messageOp) {
        super((short) 11);
        this.messageOp = messageOp;
    }

    public byte[] encode(){
        byte[] opCodeArr = shortToBytes(this.op);
        byte[] messageOpArr = shortToBytes(this.messageOp);
        byte[] output = new byte[opCodeArr.length + messageOpArr.length + 1];
        int ind = 0;

        for(byte b : opCodeArr) {
            output[ind] = b;
            ind++;
        }

        for(byte b : messageOpArr) {
            output[ind] = b;
            ind++;
        }
        output[ind] = ';';
        System.out.println(Arrays.toString(output));
        return output;
    }

    public void process(BidiMessagingProtocolImpl protocol){}

}
