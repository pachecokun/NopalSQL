import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.util.ArrayList;

public class ChatWindow extends JFrame{
    ArrayList<String> members = new ArrayList<>();
    Chat c;
    String username;
    JPanel userContainer, userList;
    JTextField inputMsg, privInputMsg;
    JLabel friendsConnected;
    JTextPane chatBox;
    HTMLDocument d;
    HTMLEditorKit editorKit;
    JDialog privDialog;
    JButton privSend;
    JScrollPane spMsg;
    JScrollBar vertical;
    private final static String[][] EMOTICONS = {
        {"7n7", "7n7"},
        {"7u7", "7u7"},
        {"(N)", "bad"},
        {"(n)", "bad"},
        {"(B)", "beer"},
        {"(b)", "beer"},
        {":/", "confused"},
        {":'(", "cry"},
        {".i.", "finger"},
        {"8)", "glasses"},
        {"(Y)", "good"},
        {"(y)", "good"},
        {":)", "happy"},        
        {":D", "happy2"},
        {":P", "happytongue"},
        {":p", "happytongue"},
        {"<3", "heart"},
        {"(:", "invsmile"},
        {":*", "kiss"},
        {"JAJAJA", "laugh"},
        {"jajaja", "laugh"},
        {"lml", "merol"},
        {":|", "neutral"},
        {":(", "sad"},
        {"B)", "sunglasses"},
        {":O", "surprised"},
        {":o", "surprised"},
        {"D:", "surprised2"},
        {";)", "wink"},
        {"XD", "xd"},
        {"xd", "xd"},
        {"Xd", "xd"},
        {"xD", "xd"}
    };
    
    ChatListener chatListener = new ChatListener() {
        @Override
        public void messageReceived(String dest, String sender, String msg) {
            String HTMLtxt;
            msg = showEmoticons(msg);
            if(sender.equals(username)){
                HTMLtxt = "<p align=\"right\" style=\"font-family: sans-serif\">";
                if(dest.length()==0)
                    HTMLtxt += "<strong style=\"color: rgb(60, 150, 20)\">Yo: </strong>";
                else
                    HTMLtxt += "<strong style=\"color: rgb(160, 0, 160)\">[para "+dest+"] Yo: </strong>";
                HTMLtxt += msg +"</p>";
            }
            else{
                HTMLtxt = "<p style=\"font-family: sans-serif\">";
                if(dest.equals(username))
                    HTMLtxt += "<strong style=\"color: rgb(150, 20, 60)\">[Privado] "+sender+": </strong>";
                else
                    HTMLtxt += "<strong style=\"color: rgb(20, 60, 150)\">"+sender+": </strong>";
                HTMLtxt += msg + "</p>";
            }
            writeInChatbox(HTMLtxt);            
        }

        @Override
        public void fileReceived(String sender, String file) {
            String HTMLtxt = "<p style=\"font-family: sans-serif\">";
                HTMLtxt += "<strong style=\"color: rgb(150, 20, 60)\">"+sender+": </strong>";
            HTMLtxt += "Archivo recibido: <a href=\"file://"+file+"\">"+file+"</a></p>";
            writeInChatbox(HTMLtxt);          
            System.out.println("archivo recibido. "+sender+" . "+file);
        }

        @Override
        public void clientConnected(boolean response, String name) {
            members.add(name);
            updateUserList();
            if(!response){
                String HTMLtxt = "<p align=\"center\" style=\"font-family: sans-serif\"><i>"+name+" ha entrado a la sala</i></p>";
                writeInChatbox(HTMLtxt);
            }
        }

        @Override
        public void clientDisconnected(String name) {
            int n = members.size();
            for(int i=0; i<n; i++){
                if(members.get(i).equals(name)){
                    members.remove(i);
                    break;
                }
            }
            updateUserList();
            String HTMLtxt = "<p align=\"center\" style=\"font-family: sans-serif\"><i>"+name+" se ha ido</i></p>";
            writeInChatbox(HTMLtxt);
        }
    };
    
    ActionListener sendPrivateListener = new ActionListener(){
       @Override
       public void actionPerformed(ActionEvent e){
           privDialog.setTitle("Enviar mensaje privado a "+e.getActionCommand());
           privSend.setActionCommand(e.getActionCommand());
           privDialog.setVisible(true);
       }
    };
    
