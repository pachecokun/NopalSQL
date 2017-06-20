/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.awt.Rectangle;
import java.io.Serializable;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author david
 */
public class Player implements Serializable{
    public final static int W = 50;
    public final static int H = 50;
    private float px  =  0;
    private float py = 0;
    private float vx  =  0;
    private float vy = 0;
    private String name;
    private int sprite;
    private int maxx;
    private int maxy;
    private boolean crashed = false;

    public Player(String name,int sprite, int maxx, int maxy) {
        this.name = name;
        this.sprite = sprite;
        this.maxx = maxx;
        this.maxy = maxy;
    }
    
    public void update(float dt){
        if(px>-W){
            px += dt*vx;
        }
        py = min(max(0,py+dt*vy),maxy-H);
    }
    
    public void detect(CopyOnWriteArrayList<Player> enemies){
        if(crashed){
            return;
        }
        Rectangle r = new Rectangle((int)px, (int)py, (int)W, (int)H);
        for(Player p:enemies){
            Rectangle r2 = new Rectangle((int)p.getPx(), (int)p.getPy(), (int)W, (int)H);
            if(r.intersects(r2)){
                crashed = true;
                p.setCrashed(true);
                break;
            }
        }
    }

    public float getPx() {
        return px;
    }

    public void setPx(float px) {
        this.px = px;
    }

    public float getPy() {
        return py;
    }

    public void setPy(float py) {
        this.py = py;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public int getSprite() {
        return sprite;
    }

    public String getName() {
        return name;
    }

    public boolean isCrashed() {
        return crashed;
    }

    public void setCrashed(boolean crashed) {
        this.crashed = crashed;
    }
    
    
      
}
