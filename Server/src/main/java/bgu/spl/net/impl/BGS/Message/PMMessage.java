package bgu.spl.net.impl.BGS.Message;

import bgu.spl.net.impl.BGS.BidiMessagingProtocolImpl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;

public class PMMessage extends Message{

    //fields
    String userNameToSend;
    String content;
    LinkedList<String> filteredWords;
    int zeroCounter;


    //constructor
    public PMMessage(byte[] bytes) {
        super((short)6);
        zeroCounter = 2;
        filteredWords = new LinkedList<>();
        filteredWords.add("Covid");
        filteredWords.add("war");
        filteredWords.add("Bennet");

        int userNameLen = 0;
        int contentLen = 0;

        byte[] usernameBytes = new byte[bytes.length];
        byte[] contentBytes = new byte[bytes.length];

        for(int i = 0; i < bytes.length && zeroCounter > 0; i++) {
            byte curr = bytes[i];
            if(curr != '\0') {
                if (zeroCounter == 2) {
                    usernameBytes[userNameLen] = curr;
                    userNameLen++;
                }
                if (zeroCounter == 1) {
                    contentBytes[contentLen] = curr;
                    contentLen++;
                }
            }
            else
                zeroCounter--;
        }

        userNameToSend = new String(usernameBytes, 0 , userNameLen, StandardCharsets.UTF_8);
        content = new String(contentBytes, 0 , contentLen, StandardCharsets.UTF_8);
    }

    public byte[] encode(){
        return null;
    }

    public void process(BidiMessagingProtocolImpl protocol) {
        if (protocol.getUser() == null || !protocol.getUser().isLoggedIn() ||
            protocol.getDataBase().getUser(userNameToSend) == null ||
            !protocol.getUser().following(userNameToSend)){
            protocol.getConnections().send(protocol.getConnectionId(), new ErrorMessage((short) 6));
        }
        else {
            String[] separatedWords = content.split("\\s+");
            String filteredContent = "";
            for(int i = 0; i < separatedWords.length; i++) {
                if(filteredWords.contains(separatedWords[i]))
                    separatedWords[i] = "<filtered>";
                if(i != separatedWords.length-1)
                    filteredContent += separatedWords[i] + " ";
                else
                    filteredContent += separatedWords[i];
            }
            protocol.getDataBase().addPostOrPM(filteredContent);
            protocol.getConnections().send(protocol.getConnectionId(), new ACKMessage((short) 6));
            protocol.getConnections().send(protocol.getDataBase().getUser(userNameToSend).getConnectionId(), new NotificationMessage((byte) 0, protocol.getUser().getUsername(), filteredContent));
        }
    }
}
