package baza;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class PasazerLogin extends JFrame {
    private JPanel MainPanel;
    private JTextField loginfield;
    private JTextField passwordfield;
    private JButton button1;
    private JButton powrotButton;
    //private JButton backButton;

    public PasazerLogin(){
        setContentPane(MainPanel);
        setTitle("Pasażer logowanie");
        setSize(600,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        powrotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PasazerLoginRegis();
                dispose();
            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pasazer pasazerLogin = new Pasazer();
                if(pasazerLogin.login(loginfield.getText(), passwordfield.getText())){
                    new PasazerView(pasazerLogin);
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Zly login lub haslo");
                }

            }
        });
    }
}
