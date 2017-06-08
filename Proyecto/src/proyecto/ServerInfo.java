/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.io.Serializable;

/**
 *
 * @author david
 */
public class ServerInfo implements Serializable{
    String name;
    String ip;
    int available;

    public ServerInfo(String name, String ip, int available) {
        this.name = name;
        this.ip = ip;
        this.available = available;
    }

    @Override
    public String toString() {
        return "["+available+"]"+name; //To change body of generated methods, choose Tools | Templates.
    }
    
    public String getString(){
        return name+";;;"+ip+";;;"+available;
    }
    
    public static ServerInfo parseServer(String s){
        String[] tokens = s.trim().split(";;;");
        return new ServerInfo(tokens[0], tokens[1], Integer.parseInt(tokens[2]));
    }
    
}
