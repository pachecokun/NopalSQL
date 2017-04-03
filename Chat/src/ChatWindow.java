import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class ChatWindow extends JFrame{
    private static final String IP_ADDR = "230.0.0.1";
    private static final int PORT = 5637, MAX_LEN = 65536;
    String chat="";
    String username;
    JTextField inputMsg;
    JTextPane chatBox;
    MulticastSocket s;
    
    
    Thread escuchar = new Thread() {
        
        @Override
        public void run() {
            String msj, HTMLtxt;
            HTMLDocument d = (HTMLDocument) chatBox.getDocument();
            HTMLEditorKit editorKit = (HTMLEditorKit) chatBox.getEditorKit();
            try {
                MulticastSocket cl = new MulticastSocket(PORT);
                cl.joinGroup(InetAddress.getByName(IP_ADDR));
                
                for(;;){
                    byte[] b = new byte[MAX_LEN];
                    DatagramPacket p = new DatagramPacket(b, b.length);
                    cl.receive(p);
                    msj = new String(p.getData());
                    System.out.println("Recibido: "+msj);
                    /*HTMLtxt = "<p style=\"font-family: sans-serif\">"
                        + "<strong style=\"color: blue\">Demis: </strong>"
                        + msj
                        +"</p>";*/
                    HTMLtxt = "<p style=\"font-family: sans-serif\">"+msj+"</p>";
                    editorKit.insertHTML(d, d.getLength(), HTMLtxt, 0, 0, null);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(0);
            }
        }
    };
    
    ActionListener enviarListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                String msj = inputMsg.getText();
                byte[] b = msj.getBytes();
                DatagramPacket p = new DatagramPacket(b, b.length, InetAddress.getByName(IP_ADDR), PORT);
                s.send(p);
                System.out.println("Enviado: "+msj);
                inputMsg.setText("");
            }
            catch(Exception ex){
                ex.printStackTrace();
                System.exit(0);
            }
        }
    };
    
    public ChatWindow(String username){
        super("Chat");
        this.setLayout(new BorderLayout());
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.username = username;
        
        try{
            s = new MulticastSocket();
        }catch(Exception ex){
            ex.printStackTrace();
            System.exit(0);
        }
        
        JLabel userLabel = new JLabel("<html>Usuario: <i>"+username+"</i></html>");
        this.add(userLabel, BorderLayout.PAGE_START);
        
        chatBox = new JTextPane();
        chatBox.setContentType("text/html");
        chatBox.setEditable(false);
        JScrollPane spMsg = new JScrollPane(chatBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(spMsg, BorderLayout.CENTER);
        
        JPanel userList = new JPanel();
        userList.setLayout(new GridLayout(25, 1));
            for(int i=0; i<25; i++){
                JButton b = new JButton("Usuario "+(i+1));
                b.setPreferredSize(new Dimension(200, 30));
                userList.add(b);
            }
        JScrollPane spUser = new JScrollPane(userList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(spUser, BorderLayout.LINE_END);
        
        JPanel chatBar = new JPanel();
            inputMsg = new JTextField();
            inputMsg.setPreferredSize(new Dimension(200, 30));
            chatBar.add(inputMsg);
            JButton enviar = new JButton("Enviar");
                enviar.setActionCommand("send");
                enviar.addActionListener(enviarListener);
                chatBar.add(enviar);
            JButton adjuntar = new JButton("Adjuntar");
                adjuntar.setActionCommand("adj");
                adjuntar.addActionListener(null);
                chatBar.add(adjuntar);
        
        this.add(chatBar, BorderLayout.PAGE_END);
        
        
        escuchar.start();
        
        
        this.rootPane.setDefaultButton(enviar);
        this.setVisible(true);
    }
}
