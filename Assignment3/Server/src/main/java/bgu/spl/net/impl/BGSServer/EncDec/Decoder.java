package bgu.spl.net.impl.BGSServer.EncDec;

import bgu.spl.net.impl.BGSServer.BaseCommand;
import bgu.spl.net.impl.BGSServer.Commands.*;

public class Decoder {
    private Object[] Struct = null;
    private OpcodeEncodeDecode opencdec = new OpcodeEncodeDecode();
    private CharEncodeDecode charencdec = new CharEncodeDecode();
    private StrEncodeDecode strencdec = new StrEncodeDecode();
    private int index = 0;
    private BaseCommand cmd;
    private int length = 0;


    public BaseCommand decodeNextByte(byte nextByte){
        if(length <2){
            Short opcode = opencdec.decodeNextByte(nextByte);
            if (opcode != null){
                setCommand(opcode);
                opencdec = new OpcodeEncodeDecode();
            }
        }
        if (Struct != null && length > 1 && index < Struct.length){
            Object o = Struct[index];
            if(o.getClass().equals(Byte.class)){
                Struct[index] = nextByte;
                index++;
            }
            else if(o.getClass().equals(Character.class)){
                Character ch = charencdec.decodeNextByte(nextByte);
                if(ch != null){
                    Struct[index] = ch;
                    index++;
                    charencdec = new CharEncodeDecode();
                }
            }
            else if(o.getClass().equals(Short.class)){
                Short s = opencdec.decodeNextByte(nextByte);
                if(s != null){
                    Struct[index] = s;
                    index++;
                    opencdec = new OpcodeEncodeDecode();
                }
            }
            else if(cmd.getClass().equals(FollowUnfollow.class) && index==0){
                if(nextByte=='\0')
                    Struct[index] = "0";
                else
                    Struct[index] = "1";
                index++;
                strencdec = new StrEncodeDecode();
            }
            else if(o.getClass().equals(String.class)){
                String str = strencdec.decodeNextByte(nextByte);
                if(str != null){
                    Struct[index] = str;
                    index++;
                    strencdec = new StrEncodeDecode();
                }
            }
        }
        if(Struct != null && index == Struct.length){
            BaseCommand cmd = CreateCommand();
            clear();
            return cmd;
        }
        length++;
        return null;
    }

    private void clear() {
        length = 0;
        index = 0;
        Struct = null;
    }

    private void setCommand(short opcode){
        switch (opcode){
            case 1:
                cmd = new Register();
                break;
            case 2:
                cmd = new Login();
                break;
            case 3:
                cmd = new Logout();
                break;
            case 4:
                cmd = new FollowUnfollow();
                break;
            case 5:
                cmd = new Post();
                break;
            case 6:
                cmd = new PM();
                break;
            case 7:
                cmd = new Logstat();
                break;
            case 8:
                cmd = new Stats();
                break;
            case 12:
                cmd = new Block();
        }
        Struct = cmd.commandStructure();
    }

    private BaseCommand CreateCommand(){
        try {
        Class[] c = new Class[Struct.length];
        for (int i = 0; i < Struct.length; i++)
            c[i] = Struct[i].getClass();
        return cmd.getClass().getConstructor(c).newInstance(Struct);}
        catch (Exception ignored){
            System.out.println("failed to decode command!");
        };
        return null;
    }


}
