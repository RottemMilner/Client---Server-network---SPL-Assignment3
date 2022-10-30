
#ifndef CLIENT_USERINPUTREADER_H
#define CLIENT_USERINPUTREADER_H

#include <iostream>
#include "connectionHandler.h"
#include <boost/asio.hpp>
#include <string>

using boost::asio::ip::tcp;
using namespace std;

class UserInputReader {
private:
    char del[2];
    ConnectionHandler &ch;
    bool terminate;

public:
    UserInputReader(ConnectionHandler &h);

    bool sendLine(string &line);
    void shortToBytes(short num, char *bytesArr);
    void run();




};


#endif //CLIENT_USERINPUTREADER_H
