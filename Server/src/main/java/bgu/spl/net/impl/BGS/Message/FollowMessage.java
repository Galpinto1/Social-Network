package bgu.spl.net.impl.BGS.Message;

import bgu.spl.net.impl.BGS.BidiMessagingProtocolImpl;
import bgu.spl.net.impl.BGS.User;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FollowMessage extends Message{
    //fields
    String userNameToFollow;
    String followOrUnfollow;
    int zeroCounter;


    //constructor
    public FollowMessage(byte[] bytes) {
        super((short)4);
        zeroCounter = 2;

        int followOrUnfollowLen = 0;
        int userToFollowLen = 0;

        byte[] followOrUnfollowBytes = new byte[bytes.length];
        byte[] userToFollowBytes = new byte[bytes.length];

        for(int i = 0; i < bytes.length && zeroCounter > 0; i++) {
            byte curr = bytes[i];
            if(curr != '\0') {
                if (zeroCounter == 2) {
                    followOrUnfollowBytes[followOrUnfollowLen] = curr;
                    followOrUnfollowLen++;
                }
                if (zeroCounter == 1) {
                    userToFollowBytes[userToFollowLen] = curr;
                    userToFollowLen++;
                }
            }
            else
                zeroCounter--;
        }

        followOrUnfollow = new String(followOrUnfollowBytes, 0 , followOrUnfollowLen, StandardCharsets.UTF_8);
        userNameToFollow = new String(userToFollowBytes, 0 , userToFollowLen, StandardCharsets.UTF_8);
    }

    public byte[] encode(){
        return null;
    }

    public void process(BidiMessagingProtocolImpl protocol) {

        if (protocol.getUser() == null || !protocol.getUser().isLoggedIn() || protocol.getDataBase().getUser(userNameToFollow) == null || (!followOrUnfollow.equals("0") && !followOrUnfollow.equals("1") ) ) {
            protocol.getConnections().send(protocol.getConnectionId(), new ErrorMessage((short) 4));
        }
        else {
            if (!protocol.getUser().blocked(userNameToFollow)) {
                User other = protocol.getDataBase().getUser(userNameToFollow);
                if (followOrUnfollow.equals("0")) { //follow case
                    if (protocol.getUser().following(userNameToFollow)) {
                        protocol.getConnections().send(protocol.getConnectionId(), new ErrorMessage((short) 4));
                    } else {
                        protocol.getUser().follow(userNameToFollow, other);
                        other.addFollower(protocol.getUser().getUsername(), protocol.getUser());
                        protocol.getConnections().send(protocol.getConnectionId(), new ACKMessage((short) 4, userNameToFollow));
                    }
                } else {
                    if (!protocol.getUser().following(userNameToFollow)) {
                        protocol.getConnections().send(protocol.getConnectionId(), new ErrorMessage((short) 4));
                    } else {
                        protocol.getUser().unfollow(userNameToFollow);
                        other.removeFollower(protocol.getUser().getUsername());
                        protocol.getConnections().send(protocol.getConnectionId(), new ACKMessage((short) 4, userNameToFollow));
                    }
                }
            }
        }
    }
}
