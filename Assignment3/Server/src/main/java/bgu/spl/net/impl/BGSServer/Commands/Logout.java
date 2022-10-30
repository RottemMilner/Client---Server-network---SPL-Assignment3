package bgu.spl.net.impl.BGSServer.Commands;


import bgu.spl.net.impl.BGSServer.BaseCommand;
import bgu.spl.net.impl.BGSServer.ConnectionsImpl;
import bgu.spl.net.impl.BGSServer.DataBase;


public class Logout extends BaseCommand {

    public Logout() {
        super((short)3);
    }

    @Override
    public Object[] commandStructure() {
        return new Object[0];
    }

    public boolean act(String loggedUsername, int connectionId) {
        if (loggedUsername != null && !loggedUsername.isEmpty() && DataBase.getInstance().getUser(loggedUsername).logout()) {
            if (!ConnectionsImpl.getInstance().send(connectionId, new Ack((short) 3)))
                System.out.println("Could not send Logout acknowledge");
            ConnectionsImpl.getInstance().disconnect(connectionId);
            return true;
        }
        else {
            if (!ConnectionsImpl.getInstance().send(connectionId, new Error((short) 3)))
                System.out.println("Could not send Error");
            return false;
        }
    }
}
