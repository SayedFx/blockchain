package blockchain;

import blockchain.util.MockTransactionGenerator;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        BlockChain instance = BlockChain.getInstance();
        var transactionSubmitter = Executors.newScheduledThreadPool(1);
        transactionSubmitter.scheduleAtFixedRate(() -> instance.submitTransaction(MockTransactionGenerator.generate()), 100, 100, TimeUnit.MILLISECONDS);
        Collection<Callable<Object>> miningTasks = IntStream.range(1, 7).mapToObj(Miner::new).collect(Collectors.toList());
        executorService.invokeAll(miningTasks);
        transactionSubmitter.shutdownNow();
        executorService.shutdown();

        System.out.println(instance.toString());
    }


}
