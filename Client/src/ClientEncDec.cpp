
#include "../include/ClientEncDec.h"
#include "boost/lexical_cast.hpp"
#include <iostream>
#include <codecvt>
#include <cstring>
#include <boost/algorithm/string.hpp>

using namespace std;

ClientEncDec:: ClientEncDec(ConnectionHandler* ch): ch(ch), opCode(-1){}
ClientEncDec::ClientEncDec(const ClientEncDec &other): ch(other.ch), opCode(other.opCode) {}
ClientEncDec& ClientEncDec::operator=(const ClientEncDec &other) {
    ch = other.ch;
    opCode = other.opCode;
    return *this;
}

string ClientEncDec:: decode() {
    string out ="";
    char opBuffer[2];
    memset(opBuffer, 0, 2);
    ch->getBytes(opBuffer, 2);
    opCode = bytesToShort(opBuffer);
    if (opCode == 9) {
       out = notificationRead();
    } else if (opCode == 10) {
        out = ACKRead();
    } else if (opCode == 11) {
        out = ErrorRead();
    }
    return out;
}

string ClientEncDec::notificationRead() {
    string output = "";
    string data = "";
    char type[1];
    ch->getBytes(type,1);
    short typeNum = (short) ((type[0]&0xff));
    if (typeNum==0)
        output+="NOTIFICATION PM";
    else
        output+="NOTIFICATION PUBLIC";
    ch->getLine(data);
    output+= " " + data.substr(0, data.size()-1);
    return output;

}


string ClientEncDec::ACKRead() {
    string output="ACK";
    char opBuffer[2];
    ch->getBytes(opBuffer,2);
    short messageOpcode = bytesToShort(opBuffer);
    output += " " + to_string(messageOpcode);
    if (messageOpcode==4) {
        string data = "";
        ch->getLine(data);
        output+= " " + data.substr(0,data.length()-1);
    }
    else if(messageOpcode == 7 || messageOpcode == 8) {
        output += "\n";
        char numOfUsers[2];
        ch->getBytes(numOfUsers,2);
        int times = (int) bytesToShort(numOfUsers);
        for(int i = 0; i < times; ++i) {
            for(int j = 0; j < 6; ++j) {
                char toInsert[2];
                ch->getBytes(toInsert,2);
                short insert = bytesToShort(toInsert);
                output += to_string(insert) + " ";
            }
            output += '\n';
        }
        string str = "";
        ch->getFrameAscii(str, ';');
    }
    else {
        string str = "";
        ch->getFrameAscii(str, ';');
    }
    return output;
}

string ClientEncDec::ErrorRead() {
    string output = "ERROR";
    char opBuffer[2];
    ch->getBytes(opBuffer, 2);
    short msgOpCode = bytesToShort(opBuffer);
    output+= " " + to_string(msgOpCode);
    string str = "";
    ch->getFrameAscii(str, ';');
    return output;
}


void ClientEncDec::encode(string msg) {
    vector<string> inputList;
    boost::split(inputList, msg, boost::is_any_of(" "));
    char* arr = new char[2];

    if (inputList[0] == "REGISTER") {
        shortToBytes(1, arr);
        ch->sendBytes(arr, 2);
        ch->sendLine(inputList[1]);
        ch->sendLine(inputList[2]);
        ch->sendLine(inputList[3]);
        ch->sendFrameAscii("", ';');
    }

    else if (inputList[0] == "LOGIN") {
        shortToBytes(2, arr);
        ch->sendBytes(arr, 2);
        ch->sendLine(inputList[1]);
        ch->sendLine(inputList[2]);
        ch->sendFrameAscii(inputList[3], ';');
    }
    else if (inputList[0] == "LOGOUT") {
        shortToBytes(3, arr);
        ch->sendBytes(arr, 2);
        ch->sendFrameAscii("", ';');
    }
    else if (inputList[0] == "FOLLOW") {
        shortToBytes(4, arr);
        ch->sendBytes(arr, 2);
        ch->sendLine(inputList[1]);
        ch->sendLine(inputList[2]);
        ch->sendFrameAscii("", ';');
    }
    else if (inputList[0] == "POST") {
        shortToBytes(5, arr);
        ch->sendBytes(arr, 2);
        string content = "";
        for(size_t i = 1; i < inputList.size()-1; i++) {
            content += inputList[i] + " ";
        }
        content += inputList[inputList.size()-1];
        ch->sendLine(content);
        ch->sendFrameAscii("", ';');
    }

    else if (inputList[0] == "PM") {
        shortToBytes(6, arr);
        ch->sendBytes(arr, 2);
        ch->sendLine(inputList[1]);
        string content = "";
        for(size_t i = 2; i < inputList.size()-1; i++) {
            content += inputList[i] + " ";
        }
        content += inputList[inputList.size()-1];
        ch->sendLine(content);
        ch->sendFrameAscii("", ';');
    }
    else if (inputList[0] == "LOGSTAT") {
        shortToBytes(7, arr);
        ch->sendBytes(arr, 2);
        ch->sendFrameAscii("", ';');
    }
    else if (inputList[0] == "STAT") {
        shortToBytes(8, arr);
        ch->sendBytes(arr, 2);
        ch->sendLine(inputList[1]);
        ch->sendFrameAscii("", ';');
    }
    else if (inputList[0] == "BLOCK") {
        shortToBytes(12, arr);
        ch->sendBytes(arr, 2);
        ch->sendLine(inputList[1]);
        ch->sendFrameAscii("", ';');
    }
    else
        cout << "THE COMMAND IS NOT KNOWN" << endl;
}

short ClientEncDec::bytesToShort(char* bytesArr) {
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

void ClientEncDec:: shortToBytes(short num, char* bytesArr) {
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}











