package bgu.spl.net.impl.BGS.Message;

import bgu.spl.net.impl.BGS.BidiMessagingProtocolImpl;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ACKMessage extends Message{

    //fields
    short messageOp;
    String followData;
    short[][] usersData;


    //constructor
    public ACKMessage(short messageOp) {
        super((short)10);
        this.messageOp = messageOp;
    }

    public ACKMessage(short messageOp, String name) {
        super((short)10);
        this.messageOp = messageOp;
        followData = name;
    }

    public ACKMessage(short messageOp, short[][] data) {
        super((short) 10);
        this.messageOp = messageOp;
        this.usersData = data;
    }

    public byte[] encode(){
        byte[] opCodeArr = shortToBytes(this.op);
        byte[] messageOpArr = shortToBytes(this.messageOp);
        byte[] output;
        int ind = 0;

        if(messageOp == 4) {
            byte[] userNameBytes = followData.getBytes(StandardCharsets.UTF_8);
            output = new byte[opCodeArr.length + messageOpArr.length + userNameBytes.length + 2];
            ind = insert(opCodeArr, output, ind);
            ind = insert(messageOpArr, output, ind);
            ind = insert(userNameBytes, output, ind);
            output[ind] = '\0';
            ind++;
            output[ind] = ';';
            return output;
        }
        else if(messageOp == 7 | messageOp == 8) {
            output = new byte[12 * usersData.length + 7];
            byte[] numOfUsersArr = shortToBytes((short)usersData.length);
            ind = insert(opCodeArr, output, ind);
            ind = insert(messageOpArr, output, ind);
            ind = insert(numOfUsersArr, output, ind);

            for(int i = 0; i < usersData.length; i++) {
                for(int j = 0; j < usersData[i].length; j++) {
                    byte[] bytesArr = shortToBytes(usersData[i][j]);
                    ind = insert(bytesArr, output, ind);
                }
            }
            output[output.length-1] = ';';
            System.out.println(Arrays.toString(output));
            return output;
        }
        else {
            output = new byte[5];
            ind = insert(opCodeArr, output, ind);
            ind = insert(messageOpArr, output, ind);
            output[ind] = ';';
            return output;
        }
    }

    private int insert(byte[] src, byte[] dest, int index) {
        for(byte b : src) {
            dest[index] = b;
            index++;
        }
        return index;
    }

    public void process(BidiMessagingProtocolImpl protocol){}
}
