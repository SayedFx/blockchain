package blockchain;

import java.security.PublicKey;

public class Message {
    private final byte[] message;
    private final Transaction transaction;
    private byte[] signature;
    private PublicKey publicKey;

    public Message(Transaction transaction) {
        if (transaction.getPayer().equals("mined")) {
            message = String.format("%s gets %.0f VC", transaction.getPayee(), transaction.getAmount()).getBytes();
        } else {
            message = String.format("%s sent %.0f VC to %s"
                    , transaction.getPayer()
                    , transaction.getAmount()
                    , transaction.getPayee())
                    .getBytes();
        }
        this.transaction = transaction;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public byte[] getMessage() {
        return message;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return new String(message);
    }
}
