CREATE OR REPLACE FUNCTION sprawdz_rejestracje_pasazera()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.login IS NULL OR NEW.login = '' OR NEW.haslo IS NULL OR NEW.haslo = '' OR NEW.imie IS NULL OR NEW.imie = '' OR NEW.nazwisko IS NULL OR NEW.nazwisko = '' OR NEW.numer_telefonu IS NULL OR NEW.numer_telefonu = '' OR NEW.email IS NULL OR NEW.email = '' THEN
        RAISE EXCEPTION 'Pola nie mogą być puste' USING ERRCODE = '00002';
    END IF;

    IF NOT NEW.email ~ '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$' THEN
        RAISE EXCEPTION 'Niepoprawny format adresu email' USING ERRCODE = '00003';
    END IF;

    IF NOT NEW.numer_telefonu ~ '^[0-9]+$' THEN 
        RAISE EXCEPTION 'Niepoprawny format numeru telefonu' USING ERRCODE = '00004';
    END IF;

    IF NOT NEW.imie ~ '^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]+$' THEN
        RAISE EXCEPTION 'Niepoprawny format imienia' USING ERRCODE = '00005';
    END IF;

    IF NOT NEW.nazwisko ~ '^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]+$' THEN
        RAISE EXCEPTION 'Niepoprawny format nazwiska' USING ERRCODE = '00006';
    END IF;

    IF NOT NEW.data_urodzenia::text ~ '^\d{4}-\d{2}-\d{2}$' THEN
        RAISE EXCEPTION 'Błędny format daty. Poprawny format to YYYY-MM-DD.' USING ERRCODE = '00007';
    END IF;

    IF EXISTS (SELECT 1 FROM pasazer WHERE login = NEW.login) THEN
        RAISE EXCEPTION 'Nazwa użytkownika zajęta' USING ERRCODE = '00001';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER rejestracja_pasazera
BEFORE INSERT ON pasazer
FOR EACH ROW
EXECUTE FUNCTION sprawdz_rejestracje_pasazera();



CREATE OR REPLACE FUNCTION sprawdz_samolot()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.rok_produkcji IS NULL OR NEW.nazwa_lini_lotniczej IS NULL OR NEW.nazwa_lini_lotniczej = '' OR NEW.pojemnosc_ludzi IS NULL OR NEW.pojemnosc_bagazy IS NULL THEN
        RAISE EXCEPTION 'Pola nie mogą być puste' USING ERRCODE = '00002';
    END IF;

    IF NOT NEW.nazwa_lini_lotniczej ~ '^[a-zA-Z0-9._%+ -]+$' THEN
        RAISE EXCEPTION 'Niepoprawny format nazwy linii lotniczej' USING ERRCODE = '00003';
    END IF;

    IF NEW.rok_produkcji > current_date THEN
        RAISE EXCEPTION 'Rok produkcji nie może być z przyszłości' USING ERRCODE = '00008';
    END IF;

    IF NOT NEW.rok_produkcji::text ~ '^\d{4}-\d{2}-\d{2}$' THEN
        RAISE EXCEPTION 'Błędny format daty. Poprawny format to YYYY-MM-DD.' USING ERRCODE = '00007';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;



CREATE TRIGGER samolot_trig
BEFORE INSERT ON samolot
FOR EACH ROW
EXECUTE FUNCTION sprawdz_samolot();


CREATE TRIGGER zmiana_danych_pasazer
BEFORE UPDATE ON pasazer
FOR EACH ROW
EXECUTE FUNCTION sprawdz_rejestracje_pasazera();

CREATE TRIGGER zmiana_danych_samolot
BEFORE UPDATE ON samolot
FOR EACH ROW
EXECUTE FUNCTION sprawdz_samolot();


CREATE OR REPLACE FUNCTION sprawdz_nazwe_lotniska()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.nazwa_lotniska IS NULL OR NEW.nazwa_lotniska = '' THEN
        RAISE EXCEPTION 'Pola nie mogą być puste' USING ERRCODE = '00002';
    END IF;

    IF NOT NEW.nazwa_lotniska ~ '^[a-zA-Z0-9._%+ -]+$' THEN
        RAISE EXCEPTION 'Niepoprawny format nazwy lotniska' USING ERRCODE = '00003';
    END IF;

    IF length(NEW.nazwa_lotniska) > 64 THEN
        RAISE EXCEPTION 'Zbyt długa nazwa lotniska. Maksymalna długość to 64 znaki' USING ERRCODE = '00011';
    END IF;

    IF EXISTS (SELECT 1 FROM lotnisko WHERE nazwa_lotniska = NEW.nazwa_lotniska) THEN
        RAISE EXCEPTION 'Lotnisko o podanej nazwie już istnieje' USING ERRCODE = '00014';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;



CREATE TRIGGER lotnisko_trig
BEFORE INSERT ON lotnisko
FOR EACH ROW
EXECUTE FUNCTION sprawdz_nazwe_lotniska();

CREATE TRIGGER zmiana_danych_lotnisko
BEFORE UPDATE ON lotnisko
FOR EACH ROW
EXECUTE FUNCTION sprawdz_nazwe_lotniska();









-- Funkcja sprawdzająca poprawność danych dla biletu na lot
CREATE OR REPLACE FUNCTION sprawdz_bilet_na_lot()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.klasa_biletu IS NULL OR NEW.cena IS NULL OR NEW.data_odlotu IS NULL OR NEW.przewidywana_data_przylotu IS NULL OR NEW.samolot_id IS NULL OR NEW.lotnisko_start IS NULL OR NEW.lotnisko_stop IS NULL THEN
        RAISE EXCEPTION 'Pola nie mogą być puste' USING ERRCODE = '00002';
    END IF;

    IF NOT NEW.data_odlotu::text ~ '^\d{4}-\d{2}-\d{2}$' THEN
        RAISE EXCEPTION 'Błędny format daty. Poprawny format to YYYY-MM-DD.' USING ERRCODE = '00007';
    END IF;

    IF NOT NEW.przewidywana_data_przylotu::text ~ '^\d{4}-\d{2}-\d{2}$' THEN
        RAISE EXCEPTION 'Błędny format daty. Poprawny format to YYYY-MM-DD.' USING ERRCODE = '00007';
    END IF;

    IF NEW.klasa_biletu NOT IN (1, 2, 3) THEN
        RAISE EXCEPTION 'Klasa biletu musi być z opcji 1/2/3' USING ERRCODE = '00012';
    END IF;

    IF NEW.cena < 0 THEN
        RAISE EXCEPTION 'Cena biletu nie może być ujemna!' USING ERRCODE = '00013';
    END IF;

    IF NEW.data_odlotu >= NEW.przewidywana_data_przylotu THEN
        RAISE EXCEPTION 'Błędne daty. Data przylotu musi być późniejsza niż data odlotu' USING ERRCODE = '00010';
    END IF;

    IF NEW.data_odlotu < current_date THEN
        RAISE EXCEPTION 'Błędna data. Data odlotu nie może być z przeszłości' USING ERRCODE = '00008';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


-- Trigger przed wstawieniem do tabeli bilet_na_lot
CREATE TRIGGER bilet_na_lot_trig
BEFORE INSERT ON bilet_na_lot
FOR EACH ROW
EXECUTE FUNCTION sprawdz_bilet_na_lot();

-- Trigger przed aktualizacją danych w tabeli bilet_na_lot
CREATE TRIGGER bilet_na_lot_trig_update
BEFORE UPDATE ON bilet_na_lot
FOR EACH ROW
EXECUTE FUNCTION sprawdz_bilet_na_lot();