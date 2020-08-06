package blockchain;

public class Transaction {
    private final String payer;
    private final String payee;
    private final double amount;

    public Transaction(String payer, double amount, String payee) {
        this.payer = payer;
        this.payee = payee;
        this.amount = amount;
    }

    public String getPayer() {
        return payer;
    }

    public String getPayee() {
        return payee;
    }

    public double getAmount() {
        return amount;
    }
}
