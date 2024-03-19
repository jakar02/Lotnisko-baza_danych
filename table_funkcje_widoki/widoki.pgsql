CREATE VIEW opinie_w_aplikacji AS
SELECT
    p.imie,
    o.opinia
FROM pasazer p
JOIN opinie_pasazerow o ON p.pasazer_id = o.pasazer_id;


CREATE VIEW historia_lotow AS
SELECT
    pb.pasazer_id,
    b.data_odlotu,
    b.lotnisko_start as lotnisko_wylotu,
    b.lotnisko_stop as lotnisko_docelowe,
    pb.waga_bagazu
FROM bilet_na_lot b
JOIN pasazer_bilet pb ON b.id_biletu = pb.id_biletu;


CREATE view kupione_bilety_na_lot as
SELECT
b.id_biletu,
b.klasa_biletu,
b.lotnisko_start,
b.lotnisko_stop
b.data_odlotu,
COUNT(pb.id_pasazer_bilet) as kupione_bilety
FROM bilet_na_lot b
JOIN pasazer_bilet pb ON b.id_biletu = pb.id_biletu
GROUP BY b.id_biletu, b.klasa_biletu, b.lotnisko_start, b.lotnisko_stop, b.data_odlotu;