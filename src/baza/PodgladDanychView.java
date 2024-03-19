package baza;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
public class PodgladDanychView extends JFrame {
    private JScrollPane bazasamolotowfield;
    private JScrollPane kupionebiletynalotfield;
    private JScrollPane zarejestrowanipasazerowie;
    private JPanel MainPanel;
    private JButton powrotButton;


    public PodgladDanychView() {
        setContentPane(MainPanel);
        setTitle("Podgląd danych");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        powrotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminView();
                dispose();
            }
        });

        zaladujSamolotyDoJTable();
        zaladujPasazerowDoJTable();
        zaladujKupioneBiletyNaLotDoJTable();
    }

    private void zaladujKupioneBiletyNaLotDoJTable() {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID biletu");
        tableModel.addColumn("Klasa biletu");
        tableModel.addColumn("Lotnisko startowe");
        tableModel.addColumn("Lotnisko docelowe");
        tableModel.addColumn("Data odlotu");
        tableModel.addColumn("Liczba kupionych biletów");

        List<String[]> kupioneBilety = pobierzKupioneBiletyNaLotZBazy();

        for (String[] bilet : kupioneBilety) {
            tableModel.addRow(bilet);
        }

        JTable biletyTable = new JTable(tableModel);
        kupionebiletynalotfield.setViewportView(new JScrollPane(biletyTable));
    }

    private List<String[]> pobierzKupioneBiletyNaLotZBazy() {
        List<String[]> ret_data = new ArrayList<>();

        try {
            DatabaseConnector dbConnector = new DatabaseConnector();
            Connection connection = dbConnector.connect();

            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM kupione_bilety_na_lot");

                while (resultSet.next()) {
                    String idBiletu = resultSet.getString("id_biletu");
                    String klasaBiletu = resultSet.getString("klasa_biletu");
                    String lotniskoStart = resultSet.getString("lotnisko_start");
                    String lotniskoStop = resultSet.getString("lotnisko_stop");
                    String dataOdlotu = resultSet.getString("data_odlotu");
                    String liczbaKupionychBiletow = resultSet.getString("kupione_bilety");

                    String[] to_return = {idBiletu, klasaBiletu, lotniskoStart, lotniskoStop, dataOdlotu, liczbaKupionychBiletow};
                    ret_data.add(to_return);
                }

                dbConnector.closeConnection(connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Obsługa błędów SQL
        }

        return ret_data;
    }

    private void zaladujPasazerowDoJTable() {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Imię");
        tableModel.addColumn("Nazwisko");
        tableModel.addColumn("PESEL");
        tableModel.addColumn("Data urodzenia");
        tableModel.addColumn("Płeć");
        tableModel.addColumn("Numer telefonu");
        tableModel.addColumn("Email");
        tableModel.addColumn("Login");

        List<String[]> pasazerowie = pobierzPasazerowZBazy();

        for (String[] pasazer : pasazerowie) {
            tableModel.addRow(pasazer);
        }

        JTable pasazerTable = new JTable(tableModel);
        zarejestrowanipasazerowie.setViewportView(new JScrollPane(pasazerTable));
    }

    private void zaladujSamolotyDoJTable() {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Rok produkcji");
        tableModel.addColumn("Linia lotnicza");
//        tableModel.addColumn("Klasa ekonomiczna");
//        tableModel.addColumn("Klasa biznesowa");
//        tableModel.addColumn("Klasa pierwsza");
        tableModel.addColumn("Pojemność ludzi");
        tableModel.addColumn("Pojemność bagażu");
        tableModel.addColumn("Lotnisko postoju");

        List<String[]> samoloty = pobierzSamolotyZBazy();

        for (String[] samolot : samoloty) {
            tableModel.addRow(samolot);
        }

        JTable samolotTable = new JTable(tableModel);
        bazasamolotowfield.setViewportView(new JScrollPane(samolotTable));
    }
    private List<String[]> pobierzPasazerowZBazy() {
        List<String[]> ret_data = new ArrayList<>();

        try {
            DatabaseConnector dbConnector = new DatabaseConnector();
            Connection connection = dbConnector.connect();

            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM pasazer");

                while (resultSet.next()) {
                    String id = resultSet.getString("pasazer_id");
                    String imie = resultSet.getString("imie");
                    String nazwisko = resultSet.getString("nazwisko");
                    String pesel = resultSet.getString("pesel");
                    String dataUrodzenia = resultSet.getString("data_urodzenia");
                    String plec = resultSet.getString("plec");
                    String numerTelefonu = resultSet.getString("numer_telefonu");
                    String email = resultSet.getString("email");
                    String login = resultSet.getString("login");

                    String[] to_return = {id, imie, nazwisko, pesel, dataUrodzenia, plec, numerTelefonu, email, login};
                    ret_data.add(to_return);
                }

                dbConnector.closeConnection(connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Obsługa błędów SQL
        }

        return ret_data;
    }

    private List<String[]> pobierzSamolotyZBazy() {
        List<String[]> ret_data = new ArrayList<>();

        try {
            DatabaseConnector dbConnector = new DatabaseConnector();
            Connection connection = dbConnector.connect();

            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM samolot");

                while (resultSet.next()) {
                    String id = resultSet.getString("samolot_id");
                    String rokProdukcji = resultSet.getString("rok_produkcji");
                    String liniaLotnicza = resultSet.getString("nazwa_lini_lotniczej");
//                    String klasaEkonomiczna = resultSet.getString("klasa_ekonomiczna");
//                    String klasaBiznesowa = resultSet.getString("klasa_biznesowa");
//                    String klasaPierwsza = resultSet.getString("klasa_pierwsza");
                    String pojemnoscLudzi = resultSet.getString("pojemnosc_ludzi");
                    String pojemnoscBagazu = resultSet.getString("pojemnosc_bagazy");
                    String lotniskoPostoju = resultSet.getString("lotnisko_postoju");

                    String[] to_return = {id, rokProdukcji, liniaLotnicza, pojemnoscLudzi, pojemnoscBagazu, lotniskoPostoju};
                    ret_data.add(to_return);
                }

                dbConnector.closeConnection(connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Obsługa błędów SQL
        }

        return ret_data;
    }
}
