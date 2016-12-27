package ca.jeffrey.watcard;


public class Test {
    public static void main (String[] args) {
        WatAccount account = new WatAccount("xxxxxxxx", "xxx");

        try {
            account.login();
        }
        catch (IllegalArgumentException e) {
            // Invalid login information
        }

        account.loadBalances();
        account.displayBalances();
        account.loadPersonalInfo();
    }
}
