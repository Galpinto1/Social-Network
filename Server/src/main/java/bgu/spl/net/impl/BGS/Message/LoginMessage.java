package bgu.spl.net.impl.BGS.Message;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.impl.BGS.BidiMessagingProtocolImpl;

import java.nio.charset.StandardCharsets;

public class LoginMessage extends Message {

    //fields
    String userName;
    String password;
    String captcha;
    int zeroCounter;


    //constructor
    public LoginMessage(byte[] bytes) {
        super((short) 2);
        zeroCounter = 2;

        int userNameLen = 0;
        int passwordLen = 0;

        byte[] usernameBytes = new byte[bytes.length];
        byte[] passwordBytes = new byte[bytes.length];
        byte[] captchaBytes = new byte[1];

        for (int i = 0; i < bytes.length && zeroCounter > -1; i++) {
            byte curr = bytes[i];
            if (curr != '\0') {
                if (zeroCounter == 2) {
                    usernameBytes[userNameLen] = curr;
                    userNameLen++;
                }
                if (zeroCounter == 1) {
                    passwordBytes[passwordLen] = curr;
                    passwordLen++;
                }
                if(zeroCounter == 0) {
                    captchaBytes[0] = curr;
                    zeroCounter--;
                }
            } else
                zeroCounter--;
        }

        userName = new String(usernameBytes, 0, userNameLen, StandardCharsets.UTF_8);
        password = new String(passwordBytes, 0, passwordLen, StandardCharsets.UTF_8);
        captcha = new String(captchaBytes, 0, 1, StandardCharsets.UTF_8);
    }

    public byte[] encode() {
        return null;
    }

    public void process(BidiMessagingProtocolImpl protocol) {
        if (protocol.getDataBase().getUser(userName) == null ||
                !protocol.getDataBase().getUser(userName).getPassword().equals(password) ||
                protocol.getDataBase().getUser(userName).isLoggedIn() ||
                !captcha.equals("1")) {
            protocol.getConnections().send(protocol.getConnectionId(), new ErrorMessage((short) 2));
        }
        else if (captcha.equals("1")) {
            if (protocol.getUser() == null)
                protocol.setUser(protocol.getDataBase().getUser(userName));
            protocol.getUser().Login();
            protocol.getDataBase().incLoggedIn();
            protocol.getConnections().send(protocol.getConnectionId(), new ACKMessage((short) 2));
        }
    }
}

