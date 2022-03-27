CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lboost_thread -pthread

all: bin/BGSclient

bin/BGSclient: bin/BGSClient.o bin/ClientEncDec.o bin/ConnectionHandler.o bin/SocketReadTask.o bin/UserInputTask.o
	@echo 'Building target: BGSclient'
	@echo 'Invoking: C++ Linker'
	g++ -Wall -Weffc++ -o bin/BGSclient bin/BGSClient.o bin/ClientEncDec.o bin/ConnectionHandler.o bin/SocketReadTask.o bin/UserInputTask.o  $(LDFLAGS)
	@echo 'Finished building target: BGSclient'

bin/BGSClient.o: src/BGSClient.cpp
	g++ $(CFLAGS) -o bin/BGSClient.o src/BGSClient.cpp

bin/ClientEncDec.o: src/ClientEncDec.cpp
	g++ $(CFLAGS) -o bin/ClientEncDec.o src/ClientEncDec.cpp

bin/ConnectionHandler.o: src/ConnectionHandler.cpp
	g++ $(CFLAGS) -o bin/ConnectionHandler.o src/ConnectionHandler.cpp

bin/SocketReadTask.o: src/SocketReadTask.cpp
	g++ $(CFLAGS) -o bin/SocketReadTask.o src/SocketReadTask.cpp

bin/UserInputTask.o: src/UserInputTask.cpp
	g++ $(CFLAGS) -o bin/UserInputTask.o src/UserInputTask.cpp

clean:
	rm -f bin/*



