
#ifndef CLIENT_SERVEROUTPUT_H
#define CLIENT_SERVEROUTPUT_H

#include <iostream>
#include <boost/asio.hpp>
#include <string>
#include "connectionHandler.h"

using boost::asio::ip::tcp;

class ServerOutput {
private:
    ConnectionHandler &h;
    bool terminate;
public:
    ServerOutput(ConnectionHandler &);

    void run();

    bool getLine(std::string &input);

    bool Ack(std::string &line);

    std::string Log(std::string &line);

    std::string  Stat(std::string &line);

    bool Notification(std::string &line);

    bool Error(std::string &line);

    short bytesToShort(char *bytesArr);

    short getOPCode(std::string &line);

    virtual ~ServerOutput();


};


#endif //CLIENT_SERVEROUTPUT_H
