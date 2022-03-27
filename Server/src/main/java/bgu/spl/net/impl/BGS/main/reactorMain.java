package bgu.spl.net.impl.BGS.main;
import bgu.spl.net.impl.BGS.BidiEncDec;
import bgu.spl.net.impl.BGS.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.Reactor;

public class reactorMain {

    public static void main(String[] args) {
        Reactor server = new Reactor(Integer.decode(args[0]), Integer.decode(args[1]), ()-> new BidiMessagingProtocolImpl(), ()-> new BidiEncDec());
        server.serve();
    }
}


