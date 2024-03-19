package baza;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasazerLoginRegis extends JFrame{
    //private JPanel customerLoginAndRegistrationPanel;
    private JButton button1;
    private JButton button2;
    private JPanel MainPanel;
    private JButton powrotButton;
    //private JButton backButton;

    public PasazerLoginRegis(){
        setContentPane(MainPanel);
        setTitle("Pasazer logowanie i rejestracja");
        setSize(600,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        powrotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Dostep();
                dispose();
            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PasazerLogin();
                dispose();
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PasazerRegister();
                dispose();
            }
        });
    }
}
