#include <boost/algorithm/string.hpp>
#include "../include/UserInputTask.h"

UserInputTask:: UserInputTask(ConnectionHandler* connectionHandler, bool& logout, bool& shouldTerminate):ch(connectionHandler), encDec(ClientEncDec(ch)), logOut(logout), shouldTerminate(shouldTerminate){}

void UserInputTask::run() {
    while(!shouldTerminate){
        while(!logOut) {

            //initialize buffer
            const short bufsize = 1024;
            char buf[bufsize];

            //get line from user and convert it to string
            std::cin.getline(buf, bufsize);
            std::string line(buf);

            //if user want to log out, change logout to true and send the command to the server
            if (line == "LOGOUT")
                logOut = true;

            //encode line
            encDec.encode(line);
        }
    }
}