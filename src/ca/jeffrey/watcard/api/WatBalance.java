package ca.jeffrey.watcard.api;

/**
 * Created by Jeffrey on 2016-12-19.
 */
public class WatBalance {

    private int id;
    private String name;
    private double limit;
    private double value;

    public WatBalance(int id, String name, double limit, double value) {
        this.id = id;
        this.name = name;
        this.limit = limit;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
