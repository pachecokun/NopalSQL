/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sopitas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author david
 */
public class Sopitas extends JFrame{

    SopaPanel sp;
    String player;
    int[][] start = null;
    JLabel lbl_game;
    
    
    public Sopitas(Sopa s,String player,DiscoverListener listener){
        super("Sopitas");
        this.player = player;
        
        setSize(700,500);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
        sp = new SopaPanel(s,player,listener,10, 10, 450, 450);
                
        JLabel l = new JLabel(s.hints);
        l.setVerticalAlignment(l.NORTH);
        l.setBounds(500,10,250,200);
        add(l);
        
        lbl_game = new JLabel();
        lbl_game.setVerticalAlignment(l.NORTH);
        lbl_game.setBounds(500, 250, 250, 200);
        add(lbl_game);
        
        add(sp);
        
        repaint();
        updateInfo(s);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Sopa s = Sopa.read("../sopas/animales.txt");
        
        s.addPlayer("David");
        
        new Sopitas(s,"David",null);
    }
    
    public void update(Sopa s){
        sp.updateGame(s);
        updateInfo(s);
    }
    
    void updateInfo(Sopa s){
        String text = "<html>";
        
        text += "Tiempo restante: "+s.time+"<br><br>";
        
        for(Player p:s.players){
            text+=p.name+": "+p.score+"/"+p.longest+"<br>";
        }
        text += "</html>";
        lbl_game.setText(text);
    }
    
}
class SopaPanel extends JPanel{
    Sopa s;
    int w_cell,h_cell;
    int[] start = null;
    String player;
    DiscoverListener listener;
    
    public void updateGame(Sopa s){
        this.s = s;
        repaint();
    }

    public SopaPanel(Sopa s,String player,DiscoverListener listener,int x,int y,int w,int h) {
        this.s = s;
        this.player = player;
        this.listener = listener;
        setBounds(x, y, w, h);
        this.w_cell = (int)((getWidth())/s.getW());
        this.h_cell = (int)((getHeight())/s.getH());
        
        addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
                int i = e.getX()/w_cell;
                int j = e.getY()/h_cell;
                
                if(start==null){
                    start = new int[]{i,j};
                }
                else{
                    listener.discoverWord(player, start[0], start[1], i, j);
                    start = null;
                }
                repaint();
            }
         
        });
    }    
    
    public void paint(Graphics g){
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        
        g.setColor(Color.BLACK);
        g.setFont(new Font("Consolas", Font.PLAIN, h_cell-10));
        g.drawRect(0, 0, getWidth()-1, getHeight()-1);
        for(int i=0;i<s.getW();i++){
            for(int j=0;j<s.getH();j++){
                Cell c = s.getLetter(i, j);
                Color bg;
                if(c.selected||(start!=null&&i==start[0]&&j==start[1])){
                    bg = Color.YELLOW;
                }
                else if(c.discovered){
                    bg = Color.GREEN;
                }
                else{
                    bg = Color.WHITE;
                }
                g.setColor(bg);
                g.fillRect(w_cell*i, h_cell*j, w_cell, h_cell);
                g.setColor(Color.BLACK);
                g.drawRect(w_cell*i, h_cell*j, w_cell, h_cell);
                g.drawString(""+c.c, w_cell*i+5, h_cell*(j+1)-5);
            }   
        }
    }
}
interface DiscoverListener{
    void discoverWord(String player,int i1,int j1,int i2,int j2);
}