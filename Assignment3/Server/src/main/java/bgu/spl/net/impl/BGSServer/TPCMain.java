package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.srv.Server;

public class TPCMain {

    public static void main(String [] args){
        int port = Integer.parseInt(args[0]); //getting the correct port
        Server.threadPerClient(port, () -> new BGSServerProtocol()
                , () -> new BGSEncoderDecoder()).serve();

    }
}
