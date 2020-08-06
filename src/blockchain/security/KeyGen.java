package blockchain.security;

import java.security.*;

public class KeyGen {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public KeyGen() {
        KeyPairGenerator generator = getGenerator();
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    private KeyPairGenerator getGenerator() {
        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to get RSA generator.\n" + e.getMessage());
        }
        return generator;
    }
}
