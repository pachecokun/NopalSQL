/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sopitas;

import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author david
 */
public class ServerFinder {
    
    volatile ArrayList<Server> servers = new ArrayList<>();
    
    public ServerFinder(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    MulticastSocket socket = new MulticastSocket(5050);
                    InetAddress group = InetAddress.getByName("225.225.225.225");
                    socket.joinGroup(group);

                    DatagramPacket packet;
                    for (;;) {
                        byte[] buf = new byte[256];
                        packet = new DatagramPacket(buf, buf.length);
                        socket.receive(packet);

                        String received = new String(packet.getData());
                        
                        updateList(Server.parseServer(received));
                        
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ServerSocket ss = new ServerSocket(5050);
                    while(true){
                        Socket sock = ss.accept();
                        sock.setSoLinger(true, 10);
                        PrintWriter pw = new PrintWriter(sock.getOutputStream());
                        for(Server s:servers){
                            pw.println(s.sendString());
                            pw.flush();
                        }
                        pw.close();
                        sock.close();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        
        updateList(new Server("127.0.0.1", 8181, 5));
    }
    
    public void updateList(Server sr){
        for(Server s:servers){
            if(s.address.equals(sr.address)&&sr.port==s.port){
                if(sr.available==-1){
                    servers.remove(s);
                }
                else{
                    s.setAvailable(sr.available);
                }
                return;
            }
        }
        servers.add(sr);
        System.out.println("added: "+sr);
    }
    
    public static void main(String[] args) {
        new ServerFinder();
    }
}