
#ifndef ASS3_SOCKETREADTASK_H
#define ASS3_SOCKETREADTASK_H

#include "ConnectionHandler.h"
#include "ClientEncDec.h"

class SocketReadTask {

private:
    ConnectionHandler *ch;
    ClientEncDec encDec;
    bool &logOut;
    bool &shouldTerminate;

public:
    SocketReadTask(ConnectionHandler *connectionHandler, bool &logout, bool &shouldTerminate);

    void run();

};


#endif //ASS3_SOCKETREADTASK_H
