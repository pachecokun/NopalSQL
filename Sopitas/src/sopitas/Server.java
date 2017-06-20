/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sopitas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.Timer;

/**
 *
 * @author david
 */
class Server{
    String address;
    int port;
    int available;
    
    volatile Sopa game = null;
    volatile ArrayList<ObjectOutputStream> oos = new ArrayList<>();
    
    ExecutorService executor = Executors.newFixedThreadPool(5);
    

    public static void main(String[] args) {
        new Server("localhost", 8181, 5);
    }
    
    public Server(String address, int port, int available) {
        try{
            this.address = address;
            this.port = port;
            this.available = available;
            
            ServerSocket ss = new ServerSocket(port);
            
            while(available>0) {
                Socket s = ss.accept();
                System.out.println("Cliente conectado");
                addClient(s);
                while(game==null);
                available--;
            }  
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
    
    void addClient(Socket s){
        try{
            executor.execute(new PlayerHandler(s));
            ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
            os.writeBoolean(game==null);
            os.flush();
            System.out.println("Juego nuevo? "+(game==null));
            oos.add(os);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    void sendGame(){
        try{
            for(ObjectOutputStream os:oos){
                os.writeObject(game);
                os.flush();
                os.reset();
            }
            System.out.println("Juego actualizado "+game.time);
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
    class PlayerHandler implements Runnable{

        Socket s;
        ObjectOutputStream oos;
        BufferedReader br;

        public PlayerHandler(Socket s) {
            this.s = s;
        }    

        @Override
        public void run() {
            try {
                br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String msg;
                
                while(true){
                    msg = br.readLine();
                    
                    System.out.println("Mensaje recibido: "+msg);
                    
                    String[] tokens = msg.split(" ");
                    
                    if(tokens[0].equals("level")){
                        game = Sopa.read("../sopas/"+tokens[1]+".txt");
                        new Timer(1000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(!game.end){
                                    game.advanceTime();
                                    sendGame();
                                }
                            }
                        }).start();
                    }
                    else if(tokens[0].equals("discover")){
                        game.discoverWord(
                                tokens[1],
                                Integer.parseInt(tokens[2]),
                                Integer.parseInt(tokens[3]),
                                Integer.parseInt(tokens[4]),
                                Integer.parseInt(tokens[5])
                        );
                        sendGame();
                    }
                    else if(tokens[0].equals("player")){
                        game.addPlayer(tokens[1]);
                        sendGame();
                    }
                }

                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}