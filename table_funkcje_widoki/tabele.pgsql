CREATE TABLE lotnisko(
    nazwa_lotniska VARCHAR(32) PRIMARY KEY NOT NULL,
    adres_lotniska VARCHAR(64) NOT NULL;
);

CREATE TABLE samolot(
    samolot_id SERIAL PRIMARY KEY,
    rok_produkcji DATE,
    nazwa_lini_lotniczej VARCHAR(32),
    pojemnosc_ludzi INTEGER,
    pojemnosc_bagazy INTEGER, 
    lotnisko_postoju VARCHAR(64),
    Foreign Key (lotnisko_postoju) REFERENCES lotnisko(nazwa_lotniska),
    pas_postoju_samolotu INTEGER
);


CREATE TABLE pasazer(
    pasazer_id SERIAL PRIMARY KEY,
    imie VARCHAR(32) NOT NULL,
    nazwisko VARCHAR(32) NOT NULL,
    pesel VARCHAR(11) NOT NULL,
    data_urodzenia DATE NOT NULL,
    plec BOOLEAN,
    numer_telefonu VARCHAR(9),
    email VARCHAR,  
    login VARCHAR,
    haslo VARCHAR
);


CREATE TABLE bilet_na_lot(
    id_biletu SERIAL PRIMARY KEY,
    klasa_biletu INTEGER CHECK (klasa_biletu IN (1, 2, 3)),
    cena INTEGER,
    data_odlotu DATE,
    przewidywana_data_przylotu DATE,
    samolot_id INTEGER,
    lotnisko_start VARCHAR(64),
    lotnisko_stop VARCHAR(64),
    FOREIGN KEY (samolot_id) REFERENCES samolot(samolot_id),
    FOREIGN KEY (lotnisko_start) REFERENCES lotnisko(nazwa_lotniska),
    FOREIGN KEY (lotnisko_stop) REFERENCES lotnisko(nazwa_lotniska)
); 

 
CREATE TABLE pasazer_bilet (
    id_pasazer_bilet SERIAL PRIMARY KEY,
    pasazer_id INTEGER,
    id_biletu INTEGER,
    waga_bagazu NUMERIC(5,2),
    FOREIGN KEY (pasazer_id) REFERENCES pasazer(pasazer_id),
    FOREIGN KEY (id_biletu) REFERENCES bilet_na_lot(id_biletu)
);

CREATE TABLE opinie_pasazerow(
    id_opinii SERIAL PRIMARY KEY,
    pasazer_id INTEGER,
    opinia TEXT,
    FOREIGN KEY (pasazer_id) REFERENCES pasazer(pasazer_id)
);