
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
        try {
            String addr = "127.0.0.1";
            if(args.length>0){
                addr = args[0];
            }
            int port = 8080;
            Socket s = new Socket(addr, port);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader br2 = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            while (true) {
                System.out.print("NopalSQL> ");
                String line = "";
                while (!line.endsWith(";")) {
                    line += br.readLine();
                }
                pw.println(line);
                pw.flush();
                if (line.equals("exit")) {
                    break;
                }
                do {
                    System.out.println(br2.readLine());
                }while (br2.ready());
            }
            br2.close();
            br.close();
            pw.close();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
