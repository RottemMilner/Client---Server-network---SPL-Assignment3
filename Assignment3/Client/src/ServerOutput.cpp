
#include "../include/ServerOutput.h"

ServerOutput::ServerOutput(ConnectionHandler &h) : h(h) , terminate(false){
}

void ServerOutput::run() {
    while (!terminate) {
        std::string answer;
        if (!getLine(answer)) {
            std::cout << "Disconnected. Exiting..\n" << std::endl;
            break;
        }
        std::cout << answer << std::endl;
    }
}

ServerOutput::~ServerOutput() {}

///bytestoshorts as supplied in the assignment page
short ServerOutput::bytesToShort(char *bytesArr) {
    short result = (short) ((bytesArr[0] & 0xff) << 8);
    result += (short) (bytesArr[1] & 0xff);
    return result;
}

bool ServerOutput::getLine(std::string &line) {
    char ch;
    int index = 0;
    char op[2];
    short opcode = -1;
    ///try to read from the socket
    try {
        while (index < 2) {
            ///reading the 2 first bytes
            h.getBytes(&ch, 1);
            if (index < 2)
                op[index] = ch;
            if (index == 1)
                opcode = bytesToShort(op);
            index++;
        }
        switch (opcode) {
            ///opcode must be one of these 3
            case 9:
                Notification(line);
                break;
            case 10:
                Ack(line);
                break;
            case 11:
                Error(line);
                break;
            case -1:
                std::cout << "corrupted input" << std::endl;
                break;
        }
    }
    catch (std::exception &readsocketex) {
        std::cerr << "recv failed (Error: " << readsocketex.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ServerOutput::Notification(std::string &line) {
    line = "NOTIFICATION ";
    char current;
    h.getBytes(&current, 1);
    if (current == '\0')
        line += "PM ";
    else if (current == '\1')
        line += "Public ";
    else
        return false;
    h.getBytes(&current, 1);
    while (current != '\0') {
        line += current;
        h.getBytes(&current, 1);
    }
    line += ' ';
    h.getBytes(&current, 1);
    while (current != '\0') {
        line += current;
        h.getBytes(&current, 1);
    }
    return true;
}

bool ServerOutput::Error(std::string &line) {
    line += "Error ";
    getOPCode(line);
    return false;
}

bool ServerOutput::Ack(std::string &line) {
    line += "Ack ";
    short opcode = getOPCode(line);
    switch (opcode) {
        case 2:///successful login
            h.setActive(true);
            break;
        case 3:///logout and shut the client
            terminate = true;
            h.setActive(false);
            h.close();
            break;
        case 7:
            Log(line);
            break;
        case 8:
            Stat(line);
            break;
        default:///else dont respond
            break;
    }
    return false;
}



std::string ServerOutput::Log(std::string &line) {
    char ch;
    h.getBytes(&ch, 1);
    while (ch != '#') {
        if(ch == '$')
            line += '\n' ;
        else
            line += ch;
        h.getBytes(&ch, 1);
    }
    h.getBytes(&ch, 1);
    return line;
}

std::string ServerOutput::Stat(std::string &line) {
    char ch;
    h.getBytes(&ch, 1);
    while (ch != '#') {
        if(ch == '$')
            line += '\n' ;
        else
            line += ch;
        h.getBytes(&ch, 1);
    }
    h.getBytes(&ch, 1);
    return line;
}

short ServerOutput::getOPCode(std::string &line) {
    char opbytes[2];
    h.getBytes(opbytes , 2);
    short op = bytesToShort(opbytes);
    line.operator+=(std::to_string(op));
    return op;
}