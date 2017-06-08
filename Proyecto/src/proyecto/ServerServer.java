/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author david
 */
public class ServerServer {

    volatile ArrayList<ServerInfo> servers = new ArrayList<>();
    volatile ArrayList<ObjectOutputStream> clients = new ArrayList<>();
    
    public void update(ObjectOutputStream oos) throws Exception{
            oos.writeObject(servers);
            oos.flush();
            oos.reset();
    }
    public void updateAll(){
        for(ObjectOutputStream oos:clients){
            try {
                update(oos);
            } catch (Exception ex) {
                ex.printStackTrace();
                clients.remove(oos);
            }
        }
    }
    
    public ServerServer() {
        try {
            
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ServerSocket ss = new ServerSocket(5556);
                        
                        while(true){
                            Socket s = ss.accept();
                            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                            try {
                                update(oos);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            clients.add(oos);
                        }
                        
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    
                }
            }).start();
            
            MulticastSocket socket = new MulticastSocket(4446);
            InetAddress group = InetAddress.getByName("224.0.0.0");
            socket.joinGroup(group);
            
            DatagramPacket packet;
            for (int i = 0; i < 5; i++) {
                byte[] buf = new byte[256];
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                
                String received = new String(packet.getData());
                ServerInfo s = ServerInfo.parseServer(received);
                
                boolean found = false;
                
                for(ServerInfo server:servers){
                    if(s.ip.equals(server.ip)){
                        if(server.available == 0){
                            servers.remove(s);
                            break;
                        }
                        server.available = s.available;
                        found = true;
                        break;
                    }
                }
                if(!found){
                    servers.add(s);
                }
                System.out.println("Servidor recibido: "+s);
                updateAll();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    public static void main(String[] args) {
        new ServerServer();
    }
}
