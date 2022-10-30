
#include "../include/UserInputReader.h"
#include <boost/algorithm/string.hpp>

using namespace std;

UserInputReader::UserInputReader(ConnectionHandler &h) : ch(h) , terminate(false){
}

void UserInputReader::run() {
    while (!terminate) {
        string line;
        getline(cin, line);
        sendLine(line);
    }
}

bool UserInputReader::sendLine(string &line){
    char space[] = "$";
    std::vector<std::string> input;
    boost::split(input, line, [](char c) { return c == ' '; });
    if (!input.empty()) {
        if (input[0] == "REGISTER"){
            bool sent = false;
            char opcode[2];
            shortToBytes(1, opcode);
            if (input.size() == 4) {
                sent =  ch.sendBytes(opcode, 2) &&
                        ch.sendBytes(input[1].c_str(), input[1].length()) &&
                        ch.sendBytes(space, 1) &&
                        ch.sendBytes(input[2].c_str(), input[2].length()) &&
                        ch.sendBytes(space, 1) &&
                        ch.sendBytes(input[3].c_str(), input[3].length()) &&
                        ch.sendBytes(space, 1);
            }
            else
                cout << "input error" << endl;
            input.clear();
            return sent;
        }
        if (input[0] == "LOGIN"){
            bool sent = false;
            char opcode[2];
            shortToBytes(2, opcode);
            if (input.size() == 4) {
                sent =  ch.sendBytes(opcode, 2) &&
                        ch.sendBytes(input[1].c_str(), input[1].length()) &&
                        ch.sendBytes(space, 1) &&
                        ch.sendBytes(input[2].c_str(), input[2].length()) &&
                        ch.sendBytes(space, 1) &&
                        ch.sendBytes(input[3].c_str(), input[3].length()) &&
                        ch.sendBytes(space, 1);

            }
            else
                cout << "input error" << endl;
            input.clear();
            return sent;
        }
        if (input[0] == "LOGOUT") {
            bool sent = false;
            char opcode[2];
            shortToBytes(3, opcode);
            if (input.size() == 1) {
                sent = ch.sendBytes(opcode, 2);
            } else
                cout << "input error" << endl;
            input.clear();
            if (sent) {
                if (ch.isActive())
                    terminate = true;
                return true;
            }
            return false;
        }
        if (input[0] == "FOLLOW"){
            bool sent = false;
            char opcode[2];
            shortToBytes(4, opcode);
            if (input.size() > 2) {
                sent = ch.sendBytes(opcode, 2);
                if (input[1] == "0")
                    sent = sent && ch.sendBytes("\0", 1);
                else
                    sent = sent && ch.sendBytes("\1", 1);
                sent = sent && ch.sendBytes(input[2].c_str(), input[2].length()) &&
                        ch.sendBytes(space, 1);
            }
            else
                cout << "input error" << endl;
            input.clear();
            return sent;
        }
        if (input[0] == "POST"){
            bool sent = false;
            char opcode[2];
            shortToBytes(5, opcode);
            if (input.size() > 1) {
                sent = ch.sendBytes(opcode, 2);
                for (unsigned int i = 1; i < input.size() - 1; i++) {
                    sent = sent && ch.sendBytes((input[i] + ' ').c_str(), input[i].length() + 1);
                }
                sent = sent && ch.sendBytes(input[input.size() - 1].c_str(), input[input.size() - 1].length()) &&
                        ch.sendBytes(space, 1);
            }
            else
                cout << "input error" << endl;
            input.clear();
            return sent;
        }
        if (input[0] == "PM"){
            bool sent = false;
            char opcode[2];
            shortToBytes(6, opcode);
            if (input.size() > 1) {
                sent =  ch.sendBytes(opcode, 2) &&
                        ch.sendBytes((input[1]).c_str(), input[1].length()) &&
                        ch.sendBytes(space, 1);
                for (unsigned int i = 2; i < input.size() - 1; i++) {
                    sent = sent && ch.sendBytes((input[i] + ' ').c_str(), input[i].length() + 1);
                }
                sent = sent && ch.sendBytes(input[input.size() - 1].c_str(), input[input.size() - 1].length()) &&
                        ch.sendBytes(space, 1);
            }
            else
                cout << "input error" << endl;
            input.clear();
            return sent;
        }
        if (input[0] == "LOGSTAT"){
            bool sent = false;
            char opcode[2];
            shortToBytes(7, opcode);
            if (input.size() == 1) {
                sent = ch.sendBytes(opcode, 2);
            }
            else
                cout << "input error" << endl;
            input.clear();
            return sent;
        }
        if (input[0] == "STAT"){
            bool sent = false;
            char opcode[2];
            shortToBytes(8, opcode);
            if (input.size() > 1) {
                    sent =  ch.sendBytes(opcode, 2);
                    std::string names = input[1];
                    boost::split(input, names, [](char c) { return c == '|'; });
                    for (unsigned int i = 0; i < input.size() - 1; i++) {
                        sent = sent && ch.sendBytes((input[i] + '&').c_str(), input[i].length() + 1);
                    }
                    sent = sent && ch.sendBytes(input[input.size() - 1].c_str() , input[input.size() - 1].length()) &&
                           ch.sendBytes(space, 1);
                }
            else
                cout << "input error" << endl;
            input.clear();
            return sent;
        }
        if(input[0] == "BLOCK"){
            bool sent = false;
            char opcode[2];
            shortToBytes(12, opcode);
            if (input.size() > 1) {
                sent =  ch.sendBytes(opcode, 2) &&
                        ch.sendBytes(input[1].c_str(), input[1].length()) &&
                        ch.sendBytes(space, 1);
            }
            else
                cout << "input error" << endl;
            input.clear();
            return sent;
        }
    }
    return true;
}

void UserInputReader::shortToBytes(short num, char *bytesArr) {
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}