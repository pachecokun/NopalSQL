import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class chatMain extends JFrame{
    JTextField nombre;
    
    public chatMain(){
        super("Conectarse");
        this.setLayout(new BorderLayout());
        this.setSize(400, 150);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        
        JPanel container = new JPanel();
        JLabel nombreLabel = new JLabel("Nombre:");
        container.add(nombreLabel);
        nombre = new JTextField();
        nombre.setPreferredSize(new Dimension(250, 30));
        container.add(nombre);
        this.add(container, BorderLayout.CENTER);
        
        JPanel btnContainer = new JPanel();
        JButton conectar = new JButton("Conectarse");
        btnContainer.add(conectar);
        conectar.addActionListener(conectarListener);
        this.add(btnContainer, BorderLayout.PAGE_END);
        
        
        this.rootPane.setDefaultButton(conectar);
        this.setVisible(true);
    }
    
    ActionListener conectarListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new ChatWindow(nombre.getText());
            chatMain.this.setVisible(false);
        }
    };
    
    public static void main(String[] args) {
        new chatMain();
    }
    
}
