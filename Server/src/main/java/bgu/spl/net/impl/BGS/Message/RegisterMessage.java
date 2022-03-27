package bgu.spl.net.impl.BGS.Message;
import bgu.spl.net.impl.BGS.BidiMessagingProtocolImpl;
import bgu.spl.net.impl.BGS.User;

import java.nio.charset.StandardCharsets;


public class RegisterMessage extends Message{

    //fields
    String userName = "";
    String password = "";
    String birthday = "";
    int zeroCounter;


    //constructor
    public RegisterMessage(byte[] bytes) {
        super((short)1);
        zeroCounter = 3;

        int userNameLen = 0;
        int passwordLen = 0;
        int birthdayLen = 0;

        byte[] usernameBytes = new byte[bytes.length];
        byte[] passwordBytes = new byte[bytes.length];
        byte[] birthdayBytes = new byte[bytes.length];

        for(int i = 0; i < bytes.length && zeroCounter > 0; i++) {
            byte curr = bytes[i];
            if(curr != '\0') {
                if (zeroCounter == 3) {
                    usernameBytes[userNameLen] = curr;
                    userNameLen++;
                }
                if (zeroCounter == 2) {
                    passwordBytes[passwordLen] = curr;
                    passwordLen++;
                }
                if (zeroCounter == 1) {
                    birthdayBytes[birthdayLen] = curr;
                    birthdayLen++;
                }
            }
            else
                zeroCounter--;
        }
        userName = new String(usernameBytes, 0 , userNameLen, StandardCharsets.UTF_8);
        password = new String(passwordBytes, 0 , passwordLen, StandardCharsets.UTF_8);
        birthday = new String(birthdayBytes, 0 , birthdayLen, StandardCharsets.UTF_8);
    }

    public byte[] encode(){
        return null;
    }

    public void process(BidiMessagingProtocolImpl protocol) {
        if (protocol.getUser() != null) {
            protocol.getConnections().send(protocol.getConnectionId(), new ErrorMessage((short)1));

        } else {
            User user = new User(userName, password, birthday);
            protocol.setUser(user);
            protocol.getDataBase().getUsers().put(userName, user);
            user.setConnectionId(protocol.getConnectionId());
            protocol.getConnections().send(protocol.getConnectionId(), new ACKMessage((short)1));
        }
    }
}
