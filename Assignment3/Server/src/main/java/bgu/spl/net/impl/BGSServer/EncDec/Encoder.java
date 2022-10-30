package bgu.spl.net.impl.BGSServer.EncDec;

import bgu.spl.net.impl.BGSServer.BaseCommand;
import bgu.spl.net.impl.BGSServer.Commands.Ack;
import bgu.spl.net.impl.BGSServer.Commands.Logstat;

import java.util.LinkedList;

public class Encoder {
    private Object[] Struct;
    private OpcodeEncodeDecode opencdec = new OpcodeEncodeDecode();
    private CharEncodeDecode charencdec =new CharEncodeDecode();
    private StrEncodeDecode strencdec = new StrEncodeDecode();

    public byte[] encode(BaseCommand cmd) {
        Struct = cmd.commandStructure();
        LinkedList<Byte> Encoded = new LinkedList<>();
        OpcodeEncodeDecode op = new OpcodeEncodeDecode();
        byte[] encodedop = op.encode(cmd.getOpcode());
        for (byte opbyte : encodedop) 
            Encoded.add(opbyte);
        for (Object o : Struct) {
            if (o.getClass().equals(Byte.class))
                Encoded.add((byte) o);
            if(o.getClass().equals(Short.class)){
                byte[] encshort = opencdec.encode((Short) o);
                addBytes(Encoded , encshort);
            }
            if(o.getClass().equals(String.class)){
                byte[] encstr = strencdec.encode((String) o);
                addBytes(Encoded , encstr);
            }
            if(o.getClass().equals(String[].class)){
                for(String s:(String[]) o){
                    byte[] encstr = strencdec.encode(s);
                    addBytes(Encoded , encstr);
                }
            }
            if(o.getClass().equals(Character.class)){
                byte[] encchar = charencdec.encode((Character) o);
                addBytes(Encoded ,encchar);
            }
        }
        return toBytes(Encoded);
    }

    private void addBytes(LinkedList<Byte> target ,byte[] donor){
        for(byte b : donor)
            target.add(b);
    }

    private byte[] toBytes(LinkedList<Byte> Encoded){
        byte[] bytes = new byte[Encoded.size()];
        for (int i = 0; i < Encoded.size(); i++) {
            bytes[i] = Encoded.get(i);
        }
        return bytes;
    }


}

