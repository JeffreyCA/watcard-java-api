package ca.jeffrey.watcard;


public class WatBalance {

    // Fields
    private String id;
    private String name;
    private float limit;
    private float value;

    /**
     * Constructor
     *
     * @param id balance name
     * @param name balance type
     * @param limit limit of balance type
     * @param value balance
     */
    public WatBalance(String id, String name, float limit, float value) {
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

    public float getLimit() {
        return limit;
    }

    public void setLimit(float limit) {
        this.limit = limit;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
