package bgu.spl.net.impl.BGSServer.EncDec;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CharEncodeDecode {

    private int len = 0;
    private byte[] bytesArr = new byte[2];

    public byte[] encode(Character ch) {
        char[] tostring = {ch};
        String bytes = new String(tostring);
        return bytes.getBytes(StandardCharsets.UTF_8);
    }


    public Character decodeNextByte(byte nextByte) {
        pushByte(nextByte);
        if (len == 2) {
            len = 0;
            return bytesToChar(bytesArr);
        }
        return null;
    }

    private char bytesToChar(byte[] byteArr) {
        return (char) byteArr[0];
    }

    private void pushByte(byte nextByte) {
        if (len >= bytesArr.length) {
            bytesArr = Arrays.copyOf(bytesArr, len * 2);
        }
        bytesArr[len++] = nextByte;
    }

}
