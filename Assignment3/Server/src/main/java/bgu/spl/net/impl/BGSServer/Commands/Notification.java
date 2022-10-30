package bgu.spl.net.impl.BGSServer.Commands;


import bgu.spl.net.impl.BGSServer.BaseCommand;


public class Notification extends BaseCommand {

    private byte type;
    private String username;
    private String content;

    public Notification(byte type, String username, String content) {
        super((short) 9);
        this.type = type;
        this.username = username;
        this.content = content;
    }

    @Override
    public Object[] commandStructure() {
        Object [] structure =  new Object[]{type, username, content};
        return structure;
    }
}
