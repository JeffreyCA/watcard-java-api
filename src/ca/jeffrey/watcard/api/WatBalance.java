package ca.jeffrey.watcard.api;


public class WatBalance {

    // Fields
    private String id;
    private String name;
    private double limit;
    private double value;

    /**
     * Constructor
     *
     * @param id balance name
     * @param name balance type
     * @param limit limit of balance type
     * @param value balance
     */
    public WatBalance(String id, String name, double limit, double value) {
        this.id = id;
        this.name = name;
        this.limit = limit;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s %s: $%.2f [Limit: $%.2f]", id, name, value, limit);
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
