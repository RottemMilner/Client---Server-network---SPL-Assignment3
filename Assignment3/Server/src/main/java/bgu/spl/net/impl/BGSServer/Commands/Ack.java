package bgu.spl.net.impl.BGSServer.Commands;

import bgu.spl.net.impl.BGSServer.BaseCommand;

public class Ack<T extends BaseCommand> extends BaseCommand {

    private short msgOpcode;
    private String info;

    public Ack(short msgOpcode){
        super((short)10);
        this.msgOpcode = msgOpcode;
        this.info = "";
    }

    public Ack(short msgOpcode, String info) {
        super((short)10);
        this.msgOpcode = msgOpcode;
        this.info = info;
    }

    @Override
    public Object[] commandStructure() {
        Object [] structure;
        if(msgOpcode == 7 || msgOpcode==8)
            structure = new Object[]{msgOpcode,info};
        else
            structure = new Object[]{msgOpcode};
        return structure;
    }

    public short getMsgOpcode() {
        return msgOpcode;
    }
}
