package blockchain.util;

import blockchain.Transaction;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MockTransactionGenerator {
    private final static List<String> transactors = IntStream.rangeClosed(1, 15).mapToObj(value -> "miner" + value).collect(Collectors.toList());
    private static final Random random;

    static {
        transactors.addAll(List.of("Nick", "Bob", "Alice", "ShoeShop", "FastFood", "CarShop"));
        random = new Random();
    }

    public static Transaction generate() {
        int bound = transactors.size() - 1;
        String payer = transactors.get(random.nextInt(bound));
        String payee = transactors.get(random.nextInt(bound));
        while (payee.equals(payer)) {
            payee = transactors.get(random.nextInt(bound));
        }
        double amount = random.nextInt(100);

        return new Transaction(payer, amount, payee);
    }
}
