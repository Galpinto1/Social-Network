package bgu.spl.net.impl.BGS.Message;

import bgu.spl.net.impl.BGS.BidiMessagingProtocolImpl;

public abstract class Message {

    //field
    short op;


    //constructor
    public Message(short op) {
        this.op = op;
    }

    public abstract byte[] encode();

    public abstract void process(BidiMessagingProtocolImpl protocol);

    public byte[] shortToBytes(short toConvert) {
        byte[] output = new byte[2];
        output[0] = (byte)((toConvert >> 8) & 0xFF);
        output[1] = (byte)(toConvert & 0xFF);
        return output;
    }

}
