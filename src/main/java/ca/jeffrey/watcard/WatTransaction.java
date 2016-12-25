package ca.jeffrey.watcard;


import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public class WatTransaction {

    // Date format to be passed in URL
    protected static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd+HH:mm:ss");
    // Base transactions URL
    protected static final String BASE_URL = "https://watcard.uwaterloo.ca/OneWeb/Financial/TransactionsPass";

    // Fields
    private LocalDateTime dateTime;
    private double amount;
    private String account;
    private int unit;
    private String type;
    private String terminal;

    /**
     * Constructor
     *
     * @param dateTime a LocalDateTime instance
     * @param amount transaction amount
     * @param account account used to make transaction
     * @param unit unit
     * @param type type of transaction
     * @param terminal location of transaction
     */
    public WatTransaction(LocalDateTime dateTime, double amount, String account, int unit, String type, String terminal) {
        this.dateTime = dateTime;
        this.amount = amount;
        this.account = account;
        this.unit = unit;
        this.type = type;
        this.terminal = terminal;
    }

    @Override
    public String toString() {
        return String.format("Date: %s%nAmount: $%.2f%nAccount: %s%nUnit: %d%nType: %s%nTerminal: %s",
                dateTime.toString(), amount, account, unit, type, terminal);
    }

    // Getters and setters
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }
}
