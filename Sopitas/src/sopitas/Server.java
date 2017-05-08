/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sopitas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author david
 */
class Server{
    String address;
    int port;
    int available;

    public static void main(String[] args) {
        new Server("localhost", 8181, 5);
    }
    
    public Server(String address, int port, int available) {
        try{
            this.address = address;
            this.port = port;
            this.available = available;
            ExecutorService executor = Executors.newFixedThreadPool(5);//creating a pool of 5 threads  
            
            ServerSocket ss = new ServerSocket(port);
            
            while(available>0) {
                Socket s = ss.accept();
                s.setTcpNoDelay(true);
                executor.execute(new PlayerHandler(s,available==5));
                available--;
            }  
        }catch(Exception e){
            e.printStackTrace();
        }
    }    

    public void setAvailable(int available) {
        this.available = available;
    }
    
    public static Server parseServer(String s){
        String[] data = s.split(";;;");
                        
        String ip = data[0];
        int port = Integer.parseInt(data[1]);
        int available = Integer.parseInt(data[2]);
        
        return new Server(ip, port, available);
    }

    @Override
    public String toString() {
        return address+":"+port+"("+available+" lugares)";
    }
    
    public String sendString(){
        return address+";;;"+port+";;;"+available;
    }
}
class PlayerHandler implements Runnable{

    Socket s;
    boolean first;
    PrintWriter pw;
    BufferedReader br;
    Sopa game = null;   

    public PlayerHandler(Socket s,boolean first) {
        this.s = s;
        this.first = first;
    }    
    
    @Override
    public void run() {
        try {
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            pw = new PrintWriter(s.getOutputStream());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    void sendGame(){
        
    }

}