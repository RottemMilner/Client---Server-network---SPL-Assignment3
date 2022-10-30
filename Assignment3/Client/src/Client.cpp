#include <iostream>
#include "../include/connectionHandler.h"
#include "../include/ServerOutput.h"
#include "../include/UserInputReader.h"
#include <mutex>
#include <thread>

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
    
    ConnectionHandler ch(host, port);
    if (!ch.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    UserInputReader userInputReader(ch);
    ServerOutput serverOutput(ch);
    ///creating 2 threads, one for user input and one for server input.
    std::thread thread1(&UserInputReader::run, &userInputReader);
    std::thread thread2(&ServerOutput::run, &serverOutput);

    thread1.join();
    thread2.join();
    return 0;
}
