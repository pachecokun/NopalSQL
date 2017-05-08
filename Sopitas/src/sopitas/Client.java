/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sopitas;

import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author david
 */
public class Client {
    
    Socket s;
    ObjectInputStream ois;
    PrintWriter pw;
    String player;
    Sopa game;
    
    public static void main(String[] args) {
        new Client("localhost", 8181);
    }
    
    public Client(String ip,int port){
        try {
            player = JOptionPane.showInputDialog("Introduzca su nombre: ");
            
            s = new Socket(ip, port);
            ois = new ObjectInputStream(s.getInputStream());
            pw = new PrintWriter(s.getOutputStream());
            
            if(ois.readBoolean()){
                new LevelChooser(new ChosenListener() {
                    @Override
                    public void chosenOption(String name) {
                        pw.println("level "+name);
                        startGame();
                    }
                });
            }
            else{
                startGame();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void startGame(){
        try{
            game = (Sopa)ois.readObject();
            Sopitas window = new Sopitas(game, player, new DiscoverListener() {
                @Override
                public void discoverWord(String player, int i1, int j1, int i2, int j2) {
                    pw.println("discover "+i1+" "+j1+" "+i2+" "+j2+"");
                }
            });
            while(true){
                game = (Sopa)ois.readObject();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
