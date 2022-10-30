package bgu.spl.net.impl.BGSServer;
import bgu.spl.net.srv.*;

public class ReactorMain {
        public static void main(String[] args) {
            int port = Integer.parseInt(args[0]); //getting the correct port
            int numOfThreads = Integer.parseInt(args[1]); //getting num of threads
            Server.reactor(numOfThreads, port,
                    () -> new BGSServerProtocol(),
                    () -> new BGSEncoderDecoder())
                    .serve();
        }
}
