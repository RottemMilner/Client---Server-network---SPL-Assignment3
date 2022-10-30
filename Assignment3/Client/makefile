CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lpthread -lm


all: BGSclient
	g++ -o bin/BGSclient bin/ServerOutput.o bin/connectionHandler.o bin/UserInputReader.o bin/Client.o $(LDFLAGS)

BGSclient:  bin/ServerOutput.o bin/connectionHandler.o bin/UserInputReader.o bin/Client.o

bin/Client.o: src/Client.cpp
	g++ $(CFLAGS) -o bin/Client.o src/Client.cpp

bin/ServerOutput.o: src/ServerOutput.cpp
	g++ $(CFLAGS) -o bin/ServerOutput.o src/ServerOutput.cpp

bin/UserInputReader.o: src/UserInputReader.cpp
	g++ $(CFLAGS) -o bin/UserInputReader.o src/UserInputReader.cpp

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

.PHONY: clean
clean:
	rm -f bin/*
