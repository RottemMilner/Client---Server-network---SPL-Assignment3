HOW TO RUN THE CODE-
1) (mvn clean,mvn compile)
2)
REACTOR SERVER-
mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.ReactorMain" -Dexec.args="<PORT> <NUMOFTHREADS>"
TPC SERVER-
mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.TPCMain" -Dexec.args="<PORT>"
3)
CLIENT-
./bin/BGSclient 127.0.0.1 7777
--------------------------------------------------------------
Messages example-
1)REGISTER <USERNAME> <PASSWORD> <DD-MM-YYYY>
	REGISTER Dave 123a 01-01-1990
2)LOGIN <USERNAME> <PASSWORD> <CAPTCHA>
	LOGIN Dave 123a 1
3)LOGOUT
	LOGOUT
4)FOLLOW <0/1> <USERNAME>
	FOLLOW 0 DAVE
5)POST <CONTENT>
	POST Hello world
6)PM <USERNAME> <CONTENT>
	PM Dave Hey dave
7)LOGSTAT 
	LOGSTAT
8)STAT <User1>|<User2>|<User3>...
	STAT Dave|Bob|Alice
12)BLOCK <USER>
	BLOCK Dave
----------------------------------------------------------------
Filtered words are stored in LinkedList<String> located at- "Server/src/main/java/bgu/spl/net/impl/BGSServer/BGSServerProtocol.java"
-----------------------------------------------------------------
