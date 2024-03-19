package baza;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class AdminView extends JFrame{
    private JTextField lotniskofield;
    private JTextField paspostojufield;
    private JTextField klasabiletufield;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JTextField lotniskoadresfield;
    private JTextField rokprofukcjifield;
    private JTextField nazwaLotniskaPostojuField;
    private JTextField linialotniczafield;
    private JTextField bagazefield;
    private JTextField pojemnoscludzifield;
    private JTextField cenaBiletuField;
    private JTextField dataOdlotuTextField;
    private JTextField dataPrzylotuTextField;
    private JTextField nazwaLotniskaStartowegoTextField;
    private JTextField nazwaLotniskaDocelowegoTextField;
    private JTextField samolotIdTextField;
    private JPanel MainPanel;
    private JButton powrotButton;
    private JButton podgladDanychButton;

    public AdminView() {
        setContentPane(MainPanel);
        setTitle("Admin: dodawanie informacji");
        setSize(600, 600);
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

        podgladDanychButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PodgladDanychView();
                dispose();
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Administrator admin = new Administrator();
                Administrator.Samolot samolot = admin.new Samolot();

                Status status = samolot.insertSamolot(LocalDate.parse(rokprofukcjifield.getText()), linialotniczafield.getText(), Integer.parseInt(pojemnoscludzifield.getText()), Integer.parseInt(bagazefield.getText()), nazwaLotniskaPostojuField.getText(),Integer.parseInt(paspostojufield.getText()));
                if(status.equals(Status.SUCCESS) ){
                    JOptionPane.showMessageDialog(null, "Pomyślnie dodano do bazy danych");
                    //dispose();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Blad dodawania: " + status.getDescription());
                }
            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Administrator admin = new Administrator();
                Administrator.Lotnisko lotnisko = admin.new Lotnisko();

                Status status = lotnisko.setAll(lotniskofield.getText(), lotniskoadresfield.getText());
                if(status.equals(Status.SUCCESS) ){
                    JOptionPane.showMessageDialog(null, "Pomyślnie dodano do bazy danych");
                    //dispose();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Blad dodawania: " + status.getDescription());
                }
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Administrator admin = new Administrator();
                Administrator.BiletNaLot nowybilet = admin.new BiletNaLot();


                    // Przekształć dane z JTextField na odpowiednie typy danych
                    int klasaBiletu = Integer.parseInt(klasabiletufield.getText());
                    int cenaBiletu = Integer.parseInt(cenaBiletuField.getText());
                    LocalDate dataOdlotu = LocalDate.parse(dataOdlotuTextField.getText());
                    LocalDate dataPrzylotu = LocalDate.parse(dataPrzylotuTextField.getText());
                    int samolotId = Integer.parseInt(samolotIdTextField.getText());

                    // Wstaw dane do bazy
                    Status status = nowybilet.setAll(klasaBiletu, cenaBiletu, dataOdlotu, dataPrzylotu, samolotId, nazwaLotniskaStartowegoTextField.getText(), nazwaLotniskaDocelowegoTextField.getText());

                    if (status.equals(Status.SUCCESS)) {
                        JOptionPane.showMessageDialog(null, "Pomyślnie dodano do bazy danych");
                        //dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Blad dodawania: " + status.getDescription());
                    }
            }
        });

    }
}
