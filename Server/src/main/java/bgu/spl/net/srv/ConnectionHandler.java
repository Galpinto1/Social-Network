package bgu.spl.net.srv;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;

import java.io.Closeable;
import java.io.IOException;

public interface ConnectionHandler<T> extends Closeable{

    void send(T msg) ;
     BidiMessagingProtocol getProtocol();

}
