package bgu.spl.net.impl.BGS.Message;
import bgu.spl.net.impl.BGS.BidiMessagingProtocolImpl;
import bgu.spl.net.impl.BGS.User;
import java.nio.charset.StandardCharsets;

public class BlockMessage extends Message {
    //fields
    String userNameToBlock;
    int zeroCounter;

    //constructor
    public BlockMessage(byte[] bytes) {
        super((short) 12);
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

        userNameToBlock = new String(contentBytes, 0 , contentLen, StandardCharsets.UTF_8);
    }

    public byte[] encode() {
        return null;
    }

    public void process(BidiMessagingProtocolImpl protocol) {
        if (protocol.getUser() == null || !protocol.getUser().isLoggedIn()) {
            protocol.getConnections().send(protocol.getConnectionId(), new ErrorMessage((short) 12));
        }
        else {
            User other = protocol.getDataBase().getUser(userNameToBlock);
            protocol.getUser().block(userNameToBlock, other);
            other.block(protocol.getUser().getUsername(), protocol.getUser());
            protocol.getConnections().send(protocol.getConnectionId(), new ACKMessage((short) 12));
        }
    }
}
