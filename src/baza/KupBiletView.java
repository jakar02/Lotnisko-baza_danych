package baza;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class KupBiletView extends JFrame {

    private JPanel MainPanel;
    private JButton nadajBagazButton;
    private JButton powrotButton;
    private JTextField wagaBagazuTextField;

    public KupBiletView(Pasazer pasazerLogin, int idpasazer, int idbilet, String lotnisko_start, String lotnisko_stop, int id_pasazer_bilet_local){
        setContentPane(MainPanel);
        setTitle("Pasażer akcje");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        powrotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PasazerView(pasazerLogin);
                dispose();
            }
        });

        // Dodaj pole w klasie KupBiletView


// Dodaj ActionListener dla przycisku nadajBagazButton
        nadajBagazButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pobierz wartość z pola tekstowego
                String wagaBagazu = wagaBagazuTextField.getText();
                String UPDATE_BAGGAGE_QUERY = "UPDATE pasazer_bilet SET waga_bagazu = ? WHERE id_pasazer_bilet = ?";

                DatabaseConnector dbConnector = new DatabaseConnector();
                Connection connection = dbConnector.connect();

                if (connection != null) {
                    try {
                        PreparedStatement statement = connection.prepareStatement(UPDATE_BAGGAGE_QUERY);
                        statement.setFloat(1, Float.parseFloat(wagaBagazu));
                        statement.setInt(2, id_pasazer_bilet_local);

                        int rowsAffected = statement.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(KupBiletView.this, "Bagaż nadany pomyślnie.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(KupBiletView.this, "Błąd podczas nadawania bagażu. Brak pasującego id_pasazer_bilet w bazie danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        // Obsługa błędów SQL
                    } finally {
                        dbConnector.closeConnection(connection);
                    }
                }
            }
        });


    }
}
