package blockchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {
    private final String prevBlockHash;
    private final int blockID;
    private final long timeStamp;
    private String hash;
    private int magic;
    private int generationDurationInSeconds;
    private String difficultyMessage;
    private String miner;
    private final List<Message> transactions = new ArrayList<>();

    public Block(String prevBlockHash) {
        this.prevBlockHash = prevBlockHash;
        blockID = BlockChain.getInstance().getNextID();
        timeStamp = new Date().getTime();
    }

    @Override
    public String toString() {
        return "blockchain.Block:" +
                "\nCreated by: " + miner +
                "\n" + miner +" gets 100 VC"+
                "\nId: " + blockID +
                "\nTimestamp: " + timeStamp +
                "\nMagic number: " + magic +
                "\nHash of the previous block:\n" + prevBlockHash +
                "\nHash of the block: \n" + hash +
                "\nblockchain.Block data: " +
                getPrintableData() +
                "\nblockchain.Block was generating for " + generationDurationInSeconds + " seconds" +
                "\n" + difficultyMessage;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHashInput() {
        return prevBlockHash + blockID + timeStamp + transactions;
    }

    public void addTransactions(List<Message> transactions) {
        this.transactions.addAll(transactions);
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getGenerationDurationInSeconds() {
        return generationDurationInSeconds;
    }

    public void setGenerationDurationInSeconds(int generationDurationInSeconds) {
        this.generationDurationInSeconds = generationDurationInSeconds;
    }

    public void setDifficultyMessage(int difficultyFactor) {
        if (getGenerationDurationInSeconds() < 15) {
            difficultyMessage = "N was increased to " + difficultyFactor;
        } else if (getGenerationDurationInSeconds() > 60 && difficultyFactor > 0) {
            difficultyMessage = "N was decreased by 1 ";
        } else difficultyMessage = "N stays the same";
    }

    public void setMiner(String string) {
        this.miner = string;
    }

    public List<Message> getTransactions() {
        return transactions;
    }


    private String getPrintableData() {
        return transactions.stream().skip(1).map(string -> String.format("\n%s", string)).reduce(String::concat).orElse("no messages");
    }
}
