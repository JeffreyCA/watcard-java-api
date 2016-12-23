package ca.jeffrey.watcard.api;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WatTransaction {
    final protected static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd+HH:mm:ss");
    final protected static String BASE_URL = "https://watcard.uwaterloo.ca/OneWeb/Financial/TransactionsPass";

    private LocalDateTime date_time;
    private double amount;
    private String account;
    private int unit;
    private String type;
    private String terminal;

    public WatTransaction(LocalDateTime date_time, double amount, String account, int unit, String type, String terminal) {
        this.date_time = date_time;
        this.amount = amount;
        this.account = account;
        this.unit = unit;
        this.type = type;
        this.terminal = terminal;
    }

    // Sample request:
    // Base URL:   https://watcard.uwaterloo.ca/OneWeb/Financial/TransactionsPass
    // Parameters: dateFrom=2016%2F12%2F07+00%3A00%3A00
    //             dateTo=2016%2F12%2F21+23%3A59%3A59
    //             returnRows=0
    // Decoded
    // Parameters: dateFrom=2016/12/07+00:00:00
    //             dateTo=2016/12/21+23:59:59
    //             returnRows=0

    @Override
    public String toString() {
        return String.format("Date: %s%nAmount: $%.2f%nAccount: %s%nUnit: %d%nType: %s%nTerminal: %s",
        date_time.toString(), amount, account, unit, type, terminal);
    }
}
