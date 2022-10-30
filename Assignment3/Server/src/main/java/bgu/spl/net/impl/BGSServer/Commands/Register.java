package bgu.spl.net.impl.BGSServer.Commands;


import bgu.spl.net.impl.BGSServer.BaseCommand;
import bgu.spl.net.impl.BGSServer.ConnectionsImpl;
import bgu.spl.net.impl.BGSServer.DataBase;
import bgu.spl.net.impl.BGSServer.User;

public class Register extends BaseCommand {

    private  String username;
    private String password;
    private String birthday;

     public Register(String username, String password, String birthday){
         super((short)1);
         this.username = username;
         this.password=password;
         this.birthday=birthday;
     }

     public Register(){
         this("","","");
     }

    @Override
    public Object[] commandStructure() {
        String [] commandStructure =  {username, password, birthday};
        return commandStructure;
    }

    public void act(int connectionID){
         User u = new User(username, password ,birthday);
         if(DataBase.getInstance().register(u)){
             if(!ConnectionsImpl.getInstance().send(connectionID , new Ack((short)1))) {
                 System.out.println("Couldn't send Register acknowledge");
             }
         }
         else{
             if(!ConnectionsImpl.getInstance().send(connectionID, new Error((short)1))){
                 System.out.println("Couldn't send Register Error");
             }
         }
    }

}
