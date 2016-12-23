package ca.jeffrey.watcard.api;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WatTransaction {
    protected static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd+HH:mm:ss");
    protected static final String BASE_URL = "https://watcard.uwaterloo.ca/OneWeb/Financial/TransactionsPass";

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

    @Override
    public String toString() {
        return String.format("Date: %s%nAmount: $%.2f%nAccount: %s%nUnit: %d%nType: %s%nTerminal: %s",
                date_time.toString(), amount, account, unit, type, terminal);
    }
}
