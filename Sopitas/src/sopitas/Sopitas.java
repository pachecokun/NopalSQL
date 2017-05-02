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

    Sopa s;
    SopaPanel sp;
    
    public Sopitas(Sopa s){
        super("Sopitas");
        setSize(700,500);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
        sp = new SopaPanel(s,10, 10, 450, 450);
                
        JLabel l = new JLabel(s.hints);
        l.setVerticalAlignment(l.NORTH);
        l.setBounds(500,10,250,450);
        add(l);
        
        add(sp);
        
        this.s = s;
        repaint();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        new Sopitas(Sopa.read("../sopas/ciudades.txt"));
    }
    
}
class SopaPanel extends JPanel{
    Sopa s;
    int w_cell,h_cell;
    int[] start = null;

    public SopaPanel(Sopa s,int x,int y,int w,int h) {
        this.s = s;
        setBounds(x, y, w, h);
        this.w_cell = (int)((getWidth())/s.getW());
        this.h_cell = (int)((getHeight())/s.getH());
        
        addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getX()/w_cell+","+e.getY()/h_cell);
                s.selectCell(e.getX()/w_cell, e.getY()/h_cell);
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
            for(int j=0;j<s.getW();j++){
                Cell c = s.getLetter(i, j);
                Color bg;
                if(c.selected){
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