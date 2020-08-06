package blockchain;

import blockchain.security.KeyGen;
import blockchain.security.MessageCodec;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class BlockChain {
    private static final AtomicInteger id = new AtomicInteger(1);
    private static final BlockChain instance = new BlockChain();
    private final List<Block> blocks;
    private final AtomicInteger difficultyFactor = new AtomicInteger();
    private final Deque<List<Message>> transactionQueue;
    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private volatile String difficulty = "";
    private volatile String prevHash = "0";


    private BlockChain() {
        blocks = new ArrayList<>();
        transactionQueue = new ArrayDeque<>();
        transactionQueue.offerLast(new ArrayList<>());
        KeyGen keyGen = new KeyGen();
        privateKey = keyGen.getPrivateKey();
        publicKey = keyGen.getPublicKey();
    }

    public static BlockChain getInstance() {
        return instance;
    }


    public int getNextID() {
        return id.get();
    }


    public void submitTransaction(Transaction transaction) {
        boolean isValidTransaction = isValid(transaction);
        if (isValidTransaction) {
            queueTransaction(transaction);
        }
    }

    private void queueTransaction(Transaction transaction) {
        Message message = new Message(transaction);
        message.setSignature(MessageCodec.sign(message.toString(), privateKey));
        message.setPublicKey(publicKey);
        List<Message> messages = transactionQueue.peekLast();
        if (messages == null || messages.size() >= 5) {
            messages = new ArrayList<>();
            transactionQueue.offerLast(messages);
        }
        messages.add(message);
    }

    private boolean isValid(Transaction transaction) {
        return getBalance(transaction) - transaction.getAmount() >= 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        blocks.forEach(block -> sb.append(block.toString()).append("\n\n"));
        return sb.toString();
    }

    public String getDifficulty() {
        return difficulty;
    }

    public synchronized boolean accept(Block block) {
        boolean validBlock = isValidBlock(block);
        if (validBlock) {
            transactionQueue.offerLast(new ArrayList<>());
            transactionQueue.pollFirst();
            updatePrevHash(block);
            id.incrementAndGet();
            updateDifficultyFactor(block);
            block.setDifficultyMessage(difficultyFactor.get());
            blocks.add(block);
        }
        return validBlock;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public Block getNextBlock(String miner) {
        Block block = new Block(getPrevHash());
        assert transactionQueue.peekFirst() != null;
        List<Message> transactions = new ArrayList<>(transactionQueue.peekFirst());
        Message message = new Message(new Transaction("mined", 100, miner));
        message.setPublicKey(publicKey);
        message.setSignature(MessageCodec.sign(message.toString(),privateKey));
        transactions.add(0, message);
        block.addTransactions(transactions);
        return block;
    }

    public void addMessages(List<Message> messages) {
        transactionQueue.offerLast(messages);
    }

    private double getBalance(Transaction transaction) {
        return getCredits(transaction) - getDebits(transaction);
    }

    private double getCredits(Transaction transaction) {
        return blocks.stream().mapToDouble(block ->
                block.getTransactions().stream()
                        .map(Message::getTransaction)
                        .filter(t -> t.getPayer().equals(transaction.getPayee()))
                        .mapToDouble(Transaction::getAmount)
                        .sum())
                .sum();
    }

    private double getDebits(Transaction transaction) {
        return blocks.stream().mapToDouble(block ->
                block.getTransactions().stream()
                        .map(Message::getTransaction)
                        .filter(t -> t.getPayer().equals(transaction.getPayer()))
                        .mapToDouble(Transaction::getAmount)
                        .sum())
                .sum();
    }

    private void updateDifficultyFactor(Block block) {
        if (block.getGenerationDurationInSeconds() < 15) {
            increaseDifficulty();
        } else if (block.getGenerationDurationInSeconds() > 60 && difficultyFactor.get() > 0) {
            decreaseDifficulty();
        }
    }

    private void decreaseDifficulty() {
        difficulty = "0".repeat(difficultyFactor.decrementAndGet());
    }

    private void increaseDifficulty() {
        difficulty = "0".repeat(difficultyFactor.incrementAndGet());
    }


    private void updatePrevHash(Block block) {
        prevHash = block.getHash();
    }

    private boolean isValidBlock(Block block) {
        return hasPrevHash(block) && matchesDifficulty(block) && isMessageSignatureValid(block);
    }

    private boolean matchesDifficulty(Block block) {
        return block.getHash().startsWith(getDifficulty());
    }

    private boolean hasPrevHash(Block block) {
        return block.getHashInput().startsWith(getPrevHash());
    }

    private boolean isMessageSignatureValid(Block block) {
        List<Message> data = block.getTransactions();
        if (data == null || data.isEmpty()) return true;
        return data.stream().allMatch(MessageCodec::verify);
    }

}