    ActionListener attachListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                Path current = Paths.get("");
                Desktop.getDesktop().open(new File(current.toAbsolutePath().toString()));
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    };
    
    ActionListener sendListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String u = e.getActionCommand();
            String msg;
            if(u.equals("")){
                msg = inputMsg.getText();
                if(msg.equals(""))
                    JOptionPane.showMessageDialog(null, "Escriba un mensaje", "Error", JOptionPane.ERROR_MESSAGE);
                else{
                    c.sendMessage(u, msg);
                    inputMsg.setText("");
                } 
            }
            else{
                msg = privInputMsg.getText();
                if(msg.equals(""))
                    JOptionPane.showMessageDialog(null, "Escriba un mensaje", "Error", JOptionPane.ERROR_MESSAGE);
                else{
                    c.sendMessage(u, msg);
                    privInputMsg.setText("");
                    privDialog.setVisible(false);
                }
            }
            
        }
    };
    
    HyperlinkListener explorerListener = new HyperlinkListener() {
        @Override
        public void hyperlinkUpdate(HyperlinkEvent e) {
            if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
                try{
                    Path current = Paths.get("");
                    Desktop.getDesktop().open(new File(current.toAbsolutePath().toString()));
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    };
    
    public ChatWindow(String username){
        super("Pacheco Live Messenger");
        this.setLayout(new BorderLayout());
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.username = username;
        
        //SEND PRIVATE DIALOG
        JPanel dialogContainer = new JPanel();
            privInputMsg = new JTextField();
            privInputMsg.setPreferredSize(new Dimension(200, 30));
            dialogContainer.add(privInputMsg);
            privSend = new JButton("Enviar");
            privSend.setActionCommand("");
            privSend.addActionListener(sendListener);
            dialogContainer.add(privSend);
        
        privDialog = new JDialog(this, "Enviar mensaje privado", true);
        privDialog.setSize(350, 100);
        privDialog.setLocationRelativeTo(null);
        privDialog.add(dialogContainer);
        privDialog.getRootPane().setDefaultButton(privSend);
        
        //USERNAME LABEL
        JLabel userLabel = new JLabel("<html>Usuario: <i>"+username+"</i></html>");
        this.add(userLabel, BorderLayout.PAGE_START);
        
        //CHATBOX
        chatBox = new JTextPane();
        chatBox.setContentType("text/html");
        chatBox.addHyperlinkListener(explorerListener);
        chatBox.setEditable(false);
            d = (HTMLDocument) chatBox.getDocument();
            editorKit = (HTMLEditorKit) chatBox.getEditorKit();
        spMsg = new JScrollPane(chatBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(spMsg, BorderLayout.CENTER);
        
        //USERS LIST
        userContainer = new JPanel();
            userContainer.setLayout(new BorderLayout());
            friendsConnected = new JLabel("Amigos conectados (0)");
            userContainer.add(friendsConnected, BorderLayout.PAGE_START);
            userList = new JPanel();
            JScrollPane spUser = new JScrollPane(userList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            userContainer.add(spUser, BorderLayout.CENTER); 
        this.add(userContainer, BorderLayout.LINE_END);
        
        //CHAT BAR
        JPanel chatBar = new JPanel();
            inputMsg = new JTextField();
            inputMsg.setPreferredSize(new Dimension(200, 30));
            chatBar.add(inputMsg);
            JButton enviar = new JButton("Enviar");
                enviar.setActionCommand("");
                enviar.addActionListener(sendListener);
                chatBar.add(enviar);
            JButton adjuntar = new JButton("Adjuntar");
                adjuntar.setActionCommand("adj");
                adjuntar.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser jfc = new JFileChooser();
                        int resp = jfc.showOpenDialog(null);
                        if(resp == jfc.APPROVE_OPTION){
                            c.sendFile("", jfc.getSelectedFile().getAbsolutePath());
                        }
                    }
                });
                chatBar.add(adjuntar);    
        this.add(chatBar, BorderLayout.PAGE_END);        
        
        c = new Chat(username, chatListener);
        
        this.rootPane.setDefaultButton(enviar);
        this.setVisible(true);
    }
    
    public void updateUserList(){
        userList.removeAll();
        int n = members.size();
        friendsConnected.setText("Amigos conectados ("+n+")");
        userList.setLayout(new GridLayout(n, 1));
        for(int i=0; i<n; i++){
            String u = members.get(i);
            JButton b = new JButton(u);
            b.setActionCommand(u);
            b.addActionListener(sendPrivateListener);
            b.setPreferredSize(new Dimension(200, 30));
            userList.add(b);
        }
        ChatWindow.this.repaint();
        ChatWindow.this.revalidate();
    }
    
    public String showEmoticons(String s){
        
        for(int i=0; i<EMOTICONS.length; i++){
            String symbol = EMOTICONS[i][0];
            String img = "<img src=\"file:resources/"+EMOTICONS[i][1]+".png\">";
            int start =0, foundPos =0;
            while(foundPos != -1){
                foundPos = s.indexOf(symbol, start);
                if(foundPos != -1){
                    String s1 = s.substring(0, foundPos);
                    String s2 = s.substring(foundPos+symbol.length(), s.length());
                    s = s1 + img + s2;
                    start = s1.length() + img.length();
                }
            }
        }
        return s;
    }
    
    public void writeInChatbox(String txt){
        try{
            editorKit.insertHTML(d, d.getLength(), txt, 0, 0, null);
            vertical = spMsg.getVerticalScrollBar();
            vertical.setValue( spMsg.getVerticalScrollBar().getMaximum() );
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
