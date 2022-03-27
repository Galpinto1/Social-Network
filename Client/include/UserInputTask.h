
#ifndef ASS3_USERINPUTTASK_H
#define ASS3_USERINPUTTASK_H
#include "ConnectionHandler.h"
#include "ClientEncDec.h"

class UserInputTask {

private:
    ConnectionHandler *ch;
    ClientEncDec encDec;
    bool& logOut;
    bool& shouldTerminate;

public:
    UserInputTask(ConnectionHandler *connectionHandler, bool &logout, bool &shouldTerminate);
    void run();

};

#endif //ASS3_USERINPUTTASK_H
