/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author david
 */
public class Juego implements Serializable{
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Player> enemies = new ArrayList<>();
    float speed = -100;
    float accel = -10;
    float pb = 0;
    int sizex = 500;
    int sizey = 500;
    int status = 0;
    int winner = 0;
    
    public void update(float dt){
        if(status!=1)return;
        
        pb += (float)dt*speed;
        speed+=accel*dt;
        if(pb<=-sizex){
            pb=0;
        }
        for(Player p:enemies){
            p.update(dt);
            if(p.getPx()<-Player.W){
                enemies.remove(p);
            }
        }
        int crashed = 0;
        for(Player p:players){
            p.update(dt);
            p.detect(enemies);
            if(p.isCrashed()){
                p.setVx(speed);
                p.setVy(0);
                crashed++;
            }
        }
        if(players.size()-crashed==1){
            status = 2;
            for(Player p:players){
                if(!p.isCrashed()){
                    winner = players.indexOf(p);
                }
            }
        }
    }
    
    public int addPlayer(String name){
        int id = players.size()+1;
        Player p = new Player(name,id, sizex, sizey);
        p.setPy(50+players.size()*70);
        p.setPx(30);
        players.add(p);
        return id;
    }
    
    public void createEnemy(){
        Player p = new Player("", 0, sizex, sizey);
        p.setPy((float) (Math.random()*(sizey-Player.H)));
        p.setPx(sizex);
        p.setVx(speed);
        enemies.add(p);
    }
}
