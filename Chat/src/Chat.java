
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    int npart = 0;
    final int PART_SIZE = 200;
    String file_sender = "";
    String file_name = "";
    
    private void sendString(String dest,String type,String str){
        try{
            str = ""+dest+";;;"+name+";;;"+type+";;;"+str;
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
            System.out.println("ENviando "+new File(file).getName());
            fis = new FileInputStream(file);
            sendString(dest, "file", new File(file).getName());
            while(true){
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[PART_SIZE];
                int read = fis.read(buffer, PART_SIZE*npart,Math.min(fis.available(),PART_SIZE));
                bos.write((byte)npart);//numero parte
                bos.write((byte)read);//tamaño
                bos.write((byte)(fis.available()==0?1:0));//final?
                bos.write(buffer, 0, read);
                sendString(dest, "file_part", new String(bos.toByteArray()));
                if(fis.available()==0){
                    break;
                }
            }
            fis.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void sendFilePart(String dest,int npart){
        try{
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void receiveFile(String sender, String file){
        try {
            fos = new FileOutputStream(file);
            file_sender = sender;
            file_name = file;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void receiveFilePart(String data){
        try{
            byte[] bytes = data.getBytes();
            int npart = bytes[0]& (0xff);
            int size = bytes[1]& (0xff);
            int is_final = bytes[4]& (0xff);
            System.out.println(npart+": "+size+" bytes"+" final: "+is_final);
            fos.write(bytes, 5, size);
            if(is_final==1){
                fos.close();
                listener.fileReceived(file_sender,file_name);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
        
    private void handleMessage(String msg){
        //System.out.println("Mensaje recibido: "+msg);
        String[] parts = msg.split(";;;",4);
        String dest = parts[0];
        String sender = parts[1];
        if(dest.length()==0||dest.equals(name)||sender.equals(name)){
            String type = parts[2];
            String data = parts[3];
            //System.out.println("Mensaje["+type+"] recibido de "+sender+": "+data);
            if(type.equals("handshake") && !sender.equals(name)){
                boolean resp = dest.length()!=0;
                listener.clientConnected(resp, sender);
                if(dest.equals("")){
                    handshake(sender);
                }
            }
            else if(type.equals("message")){
                listener.messageReceived(dest, sender, data);
            }
            else if(type.equals("disconnect")){
                listener.clientDisconnected(sender);
            }
            else if(type.equals("file") && !sender.equals(name)){
                receiveFile(sender,  data.trim());
            }
            else if(type.equals("file_part") && !sender.equals(name)){
                receiveFilePart(data);
            }
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

    
    /*
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
    */
}

interface ChatListener{
    void messageReceived(String dest, String sender,String msg);
    void fileReceived(String sender,String file);
    void clientConnected(boolean response, String name);
    void clientDisconnected(String name);
}
