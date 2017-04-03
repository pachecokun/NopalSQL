
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author david
 */
public class Chat {
    
    String name;
    MulticastSocket s;
    ChatListener listener;
    InetAddress group;
    
    FileInputStream fis;
    FileOutputStream fos;
    
    private void sendString(String dest,String type,String str){
        try{
            str = ""+dest+";;;"+name+";;;"+type+";;;"+str;
            System.out.println("Enviando mensaje: "+str);
            byte[] buf = str.getBytes();
            DatagramPacket p = new DatagramPacket(buf, buf.length,group,6666);
            s.send(p);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    private void handshake(String dest){
        sendString(dest,"handshake", "");
    }

    public void sendMessage(String dest,String str){
        sendString(dest,"message", str);
    }
    
    public void sendFile(String dest,String file){
        try{
            fis = new FileInputStream(file);
            sendString(dest, "file", file);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void receiveFile(String sender, String file){
        try {
            FileOutputStream fos = new FileOutputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    private void handleMessage(String msg){
        String[] parts = msg.split(";;;");
        String dest = parts[0];
        String sender = parts[1];
        if(!sender.equals(name)&&(dest.length()==0||dest.equals(name))){
            String type = parts[2];
            String data = parts[3];
            if(type.equals("handshake")){
                listener.clientConnected(sender);
                if(dest.equals("")){
                    handshake(sender);
                }
            }
            else if(type.equals("message")){
                listener.messageReceived(sender, data);
            }
            else if(type.equals("disconnect")){
                listener.clientDisconnected(sender);
            }
            else if(type.equals("file")){
                receiveFile(sender,  data);
            }
            //System.out.println("Mensaje["+type+"] recibido de "+sender+": "+data);
        }
    }
    
    private void disconnect(){
        sendString("", "disconnect", "");
    }
    
    public Chat(String name, ChatListener listener) {
        this.name = name;
        this.listener = listener;
        try{
            group = InetAddress.getByName("228.5.6.7");
            s = new MulticastSocket(6666);
            s.joinGroup(group);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        while(true){
                            byte[] buffer = new byte[500];
                            DatagramPacket p = new DatagramPacket(buffer,buffer.length);
                            s.receive(p);
                            handleMessage(new String(buffer));
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }   
                }
            }).start();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
        public void run() {
            disconnect();
        }
    }, "Shutdown-thread"));
        handshake("");
    }

    
    
    public static void main(String[] args) {
        Chat c = new Chat("trux",new ChatListener() {
            @Override
            public void messageReceived(String sender, String msg) {
                System.out.println(sender+": "+msg);
            }

            @Override
            public void fileReceived(String sender, String file) {
                
            }

            @Override
            public void clientConnected(String name) {
                System.out.println(name+" conectado");
            }

            @Override
            public void clientDisconnected(String name) {
                System.out.println(name+" desconectado");
            }
        });
        c.sendMessage("","hola");
    }
    
}

interface ChatListener{
    void messageReceived(String sender,String msg);
    void fileReceived(String sender,String file);
    void clientConnected(String name);
    void clientDisconnected(String name);
}