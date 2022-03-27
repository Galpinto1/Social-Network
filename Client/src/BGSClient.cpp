#include <stdlib.h>
#include <iostream>
#include <thread>
#include "../include/UserInputTask.h"
#include "../include/SocketReadTask.h"

using namespace std;

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/

int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    bool logout = false;
    bool shouldTerminate = false;

    UserInputTask uit(&connectionHandler, logout, shouldTerminate);
    thread uitThread(&UserInputTask::run, &uit);

    SocketReadTask srt(&connectionHandler, logout, shouldTerminate);
    thread srtThread(&SocketReadTask::run, &srt);

    uitThread.join();
    srtThread.join();

    return 0;
}


