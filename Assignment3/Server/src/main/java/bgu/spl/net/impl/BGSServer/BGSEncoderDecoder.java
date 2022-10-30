package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.BGSServer.EncDec.*;



public class BGSEncoderDecoder implements MessageEncoderDecoder<BaseCommand> {
    private Encoder enc = new Encoder();
    private Decoder dec = new Decoder();

    @Override
    public BaseCommand decodeNextByte(byte nextByte) {
        return dec.decodeNextByte(nextByte);
    }

    @Override
    public byte[] encode(BaseCommand message) {
        return enc.encode(message);
    }

}
