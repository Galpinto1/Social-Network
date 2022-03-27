package bgu.spl.net.impl.BGS.main;
import bgu.spl.net.impl.BGS.BidiEncDec;
import bgu.spl.net.impl.BGS.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.Server;

public class threadPerClientMain {

    public static void main(String[] args) {
        // you can use any server...
        Server.threadPerClient(Integer.decode(args[0]), //port
                () -> new BidiMessagingProtocolImpl(), //protocol factory
                ()-> new BidiEncDec() //message encoder decoder factory
        ).serve();
    }
}
