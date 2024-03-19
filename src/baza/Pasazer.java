package baza;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pasazer {

    private int id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private String phone;

    private boolean gender;
    private LocalDate birthDate;
    private String pesel;

    public boolean login(String username, String password) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        Connection connection = dbConnector.connect();
        if (connection == null) {
            return false;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT pasazer_id, imie, nazwisko, pesel, data_urodzenia, plec, numer_telefonu, email FROM pasazer WHERE login = ? AND haslo = ?");
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return false;
            }
            setId(resultSet.getInt("pasazer_id"));
            setName(resultSet.getString("imie"));
            setSurname(resultSet.getString("nazwisko"));
            setPesel(resultSet.getString("pesel"));
            setBirthDate(resultSet.getDate("data_urodzenia").toLocalDate());
            setGender(resultSet.getBoolean("plec"));
            setPhone(resultSet.getString("numer_telefonu"));
            setEmail(resultSet.getString("email"));

            setUsername(username);
            setPassword(password);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            dbConnector.closeConnection(connection);
        }
        return true;
    }

    public Status register(String username, String password, String name, String surname, String email, String phone,
                           boolean gender, LocalDate birthDate, String pesel) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        Connection connection = dbConnector.connect();
        if (connection == null) {
            return Status.CONNECTION_ERROR;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO pasazer (login, haslo, imie, nazwisko, pesel, data_urodzenia, plec, numer_telefonu, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, name);
            statement.setString(4, surname);
            statement.setString(5, pesel);
            statement.setDate(6, Date.valueOf(birthDate));
            statement.setBoolean(7, gender);
            statement.setString(8, phone);
            statement.setString(9, email);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return Status.handleSQLState(e.getSQLState());
        } finally {
            dbConnector.closeConnection(connection);
        }
        return Status.SUCCESS;
    }

    // Metody set i get dla nowych pól

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    // Pozostałe metody set i get

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Status updateInfo(String name, String surname, String email, String phone, boolean gender, LocalDate birthDate, String pesel) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        Connection connection = dbConnector.connect();
        if (connection == null) {
            return Status.CONNECTION_ERROR;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE pasazer SET imie = ?, nazwisko = ?, pesel = ?, data_urodzenia = ?, plec = ?, numer_telefonu = ?, email = ? WHERE pasazer_id = ?");
            statement.setString(1, name);
            statement.setString(2, surname);
            statement.setString(3, pesel);
            statement.setDate(4, Date.valueOf(birthDate));
            statement.setBoolean(5, gender);
            statement.setString(6, phone);
            statement.setString(7, email);
            statement.setInt(8, getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return Status.handleSQLState(e.getSQLState());
        } finally {
            dbConnector.closeConnection(connection);
        }
        return Status.SUCCESS;
    }

    public class Bilet {
        public int id;
        public int klasaBiletu;
        public int cena;
        public LocalDate dataOdlotu;
        public LocalDate przewidywanaDataPrzylotu;
        public int samolotId;
        public String lotniskoStart;
        public String lotniskoStop;

        public Bilet(){

        }
        public Bilet(int id, int klasaBiletu, int cena, LocalDate dataOdlotu, LocalDate przewidywanaDataPrzylotu, int samolotId, String lotniskoStart, String lotniskoStop) {
            this.id = id;
            this.klasaBiletu = klasaBiletu;
            this.cena = cena;
            this.dataOdlotu = dataOdlotu;
            this.przewidywanaDataPrzylotu = przewidywanaDataPrzylotu;
            this.samolotId = samolotId;
            this.lotniskoStart = lotniskoStart;
            this.lotniskoStop = lotniskoStop;
        }


        public Status setAll(int klasaBiletu, int cena, LocalDate dataOdlotu, LocalDate przewidywanaDataPrzylotu, int samolotId, String lotniskoStart, String lotniskoStop) {
            DatabaseConnector dbConnector = new DatabaseConnector();
            Connection connection = dbConnector.connect();

            if (connection == null) {
                return Status.CONNECTION_ERROR;
            }

            try {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO bilet_na_lot (klasa_biletu, cena, data_odlotu, przewidywana_data_przylotu, samolot_id, lotnisko_start, lotnisko_stop) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, klasaBiletu);
                statement.setInt(2, cena);
                statement.setDate(3, Date.valueOf(dataOdlotu));
                statement.setDate(4, Date.valueOf(przewidywanaDataPrzylotu));
                statement.setInt(5, samolotId);
                statement.setString(6, lotniskoStart);
                statement.setString(7, lotniskoStop);

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return Status.handleSQLState(e.getSQLState());
            } finally {
                dbConnector.closeConnection(connection);
            }
            return Status.SUCCESS;
        }


        public String toString() {
            return "ID: " + id + " Klasa: " + klasaBiletu + " Cena: " + cena + " Data odlotu: " + dataOdlotu + " Data przylotu: " + przewidywanaDataPrzylotu +
                    " Samolot ID: " + samolotId + " Lotnisko start: " + lotniskoStart + " Lotnisko stop: " + lotniskoStop;
        }
    }

    public List<Bilet> getPasazerBilety() {
        DatabaseConnector dbConnector = new DatabaseConnector();
        Connection connection = dbConnector.connect();
        if (connection == null) {
            return null;
        }
        try {
            // Zmieniłem zapytanie, aby pobierało bilety za pomocą tabeli pasazer_bilet
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT pb.id_biletu, b.klasa_biletu, b.cena, b.data_odlotu, b.przewidywana_data_przylotu, b.samolot_id, b.lotnisko_start, b.lotnisko_stop " +
                            "FROM pasazer_bilet pb " +
                            "JOIN bilet_na_lot b ON pb.id_biletu = b.id_biletu " +
                            "WHERE pb.pasazer_id = ?"
            );
            statement.setInt(1, getId());

            ResultSet resultSet = statement.executeQuery();
            List<Bilet> bilety = new ArrayList<>();
            while (resultSet.next()) {
                bilety.add(new Bilet(
                        resultSet.getInt("id_biletu"),
                        resultSet.getInt("klasa_biletu"),
                        resultSet.getInt("cena"),
                        resultSet.getDate("data_odlotu").toLocalDate(),
                        resultSet.getDate("przewidywana_data_przylotu").toLocalDate(),
                        resultSet.getInt("samolot_id"),
                        resultSet.getString("lotnisko_start"),
                        resultSet.getString("lotnisko_stop")
                ));
            }
            return bilety;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            dbConnector.closeConnection(connection);
        }
    }

    public boolean makePayment(double amount) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        Connection connection = dbConnector.connect();
        if (connection == null) {
            return false;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO platnosci (pasazer_id, kwota, data) VALUES (?, ?, CURRENT_DATE)");
            statement.setInt(1, getId());
            statement.setDouble(2, amount);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            dbConnector.closeConnection(connection);
        }
    }

    public class Opinia {
        public int id;
        public String opinia;
        public int ocena;

        public Opinia(int id, String opinia, int ocena) {
            this.id = id;
            this.opinia = opinia;
            this.ocena = ocena;
        }

        public String toString() {
            return "ID: " + id + " Opinia: " + opinia + " Ocena: " + ocena;
        }
    }

    public List<Opinia> getPasazerOpinie() {
        DatabaseConnector dbConnector = new DatabaseConnector();
        Connection connection = dbConnector.connect();
        if (connection == null) {
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT id_opinii, opinia, ocena FROM opinie_pasazerow WHERE pasazer_id = ?");
            statement.setInt(1, getId());

            ResultSet resultSet = statement.executeQuery();
            List<Opinia> opinie = new ArrayList<>();
            while (resultSet.next()) {
                opinie.add(new Opinia(
                        resultSet.getInt("id_opinii"),
                        resultSet.getString("opinia"),
                        resultSet.getInt("ocena")
                ));
            }
            return opinie;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            dbConnector.closeConnection(connection);
        }
    }

    public class Bagaz {
        private int idBagazu;
        private double wagaBagazu;
        private String bagazLotniskoOddania;
        private String bagazLotniskoOdebrania;

        public Bagaz(int idBagazu, double wagaBagazu, String bagazLotniskoOddania, String bagazLotniskoOdebrania) {
            this.idBagazu = idBagazu;
            this.wagaBagazu = wagaBagazu;
            this.bagazLotniskoOddania = bagazLotniskoOddania;
            this.bagazLotniskoOdebrania = bagazLotniskoOdebrania;
        }

        public int getIdBagazu() {
            return idBagazu;
        }

        public double getWagaBagazu() {
            return wagaBagazu;
        }

        public String getBagazLotniskoOddania() {
            return bagazLotniskoOddania;
        }

        public String getBagazLotniskoOdebrania() {
            return bagazLotniskoOdebrania;
        }

        // Dodaj metody set, aby umożliwić aktualizację informacji o bagażu

        public void setWagaBagazu(double wagaBagazu) {
            this.wagaBagazu = wagaBagazu;
        }

        public void setBagazLotniskoOddania(String bagazLotniskoOddania) {
            this.bagazLotniskoOddania = bagazLotniskoOddania;
        }

        public void setBagazLotniskoOdebrania(String bagazLotniskoOdebrania) {
            this.bagazLotniskoOdebrania = bagazLotniskoOdebrania;
        }
    }

    // Dodaj metodę do obsługi operacji na bagażu

    public Status dodajBagaz(double wagaBagazu, String bagazLotniskoOddania, String bagazLotniskoOdebrania) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        Connection connection = dbConnector.connect();
        if (connection == null) {
            return Status.CONNECTION_ERROR;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO bagaz (pasazer_id, waga_bagazu, bagaz_lotnisko_oddania, bagaz_lotnisko_odebrania) VALUES (?, ?, ?, ?)");
            statement.setInt(1, getId());
            statement.setDouble(2, wagaBagazu);
            statement.setString(3, bagazLotniskoOddania);
            statement.setString(4, bagazLotniskoOdebrania);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return Status.handleSQLState(e.getSQLState());
        } finally {
            dbConnector.closeConnection(connection);
        }
        return Status.SUCCESS;
    }


    public List<Bagaz> getPasazerBagaze() {
        DatabaseConnector dbConnector = new DatabaseConnector();
        Connection connection = dbConnector.connect();
        if (connection == null) {
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT bagaz_id, waga_bagazu, bagaz_lotnisko_oddania, bagaz_lotnisko_odebrania FROM bagaz WHERE pasazer_id = ?");
            statement.setInt(1, getId());

            ResultSet resultSet = statement.executeQuery();
            List<Bagaz> bagaze = new ArrayList<>();
            while (resultSet.next()) {
                bagaze.add(new Bagaz(
                        resultSet.getInt("bagaz_id"),
                        resultSet.getDouble("waga_bagazu"),
                        resultSet.getString("bagaz_lotnisko_oddania"),
                        resultSet.getString("bagaz_lotnisko_odebrania")
                ));
            }
            return bagaze;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            dbConnector.closeConnection(connection);
        }
    }

    public Status aktualizujBagaz(int idBagazu, double wagaBagazu, String lotniskoOddania, String lotniskoOdebrania) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        Connection connection = dbConnector.connect();
        if (connection == null) {
            return Status.CONNECTION_ERROR;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE bagaz SET waga_bagazu = ?, bagaz_lotnisko_oddania = ?, bagaz_lotnisko_odebrania = ? WHERE bagaz_id = ? AND pasazer_id = ?");
            statement.setDouble(1, wagaBagazu);
            statement.setString(2, lotniskoOddania);
            statement.setString(3, lotniskoOdebrania);
            statement.setInt(4, idBagazu);
            statement.setInt(5, getId());

            int rowCount = statement.executeUpdate();
            if (rowCount == 0) {
                return Status.BAGAZ_NOT_FOUND;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return Status.handleSQLState(e.getSQLState());
        } finally {
            dbConnector.closeConnection(connection);
        }
        return Status.SUCCESS;
    }


}
