#include <boost/algorithm/string.hpp>
#include "../include/SocketReadTask.h"

using namespace std;

SocketReadTask:: SocketReadTask(ConnectionHandler* connectionHandler, bool& logout, bool& shouldTerminate):ch(connectionHandler), encDec(ClientEncDec(ch)), logOut(logout), shouldTerminate(shouldTerminate){}

void SocketReadTask::run() {

    while (!shouldTerminate) {
        string out = encDec.decode();
        if(out == "ACK 3") {
            shouldTerminate = true;
            cout << out << endl;
            ch->close();
            break;
        }
        else if(out == "ERROR 3") {
            logOut = false;
        }
        cout << out << endl;
        }
    exit(EXIT_SUCCESS);
}

