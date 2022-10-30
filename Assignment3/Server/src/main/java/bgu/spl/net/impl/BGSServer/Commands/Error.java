package bgu.spl.net.impl.BGSServer.Commands;

import bgu.spl.net.impl.BGSServer.BaseCommand;


public class Error extends BaseCommand {

    private short msgOpcode;

    public Error(short msgOpcode){
        super((short)11);
        this.msgOpcode = msgOpcode;
    }

    @Override
    public Object[] commandStructure() {
        Short [] structure = new Short[]{msgOpcode};
        return structure;
    }
}
