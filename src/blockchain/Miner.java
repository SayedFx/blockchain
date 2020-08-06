package blockchain;

import blockchain.util.Utils;

import java.util.Random;
import java.util.concurrent.Callable;

public class Miner implements Callable<Object> {
    private static Random random;
    private final int id;
    private final BlockChain chain = BlockChain.getInstance();

    public Miner(int id) {
        this.id = id;
    }

    @Override
    public Object call() {
        generateBlock();
        return null;
    }

    private static int getMagic() {
        if (random == null) random = new Random();
        return random.nextInt();
    }

    private void generateBlock() {
        try {

            Block block = chain.getNextBlock("miner" + id);
            String hashInput = block.getHashInput();

            long millis = System.currentTimeMillis();

            int magic = getMagic();
            String hash = Utils.getHash(hashInput + magic);
            while (!hash.startsWith(chain.getDifficulty())) {
                magic = getMagic();
                hash = Utils.getHash(hashInput + magic);
            }

            millis = (System.currentTimeMillis() - millis);

            block.setGenerationDurationInSeconds((int) (millis / 1000));
            block.setHash(hash);
            block.setMagic(magic);
            block.setMiner("miner" + id);
            if (!chain.accept(block)) generateBlock();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
