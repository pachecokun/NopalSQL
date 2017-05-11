/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sopitas;

import java.io.Serializable;

/**
 *
 * @author david
 */
public class Player  implements Serializable{
    String name;
    int score = 0;
    int longest = 0;

    public Player(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
