
import java.io.BufferedReader;
import java.io.InputStreamReader;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author david
 */
public class Cliente {
    public static void main(String[] args) {
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Ingrese la dirección del servidor: ");
            String addr = br.readLine();
            System.out.print("Ingrese el puerto del servidor: ");
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
