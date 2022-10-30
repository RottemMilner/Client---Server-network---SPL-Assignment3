package bgu.spl.net.impl.BGSServer.EncDec;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class StrEncodeDecode {

    private byte[] bytes = new byte[1 << 10];
    private int len = 0;

    public String decodeNextByte(byte nextByte) {
        if (nextByte == '$') {
            return popString();
        }

        pushByte(nextByte);
        return null;
    }

    public byte[] encode(String message) {
        return (message + "\0").getBytes(); //uses utf8 by default
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private String popString() {
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }
}
