package bgu.spl.net.impl.BGS.Message;

import bgu.spl.net.impl.BGS.BidiMessagingProtocolImpl;

public class LogoutMessage extends Message{

    public LogoutMessage () {
        super((short)3);
    }

    public byte[] encode(){
        return null;
    }

    public void process(BidiMessagingProtocolImpl protocol){
        if (protocol.getUser() == null || !protocol.getUser().isLoggedIn()){
            protocol.getConnections().send(protocol.getConnectionId(), new ErrorMessage((short) 3));
        }
        else {
            protocol.getUser().Logout();
            protocol.getDataBase().decLoggedIn();
            protocol.getConnections().send(protocol.getConnectionId(), new ACKMessage((short)3));
        }
    }
}
