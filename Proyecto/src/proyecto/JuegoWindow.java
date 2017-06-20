/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author david
 */
public class JuegoWindow {
    JFrame f;
    GamePanel p = new GamePanel();
    int id = 0;
    ObjectOutputStream oos;
    Message m = new Message();
    

    public JuegoWindow(String ip,int port) {
        f = new JFrame();
        f.setSize(500, 500);
        f.setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setFocusable(true);
        f.addKeyListener(listener);
        
        p.setBounds(0, 0, 500, 500);
        f.add(p);
        
        f.setVisible(true);
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket s = new Socket(ip, port);
                    ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                    oos = new ObjectOutputStream(s.getOutputStream());
                    oos.writeObject(JOptionPane.showInputDialog("Introduzca su nombre"));
                    oos.flush();
                    oos.reset();
                    id = ois.readInt();
                    m.id = id;
                    while(true){
                        p.update((Juego)ois.readObject());
                        System.out.println("Juego actualizado");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
        
    }
    
    KeyListener listener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            int code = e.getKeyCode();
            if(code==KeyEvent.VK_UP){
                m.my = -1;
            }
            if(code==KeyEvent.VK_DOWN){
                m.my = 1;
            }
            sendMessagge();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int code = e.getKeyCode();
            if(code==KeyEvent.VK_UP){
                m.my = 0;
            }
            if(code==KeyEvent.VK_DOWN){
                m.my = 0;
            }
            sendMessagge();
        }
    };
    
    private void sendMessagge(){
        try {
            oos.writeObject(m);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new JuegoWindow("127.0.0.1",5555);
    }
}
class GamePanel extends JPanel{
    Juego j=new Juego();
    Image[] spritePlayers = new Image[]{
        loadSprite("enemy.png"),
        loadSprite("ship.png"),
        loadSprite("exp.png")
    };
    Image background;

    public GamePanel() {
        background = loadSprite("bg.jpg");
    }

    private Image loadSprite(String name){
        try {
            return ImageIO.read(getClass().getResourceAsStream("sprites/"+name));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void drawSprite(Graphics g,Image img,float x,float y,float w,float h){
        g.drawImage(img, (int)x, (int)y, (int)w,(int)h,null);
    }
    
    private void drawBackgroud(Graphics g){
        drawSprite(g, background, j.pb, 0, getWidth(), getHeight());
        drawSprite(g, background, j.pb+getWidth(), 0, getWidth(), getHeight());
    }

    private void drawPlayer(Graphics g, Player p){
        int sprite = p.isCrashed()?2:(p.getSprite()>0?1:0);
        drawSprite(g, spritePlayers[sprite],p.getPx(),p.getPy(),Player.W,Player.H);
        g.drawString(p.getName(), (int)p.getPx(), (int)p.getPy()+Player.H);
    }

    private void drawPlayers(Graphics g){
        for(Player p:j.players){
            drawPlayer(g, p);
        }
        for(Player p:j.enemies){
            drawPlayer(g, p);
        }
    }

    public void update(Juego j){
        this.j = j;
        repaint();
    }
    
    public void paint(Graphics g){
        g.setColor(Color.white);
        g.clearRect(0, 0, getWidth(), getHeight());
        drawBackgroud(g);
        drawPlayers(g);
        if(j.status==0){
            g.drawString("Esperando jugadores...", 20,20);
        }
        if(j.status==2){
            g.setFont(new Font("", Font.BOLD, 30));
            g.drawString("Ganador: "+j.players.get(j.winner).getName(), 100,200);
        }
    }
}