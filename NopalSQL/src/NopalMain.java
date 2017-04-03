
import java.io.*;
import java.net.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Demis
 */
public class NopalMain {

    Analyser a;
    ServerSocket s;
    private final static int PORT = 8080;

    public NopalMain() throws IOException {
        a = new Analyser();
        s = new ServerSocket(PORT);
        s.setReuseAddress(true);
        System.out.println("Servicio iniciado... esperando clientes");
    }

    public static void main(String[] args) {
        try {
            NopalMain myNopal = new NopalMain();
            for (;;) {
                Socket cl = myNopal.s.accept();
                System.out.println("Cliente conectado desde: " + cl.getInetAddress() + ":" + cl.getPort());
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
                BufferedReader br = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                String msj = "";
                for (;;) {
                    msj = br.readLine();
                    if (msj.equalsIgnoreCase("exit")) {
                        System.out.println("El cliente termino");
                        br.close();
                        pw.close();
                        cl.close();
                        break;
                    } else {
                        System.out.println("Instrucción recibida: \n" + msj);
                        myNopal.a.execute(msj);
                        pw.println(myNopal.a.getMessage());
                        pw.flush();
                        System.out.println("Enviado:\n"+myNopal.a.getMessage());
                        continue;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
