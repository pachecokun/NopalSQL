/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package totonaca;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author david
 */
public class Server {
    
    final int PORT = 8080;
    final String WWW_ROOT="/home/david/Escritorio/www";
    
    
    public Server(){
        try{
            Selector selector = Selector.open();
            ServerSocketChannel ssChannel = ServerSocketChannel.open();
            ssChannel.configureBlocking(false);
            ssChannel.socket().bind(new InetSocketAddress(PORT));
            ssChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                if (selector.select() <= 0) {
                    continue;
                }
                processShit(selector.selectedKeys());
              }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void processShit(Set readySet) throws Exception {
        Iterator iterator = readySet.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = (SelectionKey) iterator.next();
            iterator.remove();
            if (key.isAcceptable()) {
                ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
                SocketChannel sChannel = (SocketChannel) ssChannel.accept();
                sChannel.configureBlocking(false);
                sChannel.register(key.selector(), SelectionKey.OP_READ);
            }
            if (key.isReadable()) {
                String msg = readShit(key);
                if(msg==null)return;
                //System.out.println("\n\n\nMensaje recibido: ");
                
                //System.out.println(msg.trim());

                //System.out.println("\n\n\nEnviando respuesta...");
                SocketChannel sChannel = (SocketChannel) key.channel();
                String response = processHTTP(msg);
                //System.out.println(response);
                ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
                try{
                    sChannel.write(buffer);
                    sChannel.close();
                    //System.out.println("Respuesta enviada");
                }catch(Exception e){
                    //System.out.println("Cliente desconectado");
                    key.cancel();
                }
            }
        }
    }
    
    public String readShit(SelectionKey key) throws Exception {
        SocketChannel sChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesCount = sChannel.read(buffer);
        if (bytesCount > 0) {
            buffer.flip();
            return new String(buffer.array());
        }
        return null;
    }
    
    String processHTTP(String msg) throws IOException{
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bo);
        
        String[] lines = msg.split("\n");
        
        String http = lines[0];
        
        String[] tokens = http.split(" ");
        
        String method = tokens[0];
        String file = tokens[1];
        System.out.println("Archivo: "+WWW_ROOT+file);
        
        String status,data;
        
        try{
            FileInputStream fis = new FileInputStream(WWW_ROOT+file);
            data = "";
            while(fis.available()>0){
                data += (char)fis.read();
            }
            fis.close();
            status = "200 OK";
        }
        catch(Exception e){
            status = "404 Not Found";
            data = "<h1>Archivo no encontrado :c</h1>";
            data += "Totonaca server 1.0";
        }
        
        String response = "";
        
        response += "HTTP/1.1 "+status+"\n";
        response += "Server: Totonaca/1.0\n";
        response += "Content-Length: "+data.length()+"\n";
        response += "Content-Type: text/html\n";
        response += "Connection: Closed\n\n";
        response += data;
        
        return response;
    }
}
