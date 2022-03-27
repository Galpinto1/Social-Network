package bgu.spl.net.impl.BGS.Message;
import bgu.spl.net.impl.BGS.BidiMessagingProtocolImpl;
import bgu.spl.net.impl.BGS.User;
import java.nio.charset.StandardCharsets;

public class PostMessage extends Message{
    //fields
    String content;
    int zeroCounter;


    //constructor
    public PostMessage(byte[] bytes) {
        super((short)5);
        zeroCounter = 1;

        int contentLen = 0;
        byte[] contentBytes = new byte[bytes.length];

        for(int i = 0; i < bytes.length && zeroCounter > 0; i++) {
            byte curr = bytes[i];
            if(curr != '\0') {
                if (zeroCounter == 1) {
                    contentBytes[contentLen] = curr;
                    contentLen++;
                }
            }
            else
                zeroCounter--;
        }

        content = new String(contentBytes, 0 , contentLen, StandardCharsets.UTF_8);
    }

    public byte[] encode(){
        return null;
    }

    public void process(BidiMessagingProtocolImpl protocol) {
        if (protocol.getUser() == null || !protocol.getUser().isLoggedIn()){
            protocol.getConnections().send(protocol.getConnectionId(), new ErrorMessage((short) 5));
        }
        else{
            protocol.getDataBase().addPostOrPM(content);
            protocol.getUser().incPosts();
            protocol.getConnections().send(protocol.getConnectionId(), new ACKMessage((short) 5));
            for(User follower : protocol.getUser().getFollowers().values()){
                protocol.getConnections().send(follower.getConnectionId(), new NotificationMessage((byte)1, protocol.getUser().getUsername(), content));
            }
            String copy = content;
            while(copy.contains("@")) {
                int i = copy.indexOf("@");
                copy = copy.substring(i+1);
                int j = copy.indexOf(" ");
                String taggedUser = copy.substring(0, j);
                copy = copy.substring(j+1);
                if(!protocol.getUser().getFollowers().containsKey(taggedUser) &&
                    protocol.getUser().blocked(taggedUser)) {
                    protocol.getConnections().send(protocol.getDataBase().getUser(taggedUser).getConnectionId(), new NotificationMessage((byte)1, protocol.getUser().getUsername(), content));
                }
            }
        }
    }
}

