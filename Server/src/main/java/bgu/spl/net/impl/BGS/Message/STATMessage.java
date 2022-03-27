package bgu.spl.net.impl.BGS.Message;
import bgu.spl.net.impl.BGS.BidiMessagingProtocolImpl;
import bgu.spl.net.impl.BGS.User;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class STATMessage extends Message {
    //fields
    String userNamesString;
    List<String> userNamesList;
    int zeroCounter;

    //constructor
    public STATMessage(byte[] bytes) {
        super((short) 8);
        userNamesList = new ArrayList<>();
        zeroCounter = 1;

        int contentLen = 0;
        byte[] contentBytes = new byte[bytes.length];

        for (int i = 0; i < bytes.length && zeroCounter > 0; i++) {
            byte curr = bytes[i];
            if (curr != '\0') {
                if (zeroCounter == 1) {
                    contentBytes[contentLen] = curr;
                    contentLen++;
                }
            } else
                zeroCounter--;
        }

        userNamesString = new String(contentBytes, 0, contentLen, StandardCharsets.UTF_8);
        String[] tmp = userNamesString.split("\\|", -1);
        for (int i = 0; i < tmp.length; i++) {
            userNamesList.add(tmp[i]);
        }
    }

    public byte[] encode() {
        return null;
    }

    public void process(BidiMessagingProtocolImpl protocol) {
        if (protocol.getUser() == null || !protocol.getUser().isLoggedIn()) {
            protocol.getConnections().send(protocol.getConnectionId(), new ErrorMessage((short) 8));
        } else {
            int validUsersCount = 0;
            ArrayList<Integer> validUsersIndexes = new ArrayList<>();
            int index = 0;
            for (int i = 0; i < userNamesList.size(); i++) {
                User user = protocol.getDataBase().getUser(userNamesList.get(i));
                if (user != null && !protocol.getUser().blocked(user.getUsername())) {
                    validUsersCount++;
                    validUsersIndexes.add(i);
                }
            }
            short[][] info = new short[validUsersCount][6];
            for (int i = 0; i < validUsersIndexes.size(); i++) {
                User user = protocol.getDataBase().getUser(userNamesList.get(validUsersIndexes.get(i)));
                info[index][0] = (short) 10;
                info[index][1] = (short) 8;
                info[index][2] = (short) user.getAge();
                info[index][3] = (short) user.getNumOfPosts();
                info[index][4] = (short) user.getFollowers().size();
                info[index][5] = (short) user.getNumOfFollowing();
                index++;
            }
            protocol.getConnections().send(protocol.getConnectionId(), new ACKMessage((short) 8, info));
        }
    }
}
