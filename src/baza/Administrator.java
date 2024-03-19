package baza;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Administrator {
    private static final String ADMIN_LOGIN = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    public boolean login(String username, String password) {
        return ADMIN_LOGIN.equals(username) && ADMIN_PASSWORD.equals(password);
    }

    public List<BiletNaLot> pobierzLoty(String lotniskoStartowe, String lotniskoDocelowe) {
        List<BiletNaLot> loty = new ArrayList<>();

        DatabaseConnector dbConnector = new DatabaseConnector();
        Connection connection = dbConnector.connect();

        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM bilet_na_lot WHERE lotnisko_start = ? AND lotnisko_stop = ?");
                statement.setString(1, lotniskoStartowe);
                statement.setString(2, lotniskoDocelowe);

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int idBiletu = resultSet.getInt("id_biletu");
                    int klasaBiletu = resultSet.getInt("klasa_biletu");
                    int cena = resultSet.getInt("cena");
                    LocalDate dataOdlotu = resultSet.getDate("data_odlotu").toLocalDate();
                    LocalDate przewidywanaDataPrzylotu = resultSet.getDate("przewidywana_data_przylotu").toLocalDate();
                    int samolotId = resultSet.getInt("samolot_id");
                    String lotniskoStart = resultSet.getString("lotnisko_start");
                    String lotniskoStop = resultSet.getString("lotnisko_stop");

                    BiletNaLot bilet = new BiletNaLot(idBiletu, klasaBiletu, cena, dataOdlotu, przewidywanaDataPrzylotu, samolotId, lotniskoStart, lotniskoStop);
                    loty.add(bilet);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                dbConnector.closeConnection(connection);
            }
        }

        return loty;
    }


    public class Samolot {
        private int samolotId;
        private LocalDate rokProdukcji;
        private String nazwaLiniiLotniczej;
        private boolean klasaEkonomiczna=true;
        private boolean klasaBiznesowa=true;
        private boolean klasaPierwsza=true;
        private int pojemnoscLudzi;
        private int pojemnoscBagazy;  // w kg

        public Samolot(){

        }
        public Samolot(int samolotId, LocalDate rokProdukcji, String nazwaLiniiLotniczej, boolean klasaEkonomiczna,
                       boolean klasaBiznesowa, boolean klasaPierwsza, int pojemnoscLudzi,  int pojemnoscBagazy) {
            this.samolotId = samolotId;
            this.rokProdukcji = rokProdukcji;
            this.nazwaLiniiLotniczej = nazwaLiniiLotniczej;
            this.klasaEkonomiczna = klasaEkonomiczna;
            this.klasaBiznesowa = klasaBiznesowa;
            this.klasaPierwsza = klasaPierwsza;
            this.pojemnoscLudzi = pojemnoscLudzi;
            this.pojemnoscBagazy = pojemnoscBagazy;
        }

        public Status insertSamolot(LocalDate rokProdukcji, String nazwaLiniiLotniczej, int pojemnoscLudzi, int pojemnoscBagazy, String lotnisko_postoju, int pas_postoju_samolotu) {
            DatabaseConnector dbConnector = new DatabaseConnector();
            Connection connection = dbConnector.connect();

            if (connection == null) {
                return Status.CONNECTION_ERROR;
            }

            try {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO samolot (rok_produkcji, nazwa_lini_lotniczej, pojemnosc_ludzi, pojemnosc_bagazy, lotnisko_postoju, pas_postoju_samolotu) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                statement.setDate(1, Date.valueOf(rokProdukcji));
                statement.setString(2, nazwaLiniiLotniczej);
//                statement.setBoolean(3, klasaEkonomiczna);
//                statement.setBoolean(4, klasaBiznesowa);
//                statement.setBoolean(5, klasaPierwsza);
                statement.setInt(3, pojemnoscLudzi);
                statement.setInt(4, pojemnoscBagazy);
                statement.setString(5, lotnisko_postoju);
                statement.setInt(6, pas_postoju_samolotu);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    // Jeśli wstawiono przynajmniej jeden rekord
                    System.out.println("Samolot dodany pomyślnie.");
                } else {
                    // Jeśli nie wstawiono żadnego rekordu
                    System.out.println("Błąd podczas dodawania samolotu.");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                // obsługa błędu SQL
            } finally {
                dbConnector.closeConnection(connection);
            }
            return Status.SUCCESS;
        }

        public void updateSamolot(DatabaseConnector dbConnector) {
            Connection connection = dbConnector.connect();
            if (connection == null) {
                // obsługa błędu połączenia
                return;
            }

            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE samolot SET rok_produkcji = ?, nazwa_lini_lotniczej = ?, klasa_ekonomiczna = ?, klasa_biznesowa = ?, klasa_pierwsza = ?, pojemnosc_ludzi = ?, pojemnosc_bagazy = ? WHERE samolot_id = ?");
                statement.setDate(1, Date.valueOf(rokProdukcji));
                statement.setString(2, nazwaLiniiLotniczej);
                statement.setBoolean(3, klasaEkonomiczna);
                statement.setBoolean(4, klasaBiznesowa);
                statement.setBoolean(5, klasaPierwsza);
                statement.setInt(6, pojemnoscLudzi);
                statement.setInt(7, pojemnoscBagazy);
                statement.setInt(8, samolotId);

                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                // obsługa błędu SQL
            } finally {
                dbConnector.closeConnection(connection);
            }
        }
    }

    public class Lotnisko {
        private String nazwaLotniska;
        private String adresLotniska;

        public Lotnisko(){}
        public Lotnisko(String nazwaLotniska, String adresLotniska) {
            this.nazwaLotniska = nazwaLotniska;
            this.adresLotniska = adresLotniska;
        }

        public Status setAll(String nazwaLotniska, String adresLotniska) {
            DatabaseConnector dbConnector = new DatabaseConnector();
            Connection connection = dbConnector.connect();

            if (connection == null) {
                return Status.CONNECTION_ERROR;
            }

            try {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO lotnisko (nazwa_lotniska, adres_lotniska) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, nazwaLotniska);
                statement.setString(2, adresLotniska);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    // Jeśli wstawiono przynajmniej jeden rekord
                    return Status.SUCCESS;
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return Status.handleSQLState(e.getSQLState());
            } finally {
                dbConnector.closeConnection(connection);
            }
            return Status.SUCCESS;
        }
        public void updateLotnisko(DatabaseConnector dbConnector) {
            Connection connection = dbConnector.connect();
            if (connection == null) {
                // obsługa błędu połączenia
                return;
            }

            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE lotnisko SET nazwa_lotniska = ?, adres_lotniska = ?  WHERE nazwa_lotniska = ?");
                statement.setString(1, nazwaLotniska);
                statement.setString(2, adresLotniska);

                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                // obsługa błędu SQL
            } finally {
                dbConnector.closeConnection(connection);
            }
        }
    }

    public class BiletNaLot {
        private int idBiletu;
        private int klasaBiletu;
        private int cena;
        private LocalDate dataOdlotu;
        private LocalDate przewidywanaDataPrzylotu;
        private int samolotId;
        private String lotniskoStart;
        private String lotniskoStop;


        BiletNaLot(){}
        public BiletNaLot(int idBiletu, int klasaBiletu, int cena, LocalDate dataOdlotu,
                          LocalDate przewidywanaDataPrzylotu, int samolotId, String lotniskoStart, String lotniskoStop) {
            this.idBiletu = idBiletu;
            this.klasaBiletu = klasaBiletu;
            this.cena = cena;
            this.dataOdlotu = dataOdlotu;
            this.przewidywanaDataPrzylotu = przewidywanaDataPrzylotu;
            this.samolotId = samolotId;
            this.lotniskoStart = lotniskoStart;
            this.lotniskoStop = lotniskoStop;
        }

        public void setid(int id){
            this.idBiletu = id;
        }
        public void setlotniskostart(String lotnisko){
            this.lotniskoStart = lotnisko;
        }
        public void setlotniskostop(String lotnisko){
            this.lotniskoStop = lotnisko;
        }
        public int getIdBiletu() {
            return idBiletu;
        }
        public int getKlasaBiletu() {
            return klasaBiletu;
        }
        public int getCena() {
            return cena;
        }
        public LocalDate getDataOdlotu() {
            return dataOdlotu;
        }
        public LocalDate getPrzewidywanaDataPrzylotu() {
            return przewidywanaDataPrzylotu;
        }
        public int getSamolotId() {
            return samolotId;
        }
        public String getLotniskoStart() {
            return lotniskoStart;
        }
        public String getLotniskoStop() {
            return lotniskoStop;
        }
        public Status setAll(int klasaBiletu, int cena, LocalDate dataOdlotu, LocalDate przewidywanaDataPrzylotu,
                             int samolotId, String lotniskoStart, String lotniskoStop) {

            try {
                DatabaseConnector dbConnector = new DatabaseConnector();
                Connection connection = dbConnector.connect();

                if (connection != null) {

                    // Utwórz PreparedStatement z opcją RETURN_GENERATED_KEYS
                    PreparedStatement statement = connection.prepareStatement("INSERT INTO bilet_na_lot " +
                            "(klasa_biletu, cena, data_odlotu, przewidywana_data_przylotu, samolot_id, lotnisko_start, lotnisko_stop) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

                    statement.setInt(1, klasaBiletu);
                    statement.setInt(2, cena);
                    statement.setDate(3, Date.valueOf(dataOdlotu));
                    statement.setDate(4, Date.valueOf(przewidywanaDataPrzylotu));
                    statement.setInt(5, samolotId);
                    statement.setString(6, lotniskoStart);
                    statement.setString(7, lotniskoStop);

                    // Wykonaj INSERT i pobierz klucz generowany automatycznie
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        ResultSet generatedKeys = statement.getGeneratedKeys();

                        if (generatedKeys.next()) {
                            // Ustaw ID biletu i inne informacje
                            int generatedId = generatedKeys.getInt(1);
                            setid(generatedId);
                            setlotniskostart(lotniskoStart);
                            setlotniskostop(lotniskoStop);

                            System.out.println("Bilet na lot dodany pomyślnie.");
                        }
                    } else {
                        System.out.println("Błąd podczas dodawania biletu na lot.");
                    }

                    dbConnector.closeConnection(connection);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                return Status.handleSQLState(e.getSQLState());
            }
            return Status.SUCCESS;


        }


        public void updateBiletNaLot(DatabaseConnector dbConnector) {
            Connection connection = dbConnector.connect();
            if (connection == null) {
                // obsługa błędu połączenia
                return;
            }

            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE bilet_na_lot SET klasa_biletu = ?, cena = ?, data_odlotu = ?, przewidywana_data_przylotu = ?, samolot_id = ?, lotnisko_start = ?, lotnisko_stop = ? WHERE id_biletu = ?");
                statement.setInt(1, klasaBiletu);
                statement.setInt(2, cena);
                statement.setDate(3, Date.valueOf(dataOdlotu));
                statement.setDate(4, Date.valueOf(przewidywanaDataPrzylotu));
                statement.setInt(5, samolotId);
                statement.setString(6, lotniskoStart);
                statement.setString(7, lotniskoStop);
                statement.setInt(8, idBiletu);

                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                // obsługa błędu SQL
            } finally {
                dbConnector.closeConnection(connection);
            }
        }
    }

    public void dodajSamolot(int samolotId, LocalDate rokProdukcji, String nazwaLiniiLotniczej, boolean klasaEkonomiczna,
                             boolean klasaBiznesowa, boolean klasaPierwsza, int pojemnoscLudzi, int pojemnoscBagazy) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        Connection connection = dbConnector.connect();
        if (connection == null) {
            // obsługa błędu połączenia
            return;
        }

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO samolot VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, samolotId);
            statement.setDate(2, Date.valueOf(rokProdukcji));
            statement.setString(3, nazwaLiniiLotniczej);
            statement.setBoolean(4, klasaEkonomiczna);
            statement.setBoolean(5, klasaBiznesowa);
            statement.setBoolean(6, klasaPierwsza);
            statement.setInt(7, pojemnoscLudzi);
            statement.setInt(8, pojemnoscBagazy);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // obsługa błędu SQL
        } finally {
            dbConnector.closeConnection(connection);
        }
    }

    public void usunSamolot(int samolotId) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        Connection connection = dbConnector.connect();
        if (connection == null) {
            // obsługa błędu połączenia
            return;
        }

        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM samolot WHERE samolot_id = ?");
            statement.setInt(1, samolotId);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // obsługa błędu SQL
        } finally {
            dbConnector.closeConnection(connection);
        }
    }

    public void dodajLotnisko(String nazwaLotniska, int samolotId, int pasPostojuSamolotu) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        Connection connection = dbConnector.connect();
        if (connection == null) {
            // obsługa błędu połączenia
            return;
        }

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO lotnisko VALUES (?, ?, ?)");
            statement.setString(1, nazwaLotniska);
            statement.setInt(2, samolotId);
            statement.setInt(3, pasPostojuSamolotu);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // obsługa błędu SQL
        } finally {
            dbConnector.closeConnection(connection);
        }
    }

    public void usunLotnisko(String nazwaLotniska) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        Connection connection = dbConnector.connect();
        if (connection == null) {
            // obsługa błędu połączenia
            return;
        }

        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM lotnisko WHERE nazwa_lotniska = ?");
            statement.setString(1, nazwaLotniska);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // obsługa błędu SQL
        } finally {
            dbConnector.closeConnection(connection);
        }
    }

    public void dodajBilet(int idBiletu, int klasaBiletu, int cena, LocalDate dataOdlotu,
                           LocalDate przewidywanaDataPrzylotu, int samolotId, String lotniskoStart, String lotniskoStop) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        Connection connection = dbConnector.connect();
        if (connection == null) {
            // obsługa błędu połączenia
            return;
        }

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO bilet_na_lot VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, idBiletu);
            statement.setInt(2, klasaBiletu);
            statement.setInt(3, cena);
            statement.setDate(4, Date.valueOf(dataOdlotu));
            statement.setDate(5, Date.valueOf(przewidywanaDataPrzylotu));
            statement.setInt(6, samolotId);
            statement.setString(7, lotniskoStart);
            statement.setString(8, lotniskoStop);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // obsługa błędu SQL
        } finally {
            dbConnector.closeConnection(connection);
        }
    }

    public void usunBilet(int idBiletu) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        Connection connection = dbConnector.connect();
        if (connection == null) {
            // obsługa błędu połączenia
            return;
        }

        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM bilet_na_lot WHERE id_biletu = ?");
            statement.setInt(1, idBiletu);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // obsługa błędu SQL
        } finally {
            dbConnector.closeConnection(connection);
        }
    }

    public static class PasazerBilet {
        private int id_biletu;
        private int id_pasazera;
        private int id_pasazer_bilet;

        public void setId_biletu(int id){
            id_biletu = id;
        }
        public void setId_pasazera(int id){
            id_pasazera = id;
        }
        public void setId_pasazer_bilet(int id){
            id_pasazer_bilet = id;
        }

        public int getId_pasazer_bilet(){
            return id_pasazer_bilet;
        }
        public void insertPasazerBilet() {
            DatabaseConnector dbConnector = new DatabaseConnector();
            Connection connection = dbConnector.connect();
            if (connection == null) {
                // obsługa błędu połączenia
                return;
            }

            try {
                // Ustawienie opcji, aby otrzymać wygenerowane klucze
                PreparedStatement statement = connection.prepareStatement("INSERT INTO pasazer_bilet (pasazer_id, id_biletu) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, id_pasazera);
                statement.setInt(2, id_biletu);

                // Wykonaj operację wstawiania
                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    // Pobierz wygenerowane klucze
                    ResultSet generatedKeys = statement.getGeneratedKeys();

                    if (generatedKeys.next()) {
                        int id_pas_bilet = generatedKeys.getInt(1);
                        setId_pasazer_bilet(id_pas_bilet);
                    } else {
                        // Obsługa błędu braku wygenerowanego klucza
                        System.out.println("Błąd podczas uzyskiwania klucza głównego.");
                    }
                } else {
                    // Obsługa błędu braku wstawionego wiersza
                    System.out.println("Błąd podczas wstawiania wiersza do tabeli pasazer_bilet.");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                // obsługa błędu SQL
            } finally {
                dbConnector.closeConnection(connection);
            }
        }

    }

}
