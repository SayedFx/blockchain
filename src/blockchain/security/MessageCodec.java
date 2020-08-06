package blockchain.security;

import blockchain.Message;

import java.security.*;

public class MessageCodec {
    public static byte[] sign(String message, PrivateKey key) {
        try {

            Signature rsa = Signature.getInstance("SHA1withRSA");
            rsa.initSign(key);
            rsa.update(message.getBytes());
            return rsa.sign();

        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException("Failed to sign message" + e.getMessage());
        }
    }

    public static boolean verify(Message message) {
        try {

            Signature rsa = Signature.getInstance("SHA1withRSA");
            rsa.initVerify(message.getPublicKey());
            rsa.update(message.getMessage());
            return rsa.verify(message.getSignature());

        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException("Failed to verify message" + e.getMessage());
        }
    }
}
