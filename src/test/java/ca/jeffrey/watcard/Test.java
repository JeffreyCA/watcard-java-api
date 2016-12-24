package ca.jeffrey.watcard;


public class Test {
    public static void main (String[] args) {
        WatAccount account = new WatAccount("xxxxxxxx", "xxxx");
        account.login();
        account.loadBalances();
        account.displayBalances();
    }
}
