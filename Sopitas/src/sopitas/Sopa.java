/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sopitas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author david
 */
public class Sopa {
    Cell[][] letters;
    String hints;
    int w,h;
    ArrayList<Word> words = new ArrayList<>();
    int[] start;
    
    public Sopa(String[] words,String hints,int w,int h){
        this.w = w;
        this.h = h;
        this.letters = new Cell[w][h];
        this.hints = hints;
        
        for(String word:words){
            placeWord(word);
        }
        fill();
    }
    
    private void placeWord(String word){
        boolean valid = false;
        int x0=0,y0=0,dx=0,dy=0,x1=0,y1=0;
        int l = word.length();
        while(!valid){
            valid = true;
            x0 = (int)(Math.random()*w);
            y0 = (int)(Math.random()*h);
            do{
                dx = (int)(Math.random()*3)-1;
                //dy = (int)(Math.random()*3)-1;
                dy = (int)(Math.random()*2);
            }while(dx+dy==0);
            x1 = x0+l*dx;
            y1 = y0+l*dy;
            if(x1>=w||x1<0||y1>=h||y1<0){
                valid = false;
                continue;
            }
            for(int i = 0,px = x0,py=y0;i<l;i++,px+=dx,py+=dy){
                if(letters[px][py]!=null){
                    char c1 = letters[px][py].c;
                    char c2 = word.charAt(i);
                    if(c1!=c2){
                        valid = false;
                        break;
                    }
                }
            }
        }
        Word wo = new Word(word, x0, y0, dx, dy);
        words.add(wo);
        placeWord(wo);
    }
    
    private void placeWord(Word word){
        for(int i = 0,px = word.x,py=word.y;i<word.l;i++,px+=word.dx,py+=word.dy){
            letters[px][py]=new Cell(word.word.charAt(i));
        }
    }
    
    private void fill(){
        for(int i=0;i<w;i++){
            for(int j=0;j<h;j++){
                if(letters[i][j]==null){
                    //letters[i][j] = new Cell((char)((int)(Math.random()*26)+'a'));
                    letters[i][j] = new Cell(' ');
                }
            }    
        }
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }
    
    public Cell getLetter(int i,int j){
        return letters[i][j];
    }
    
    public void selectCell(int i,int j){
        if(start==null){
            start = new int[]{i,j};
            letters[i][j].selected = true;
            System.out.println("start: "+i+","+j);
        }
        else{
            for(Word word:words){
                if(
                        !word.discovered&&
                        word.x==start[0]&&
                        word.y==start[1]&&
                        word.x1==i&&
                        word.y1==j
                ){
                    word.discovered = true;
                    for(int i1 = 0,px = word.x,py=word.y;i1<word.l;i1++,px+=word.dx,py+=word.dy){
                        letters[px][py].discovered = true;
                    }
                    System.out.println("Palabra descubierta: "+word.word);
                    break;
                }
            }
            letters[start[0]][start[1]].selected = false;
            start = null;
        }
    }
    
    static Sopa read(String name){
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(Sopa.class.getResourceAsStream(name)));
            boolean d = true;
            String desc="<html>";
            ArrayList<String> words = new ArrayList<>();
            
            String[] size = br.readLine().split(",");
            
            while(br.ready()){
                String line = br.readLine();
                if(line.equals("--")){
                    desc += "</html>";
                    d = false;
                    continue;
                }
                if(d){
                    desc += line+"<br>";
                }
                else{
                    words.add(line);
                }
            }
            br.close();
            return new Sopa(words.toArray(new String[0]), desc, Integer.parseInt(size[0]), Integer.parseInt(size[1]));
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
class Cell{
    boolean discovered=false;
    boolean selected=false;
    char c;

    public Cell(char c) {
        this.c = c;
    }
    
}
class Word{
    String word;
    int x,y,dx,dy,l,x1,y1;
    boolean discovered = false;

    public Word(String word, int x, int y, int dx, int dy) {
        this.word = word;
        this.l = word.length();
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.x1 = x+dx*(l-1);
        this.y1 = y+dy*(l-1);
        System.out.println(word+": "+x+","+y+" "+x1+","+y1);
    }
    
    public void discover(){
        discovered = true;
    }

}