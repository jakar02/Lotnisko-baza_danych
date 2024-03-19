package baza;

public enum Status {
    CONNECTION_ERROR("Problem z połączeniem"),
    USERNAME_TAKEN("Nazwa użytkownika zajęta"),
    EMPTY_FIELD("Wypełnij wszystkie pola"),
    NAME_BAD_FORMAT("Imię może zawierać tylko litery"),
    SURNAME_BAD_FORMAT("Nazwisko może zawierać tylko litery"),
    PHONE_BAD_FORMAT("Numer telefonu może zawierać tylko cyfry"),
    EMAIL_BAD_FORMAT("Niepoprawny adres email"),
    EQUIPMENT_NOT_AVAILABLE("Sprzęt niedostępny"),
    SUCCESS("Sukces"),
    INVALID_DATE_FORMAT("Błędny format daty"),
    FUTURE_DATE("Rok produkcji nie może być z przyszłości"),
    NAME_LENGTH_EXCEEDED("Zbyt długa nazwa lotniska"),
    INVALID_TICKET_CLASS("Klasa biletu musi być z opcji 1/2/3"),
    NEGATIVE_PRICE("Cena biletu nie może być ujemna"),
    INCORRECT_DATE_ORDER("Błędne daty. Data przylotu musi być późniejsza niż data odlotu"),
    ALREADY_EXSIST("Lotnisko o tej nazwie juz istnieje"),
    BAGAZ_NOT_FOUND("Bagaz nieznaleziony w bazie");


    private final String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Status handleSQLState(String errorCode) {
        switch (errorCode) {
            case "00001":
                return USERNAME_TAKEN;
            case "00002":
                return EMPTY_FIELD;
            case "00003":
                return EMAIL_BAD_FORMAT;
            case "00004":
                return PHONE_BAD_FORMAT;
            case "00005":
                return NAME_BAD_FORMAT;
            case "00006":
                return SURNAME_BAD_FORMAT;
            case "00007":
                return INVALID_DATE_FORMAT;
            case "00008":
                return FUTURE_DATE;
            case "00009":
                return EQUIPMENT_NOT_AVAILABLE;
            case "00010":
                return INCORRECT_DATE_ORDER;
            case "00011":
                return NAME_LENGTH_EXCEEDED;
            case "00012":
                return INVALID_TICKET_CLASS;
            case "00013":
                return NEGATIVE_PRICE;
            case "00014":
                return ALREADY_EXSIST;

            default:
                return CONNECTION_ERROR;
        }
    }
}
