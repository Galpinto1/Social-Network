package bgu.spl.net.impl.BGS.Message;
import bgu.spl.net.impl.BGS.BidiMessagingProtocolImpl;
import bgu.spl.net.impl.BGS.User;

import java.util.ArrayList;

public class LOGSTATMessage extends Message{

    public LOGSTATMessage () {
        super((short)7);
    }

    public byte[] encode(){
        return null;
    }

    public void process(BidiMessagingProtocolImpl protocol){
        if (protocol.getUser() == null || !protocol.getUser().isLoggedIn()){
            protocol.getConnections().send(protocol.getConnectionId(), new ErrorMessage((short) 7));
        }
        else{
            int validUsersCount = 0;
            ArrayList<String> validUsersNames = new ArrayList<>();
            int index = 0;
            for(User user : protocol.getDataBase().getUsers().values()) {
                if (user.isLoggedIn() && !protocol.getUser().blocked(user.getUsername())) {
                    validUsersCount++;
                    validUsersNames.add(user.getUsername());
                }
            }
            short[][] info = new short[validUsersCount][6];
            for(int i = 0; i < validUsersNames.size(); i++) {
                User user = protocol.getDataBase().getUser(validUsersNames.get(i));
                    info[index][0] = (short) 10;
                    info[index][1] = (short) 7;
                    info[index][2] = (short) user.getAge();
                    info[index][3] = (short) user.getNumOfPosts();
                    info[index][4] = (short) user.getFollowers().size();
                    info[index][5] = (short) user.getNumOfFollowing();
                    index++;
            }
            protocol.getConnections().send(protocol.getConnectionId(), new ACKMessage((short) 7, info));
        }
    }
}
