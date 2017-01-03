package ca.jeffrey.watcard;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

public class WatTransaction implements Serializable {

    // Date format to be passed in URL
    protected static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd+HH:mm:ss");
    // Base transactions URL
    protected static final String BASE_URL = "https://watcard.uwaterloo.ca/OneWeb/Financial/TransactionsPass";

    // Fields
    private LocalDateTime dateTime;
    private float amount;
    private int account;
    private WatBalanceType balanceType;
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
    public WatTransaction(LocalDateTime dateTime, float amount, int account, int unit, String type, String terminal) {
        this.dateTime = dateTime;
        this.amount = amount;
        this.account = account;
        this.unit = unit;
        this.type = type;
        this.terminal = terminal;
        balanceType = determineBalanceType();
    }

    /**
     * Determines from what balance account funds were deducted from
     * @return WatBalanceType enum
     */
    private WatBalanceType determineBalanceType() {
        switch (account) {
            case 1:
                return WatBalanceType.VILLAGE_MEAL;
            case 2:
                return WatBalanceType.BEST_BUY_MEAL;
            case 3:
                return WatBalanceType.FOOD_PLAN;
            case 4:
                return WatBalanceType.FLEX1;
            case 5:
                return WatBalanceType.FLEX2;
            case 6:
                return WatBalanceType.FLEX3;
            case 7:
                return WatBalanceType.TRANSFER;
            case 8:
                return WatBalanceType.DON_MEAL;
            case 9:
                return WatBalanceType.DON_FLEX;
            case 10:
                return WatBalanceType.REWARDS;
            case 11:
                return WatBalanceType.DEPT_CHARGE;
            case 12:
                return WatBalanceType.OVERDRAFT;
            default:
                return WatBalanceType.OTHER;
        }
    }

    @Override
    public String toString() {
        return String.format(Locale.CANADA, "Date: %s%nAmount: $%.2f%nAccount: %s%nUnit: %d%nType: %s%nTerminal: %s",
                dateTime.toString(), amount, account, unit, type, terminal);
    }

    // Getters and setters
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    // Formatted time
    public String getTimeString() {
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("dd MMM 'at' h:mm a");
        return myFormat.format(dateTime);
    }

    private boolean isFlex() {
        return balanceType == WatBalanceType.FLEX1 || balanceType == WatBalanceType.FLEX2
                || balanceType == WatBalanceType.FLEX3;
    }

    private boolean isMeal() {
        return balanceType == WatBalanceType.VILLAGE_MEAL
                || balanceType == WatBalanceType.BEST_BUY_MEAL
                || balanceType == WatBalanceType.FOOD_PLAN
                || balanceType == WatBalanceType.DON_MEAL;
    }

    public String getAccountTypeString() {
        if (isFlex()) {
            return "Flex Dollars";
        }
        else if (isMeal()) {
            return "Meal Plan";
        }
        else {
            return "Other";
        }
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public float getAmount() {
        return amount;
    }

    public String getAmountString() {
        return NumberFormat.getCurrencyInstance(Locale.CANADA).format(amount);
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
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

    public String getCleanTerminal() {
        return terminal.replace("WAT-FS-", "").split(" : ")[1];
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }
}
