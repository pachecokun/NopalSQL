/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author david
 */
public class Server {

    int port = 5555;
    int nplayers = 2;
    String name;
    
    Juego j = new Juego();
    
    ArrayList<ObjectOutputStream> clients = new ArrayList<>();
    
    public void updateServer(){
        try {
            ServerInfo info = new ServerInfo(name, InetAddress.getLocalHost().getHostAddress() , nplayers);
            System.out.println(InetAddress.getLocalHost().getHostAddress());
            byte[] buf = info.getString().getBytes();
            MulticastSocket socket = new MulticastSocket();
            InetAddress group = InetAddress.getByName("224.0.0.0");
            DatagramPacket packet;
            packet = new DatagramPacket(buf, buf.length, group, 4446);
            socket.send(packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public Server() {
        try {
            name = JOptionPane.showInputDialog("Introduzca el nombre del servidor");
            
            ServerSocket ss = new ServerSocket(port);
            while(nplayers>0){
                updateServer();
                System.out.println("Esperando cliente...");
                Socket s = ss.accept();
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                clients.add(oos);
                oos.writeInt(j.addPlayer((String)ois.readObject()));
                oos.flush();
                oos.reset();
                updateClients();
                new Thread(new ClientHandler(ois)).start();
                System.out.println("Cliente conectado...");
                nplayers--;
            }
            System.out.println("Iniciando juego...");
            j.status = 1;
            int dt = 1000/60;
            int dt_enemies = 2*1000;
            new Timer(dt, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    j.update(dt/1000f);
                    updateClients();
                    System.out.println("Enviando juego...");
                }
            }).start();
            new Timer(dt_enemies, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    j.createEnemy();
                }
            }).start();
            
            while(true);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void updateClients(){
        for(ObjectOutputStream oos:clients){
            try {
                oos.writeObject(j);
                oos.flush();
                oos.reset();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    class ClientHandler implements Runnable{

        ObjectInputStream ois;

        public ClientHandler(ObjectInputStream ois) {
            this.ois = ois;
        }
        
        @Override
        public void run() {
            while(true){
                try {
                    Message m = (Message)ois.readObject();
                    Player p = j.players.get(m.id-1);
                    if(!p.isCrashed()){
                        p.setVy(100*m.my);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
            }
        }
        
    }
    public static void main(String[] args) {
        new Server();
    }
}
