/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package totonaca;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author david
 */
public class Server {
    
    final int PORT = 8080;
    
    
    public Server(){
        try{
            ExecutorService executor = Executors.newFixedThreadPool(5);//creating a pool of 5 threads  
            
            ServerSocket ss = new ServerSocket(PORT);
            
            while(true) {
                Socket s = ss.accept();
                s.setTcpNoDelay(true);
                executor.execute(new RequestHandler(s));
            }  
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
}

class RequestHandler implements Runnable{

    Socket s;
    final String WWW_ROOT="/home/david/Escritorio/www";

    public RequestHandler(Socket s) {
        this.s = s;
    }
    
    
    
    @Override
    public void run() {
        try{
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();
            
            String msg = "";
            while(is.available()>0){
                msg += (char)is.read();
            }
            if(!msg.isEmpty()){
                //System.out.println("\n\n\nMensaje recibido: "+msg);
                //System.out.println(msg);
                processHTTP(msg,os);
            }
            is.close();
            os.close();
            s.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    void processHTTP(String msg, OutputStream os) throws IOException{
        PrintWriter pw = new PrintWriter(os);
        
        String[] lines = msg.split("\n");
        
        String http = lines[0];
        
        String[] tokens = http.split(" ");
        
        String method = tokens[0];
        String file = tokens[1];
        tokens = file.split("[.]");
        String ext = tokens[tokens.length-1];
        File f = null;
        long length = 0;
        ArrayList<String[]> vars = new ArrayList<>();
        
        if(method.equals("POST")){
            String v_data = lines[lines.length-1];
            System.out.println("vars: "+v_data);
            for(String var:v_data.split("&")){
                vars.add(var.split("="));
            }
        }
        
        System.out.println("Archivo: "+WWW_ROOT+file);
        
        String status,type;
        byte[] data;
        
        try{
            f = new File(WWW_ROOT+file);
            if(f.exists()&&f.isFile()){
            length = f.length();
            status = "200 OK";
            if(ext.equals("")){
                type = "";
            }
            else if(ext.equals("jpg")){
                type = "image/jpeg";
            }
            else if(ext.equals("png")){
                type = "image/png";
            }
            else if(ext.equals("pdf")){
                type = "application/pdf";
            }
            else{
                type = "text/html";
            }
            }
            else{
                status = "404 Not Found";
                length = "<h1>Archivo no encontrado :c</h1>".length();
                type = "text/html";
            }
        }
        catch(Exception e){
            status = "404 Not Found";
            type = "text/html";
        }
        
        pw.println("HTTP/1.1 "+status);
        pw.println("Server: Totonaca/1.0");
        pw.println("Content-Type:"+type);
        pw.println("Content-Encoding: UTF-8");
        pw.println("Connection: Closed");
        pw.flush();
                
        if(status.equals("200 OK")){
            if(method.equals("DELETE")){
                f.delete();
            }
            if(method.equals("GET")){
                pw.println("Content-Length: "+length+"\n");
                pw.flush();
                FileInputStream fis = new FileInputStream(f);
                while(fis.available()>0){
                    os.write(fis.read());
                }
                fis.close();
            }
            else if(method.equals("POST")){
                FileInputStream fis = new FileInputStream(f);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                String new_doc = "";
                while(br.ready()){
                    String line = br.readLine();
                    int pos = 0;
                    while((pos=line.indexOf("$(",pos))!=-1){
                        int end = line.indexOf(")",pos);
                        System.out.println(pos+" - "+end);
                        String varname = line.substring(pos+2, end);
                        System.out.println("var: "+varname);
                        for(String[] var:vars){
                            System.out.println(var[0]+"="+var[1]);
                            if(var[0].equals(varname)){
                                line = line.replace("$("+varname+")", var[1]);
                            }
                        }
                        pos++;
                    }
                    new_doc+=line+"\n";
                }
                pw.println("Content-Length: "+new_doc.length()+"\n");
                pw.println(new_doc);
                pw.flush();
            }
        }
        else if(status.equals("404 Not Found")){
            msg = "<h1>Archivo no encontrado :c</h1>Totonaca Server 1.0";
            pw.println("Content-Length: "+msg.length()+"\n");
            pw.print(msg);
            pw.flush();
        }
        os.flush();
    }
    
}
