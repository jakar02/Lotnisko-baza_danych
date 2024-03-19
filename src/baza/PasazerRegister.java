package baza;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDate;

public class PasazerRegister extends JFrame{
    private JButton button1;
    private JTextField imiefield;
    private JTextField nazwiskofield;
    private JTextField peselfield;
    private JTextField dataurfield;
    private JTextField plecfield;
    private JTextField telefonfield;
    private JTextField emailfield;
    private JTextField loginfield;
    private JTextField haslofield;
    private JPanel MainPanel;
    private JButton backbutton;

    public PasazerRegister() {
        setContentPane(MainPanel);
        setTitle("Pasażer rejestracja");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        backbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PasazerLoginRegis();
                dispose();
            }
        });

        //(String username, String password, String name, String surname, String email, String phone, boolean gender, LocalDate birthDate, String pesel)
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pasazer pasazer = new Pasazer();
                Status status = pasazer.register(loginfield.getText(), haslofield.getText(), imiefield.getText(), nazwiskofield.getText(), emailfield.getText(), telefonfield.getText(), Boolean.parseBoolean(plecfield.getText()), LocalDate.parse(dataurfield.getText()), peselfield.getText());
                //Status status = pasazer.register(loginfield.getText(), haslofield.getText(), imiefield.getText(), nazwiskofield.getText(), peselfield.getText(), telefonfield.getText(), Boolean.parseBoolean(plecfield.getText()), LocalDate.parse(dataurfield.getText()), emailfield.getText());
                if(status.equals(Status.SUCCESS)){
                    JOptionPane.showMessageDialog(null, "Zarejestrowano");
                    new PasazerLogin();
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Blad rejestracji: " + status.getDescription());
                }

            }
        });
    }
}
