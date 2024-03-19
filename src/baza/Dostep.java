package baza;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dostep extends JFrame{
    private JButton button1;
    private JButton button2;
    private JPanel MainPanel;


    public Dostep(){
        setContentPane(MainPanel);
        setTitle("Lotnisko");
        setSize(600,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminView();
                dispose();
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PasazerLoginRegis();
                dispose();
            }
        });


    }

    public static void main(String[] args) {
        new Dostep();
    }
}
