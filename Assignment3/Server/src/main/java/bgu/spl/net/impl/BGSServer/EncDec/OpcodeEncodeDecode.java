package bgu.spl.net.impl.BGSServer.EncDec;

import java.util.Arrays;

public class OpcodeEncodeDecode {
    private int len = 0;
    private byte[] bytesArr = new byte[2];

    public byte[] encode(Short num) {///as supplied in the assignment page
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte) ((num >> 8) & 0xFF);
        bytesArr[1] = (byte) (num & 0xFF);
        return bytesArr;
    }


    public Short decodeNextByte(byte nextByte) {
        pushByte(nextByte);
        if (len == 2) {
            len = 0;
            return bytesToShort(bytesArr);
        }
        return null;
    }

    private short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytesArr.length) {
            bytesArr = Arrays.copyOf(bytesArr,len * 2);
        }

        bytesArr[len++] = nextByte;
    }
}
