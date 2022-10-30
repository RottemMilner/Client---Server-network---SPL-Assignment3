package bgu.spl.net.impl.BGSServer;


public abstract class BaseCommand {

    protected short opcode;

    public BaseCommand(short opcode){
        this.opcode = opcode;
    }

    public short getOpcode() {
        return opcode;
    }

    public abstract Object[] commandStructure();

}
