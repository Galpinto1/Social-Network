#ifndef BOOST_ECHO_CLIENT_MESSAGEENCODERDECODER_H
#define BOOST_ECHO_CLIENT_MESSAGEENCODERDECODER_H
#include "ConnectionHandler.h"
#include <vector>
#include <string>

class ClientEncDec {

private:
    ConnectionHandler* ch;
    short opCode;

public :
    ClientEncDec(ConnectionHandler* ch);
    ClientEncDec(const ClientEncDec& other);
    ClientEncDec& operator=(const ClientEncDec& other);
    std::string decode();
    std::string notificationRead();
    std::string ACKRead();
    std::string ErrorRead();
    void encode(std::string str);
    short bytesToShort(char* bytesArr);
    void shortToBytes(short num, char* bytesArr);


};


#endif //BOOST_ECHO_CLIENT_MESSAGEENCODERDECODER_H