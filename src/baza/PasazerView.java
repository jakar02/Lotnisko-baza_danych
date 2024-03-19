package baza;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PasazerView extends JFrame {
    private JButton znajdzlotybutton;
    private JPanel MainPanel;
    private JButton button4;
    private JTextField opiniaField;
    private JTextField lotniskoStartoweTextField;
    private JTextField lotniskoDoceloweTextField;
    private JScrollPane bazalotniskscroll;
    private JComboBox comboBox1;
    private JButton kupBiletButton;
    private JScrollPane bazalotowscroll;
    private JButton powrotButton;
    private JScrollPane opiniefield;
    private JScrollPane historialotowfield;

    public PasazerView(Pasazer pasazerLogin) {
        setContentPane(MainPanel);
        setTitle("Pasażer akcje");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        zaladujLotniskaDoJScrollPane();
        zaladujLotyDoJScrollPane();
        zaladujOpinieDoJScrollPane();
        zaladujhistorielotowDoJScrollPane();

        powrotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Dostep();
                dispose();
            }
        });

        znajdzlotybutton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            zaladujLotyDoComboBox();
        }
    });

        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pobierz dane z pola opiniaField
                String opinia = opiniaField.getText();

                // Sprawdź, czy opinia jest niepusta
                if (opinia.isEmpty()) {
                    JOptionPane.showMessageDialog(PasazerView.this, "Opinia nie może być pusta", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Pobierz identyfikator pasażera
                int pasazerId = pasazerLogin.getId();

                // Wykonaj zapytanie SQL do dodania opinii do tabeli
                String insertOpiniaQuery = "INSERT INTO opinie_pasazerow (pasazer_id, opinia) VALUES (" + pasazerId + ", '" + opinia + "')";

                try {
                    DatabaseConnector dbConnector = new DatabaseConnector();
                    Connection connection = dbConnector.connect();

                    if (connection != null) {
                        Statement statement = connection.createStatement();
                        statement.executeUpdate(insertOpiniaQuery);

                        dbConnector.closeConnection(connection);

                        // Wyświetl komunikat o dodaniu opinii
                        JOptionPane.showMessageDialog(PasazerView.this, "Opinia została dodana pomyślnie", "Dodano opinię", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    // Obsługa błędów SQL
                    JOptionPane.showMessageDialog(PasazerView.this, "Wystąpił błąd podczas dodawania opinii", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        kupBiletButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Administrator.BiletNaLot wybranyBilet = (Administrator.BiletNaLot) comboBox1.getSelectedItem();

                Administrator.PasazerBilet pasazerBilet = new Administrator.PasazerBilet();

                // Ustaw pola w obiekcie PasazerBilet
                pasazerBilet.setId_pasazera(pasazerLogin.getId());
                pasazerBilet.setId_biletu(wybranyBilet.getIdBiletu());

                // Dodaj informacje o zakupionym bilecie do tabeli pasazer_bilet
                pasazerBilet.insertPasazerBilet();
                JOptionPane.showMessageDialog(PasazerView.this, "Bilet na lot został pomyślnie kupiony", "Zakupiono bilet", JOptionPane.INFORMATION_MESSAGE);
                // Otwórz nowe okno lub wykonaj inne akcje związane z zakupem biletu
                KupBiletView nadajbagaz= new KupBiletView( pasazerLogin, pasazerLogin.getId(), wybranyBilet.getIdBiletu(), wybranyBilet.getLotniskoStart(), wybranyBilet.getLotniskoStop(), pasazerBilet.getId_pasazer_bilet());
                dispose();
            }
        });



    }

    private void zaladujLotniskaDoJScrollPane() {
        // Pobranie danych lotnisk z bazy danych
        List<String> lotniska = pobierzLotniskaZBazy();

        // Utworzenie modelu dla JList
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String lotnisko : lotniska) {
            listModel.addElement(lotnisko);
        }

        // Utworzenie JList i przypisanie modelu
        JList<String> lotniskaList = new JList<>(listModel);

        // Utworzenie JScrollPane i dodanie JList do niego
        JScrollPane scrollPane = new JScrollPane(lotniskaList);

        // Dodanie JScrollPane do odpowiedniego komponentu w Twoim GUI
        bazalotniskscroll.setViewportView(scrollPane);
    }

    private List<String> pobierzLotniskaZBazy() {
        List<String> lotniska = new ArrayList<>();

        try {
            DatabaseConnector dbConnector = new DatabaseConnector();
            Connection connection = dbConnector.connect();

            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT nazwa_lotniska FROM lotnisko");

                while (resultSet.next()) {
                    String nazwaLotniska = resultSet.getString("nazwa_lotniska");
                    lotniska.add(nazwaLotniska);
                }

                dbConnector.closeConnection(connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Obsługa błędów SQL
        }

        return lotniska;
    }

    private void zaladujLotyDoJScrollPane() {
        // Pobranie danych lotów z bazy danych
        List<Lot> loty = pobierzLotyZBazy();

        // Utworzenie modelu dla JList
        DefaultListModel<Lot> listModel = new DefaultListModel<>();
        for (Lot lot : loty) {
            listModel.addElement(lot);
        }

        // Utworzenie JList i przypisanie modelu
        JList<Lot> lotyList = new JList<>(listModel);

        // Utworzenie JScrollPane i dodanie JList do niego
        JScrollPane scrollPane = new JScrollPane(lotyList);

        // Dodanie JScrollPane do odpowiedniego komponentu w Twoim GUI
        bazalotowscroll.setViewportView(scrollPane);
    }

    private List<Lot> pobierzLotyZBazy() {
        List<Lot> loty = new ArrayList<>();

        try {
            DatabaseConnector dbConnector = new DatabaseConnector();
            Connection connection = dbConnector.connect();

            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT cena, klasa_biletu, data_odlotu, lotnisko_start, lotnisko_stop FROM bilet_na_lot");

                while (resultSet.next()) {
                    int cena = resultSet.getInt("cena");
                    String lotniskoStart = resultSet.getString("lotnisko_start");
                    String lotniskoStop = resultSet.getString("lotnisko_stop");
                    Date datawylotu = resultSet.getDate("data_odlotu");
                    int klasabiletu = resultSet.getInt("klasa_biletu");

                    Lot lot = new Lot(cena, lotniskoStart, lotniskoStop, datawylotu, klasabiletu);
                    loty.add(lot);
                }

                dbConnector.closeConnection(connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Obsługa błędów SQL
        }

        return loty;
    }

    private static class Lot {
        private final int cena;
        private final String lotniskoStart;
        private final String lotniskoStop;
        private final Date datawylotu;
        private final int klasa_lotu;

        public Lot(int cena, String lotniskoStart, String lotniskoStop, Date datawylotu, int klasa_lotu) {
            this.cena = cena;
            this.lotniskoStart = lotniskoStart;
            this.lotniskoStop = lotniskoStop;
            this.datawylotu = datawylotu;
            this.klasa_lotu = klasa_lotu;
        }

        @Override
        public String toString() {
            return "Cena: " + cena + "zł (" + klasa_lotu + " kl.), Data: " + datawylotu + ", Z: " + lotniskoStart + " , Do: " + lotniskoStop;
        }
    }


    private void zaladujLotyDoComboBox() {
        String lotniskoStartowe = lotniskoStartoweTextField.getText();
        String lotniskoDocelowe = lotniskoDoceloweTextField.getText();

        // Pobierz loty z bazy danych
        List<Administrator.BiletNaLot> loty = new Administrator().pobierzLoty(lotniskoStartowe, lotniskoDocelowe);

        // Utwórz model dla JComboBox z dostosowaną metodą toString
        DefaultComboBoxModel<Administrator.BiletNaLot> comboBoxModel = new DefaultComboBoxModel<>();
        for (Administrator.BiletNaLot lot : loty) {
            comboBoxModel.addElement(lot);
        }

        // Przypisz model do JComboBox
        comboBox1.setModel(comboBoxModel);

        // Ustaw renderowanie elementów ComboBox
        comboBox1.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Administrator.BiletNaLot) {
                    Administrator.BiletNaLot bilet = (Administrator.BiletNaLot) value;
                    setText("Skąd: " + bilet.getLotniskoStart() + ", Dokąd: " + bilet.getLotniskoStop() +
                            ", Cena: " + bilet.getCena() + "(" + bilet.getKlasaBiletu() + "kl.)"+", Data odlotu: " + bilet.getDataOdlotu());
                }

                return this;
            }
        });
    }

    private void zaladujOpinieDoJScrollPane() {

        // Pobierz wszystkie opinie pasażera z bazy danych
        List<String> opinie = pobierzOpinieZBazy();

        // Utwórz model dla JList
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String opinia : opinie) {
            listModel.addElement(opinia);
        }

        // Utwórz JList i przypisz model
        JList<String> opinieList = new JList<>(listModel);

        // Utwórz JScrollPane i dodaj JList do niego
        JScrollPane scrollPane = new JScrollPane(opinieList);

        // Dodaj JScrollPane do odpowiedniego komponentu w Twoim GUI (np. opiniefield)
        opiniefield.setViewportView(scrollPane);
    }

    private List<String> pobierzOpinieZBazy() {
        List<String> opinie = new ArrayList<>();

        try {
            DatabaseConnector dbConnector = new DatabaseConnector();
            Connection connection = dbConnector.connect();

            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT imie, opinia FROM opinie_w_aplikacji");

                while (resultSet.next()) {
                    String imie = resultSet.getString("imie");
                    String opinia = resultSet.getString("opinia");
                    String pelnaOpinia = imie + ": " + opinia;
                    opinie.add(pelnaOpinia);
                }

                dbConnector.closeConnection(connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Obsługa błędów SQL
        }

        return opinie;
    }

    private void zaladujhistorielotowDoJScrollPane() {

        List<String> historialotow = pobierzhistorielotowZBazy();

        // Utwórz model dla JList
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String historia : historialotow) {
            listModel.addElement(historia);
        }

        // Utwórz JList i przypisz model
        JList<String> historiaList = new JList<>(listModel);

        // Utwórz JScrollPane i dodaj JList do niego
        JScrollPane scrollPane = new JScrollPane(historiaList);

        // Dodaj JScrollPane do odpowiedniego komponentu w Twoim GUI (np. opiniefield)
        historialotowfield.setViewportView(scrollPane);
    }


    private List<String> pobierzhistorielotowZBazy() {
        List<String> historia = new ArrayList<>();

        try {
            DatabaseConnector dbConnector = new DatabaseConnector();
            Connection connection = dbConnector.connect();

            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT data_odlotu, lotnisko_wylotu, lotnisko_docelowe, waga_bagazu FROM historia_lotow");

                while (resultSet.next()) {
                    String data = resultSet.getString("data_odlotu");
                    String lotnisko_start = resultSet.getString("lotnisko_wylotu");
                    String lotnisko_stop = resultSet.getString("lotnisko_docelowe");
                    float waga_bagazu = resultSet.getFloat("waga_bagazu");
                    String pelnahistoria = lotnisko_start + "->" + lotnisko_stop + ", " +data +", " + waga_bagazu + "kg";
                    historia.add(pelnahistoria);
                }

                dbConnector.closeConnection(connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Obsługa błędów SQL
        }

        return historia;
    }
}
