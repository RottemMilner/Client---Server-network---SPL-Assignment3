package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Commands.*;

import java.util.LinkedList;

public class BGSServerProtocol implements BidiMessagingProtocol<BaseCommand> {

    private String loggedUsername;
    private boolean shouldTerminate=false;
    private int connectionId;
    LinkedList<String> filteredWords;

    @Override
    public void start(int connectionId, Connections<BaseCommand> connections) {
        this.connectionId = connectionId;
        this.loggedUsername = "";
        filteredWords = new LinkedList<>();
        filteredWords.add("Trump");
    }

    @Override
    public void process(BaseCommand msg) {
        if(msg!=null){
            switch (msg.getOpcode()){
                case 1:
                    ((Register)msg).act(connectionId);
                    break;
                case 2:
                    if(((Login)msg).act(loggedUsername, connectionId))
                        loggedUsername = ((Login)msg).getUsername();
                    break;
                case 3:
                    if(((Logout)msg).act(loggedUsername, connectionId)){
                        loggedUsername= "";
                        shouldTerminate = true;
                    }
                    break;
                case 4:
                    ((FollowUnfollow)msg).act(DataBase.getInstance().getUser(loggedUsername), connectionId);
                    break;
                case 5:
                    ((Post)msg).act(DataBase.getInstance().getUser(loggedUsername), connectionId);
                    break;
                case 6:
                    ((PM)msg).act(DataBase.getInstance().getUser(loggedUsername), connectionId, filteredWords);
                    break;
                case 7:
                    ((Logstat)msg).act(DataBase.getInstance().getUser(loggedUsername), connectionId);
                    break;
                case 8:
                    ((Stats)msg).act(DataBase.getInstance().getUser(loggedUsername), connectionId);
                    break;
                case 12:
                    ((Block)msg).act(DataBase.getInstance().getUser(loggedUsername), connectionId);
                    break;
            }
        }
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
