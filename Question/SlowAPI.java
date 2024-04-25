package ques;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


//to: sharath.m@mystifly.com

public class SlowAPI {

    /**
     * Don't change this method.
     */

    public static int getSum(List<Integer> value) {

        //volatile instance
        int sum = 0;
//10
        //size/8 -> exec
        //10M
        for (int i : value) {

            sum += i;

            try {

                Thread.sleep(1);

            } catch (InterruptedException e) {

                throw new RuntimeException(e);

            }

        }

        return sum;

    }

    public static void main(String[] args) throws InterruptedException {

        List<Integer> list = IntStream.rangeClosed(1, 10000)

                .boxed()

                .map(n -> ThreadLocalRandom.current().nextInt(0, 11))

                .collect(Collectors.toList());

        long start1 = System.currentTimeMillis();

        //int sum1 = SlowAPI.getSum(list);
        int sum1 = invokeSlowAPI(list);

        System.out.println("Executed in : " + (System.currentTimeMillis() - start1) + ", sum is : " + sum1);

    }
//Early: Executed in : 14825, sum is : 50459
//Now: Executed in : 1367, sum is : 50160

    private static int invokeSlowAPI(List<Integer> list) throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(10);
        CopyOnWriteArrayList<Integer> cowal = new CopyOnWriteArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        List<List<Integer>> lists = splitList(list, 1000);
        lists.forEach(batch -> {
            executorService.submit(() -> {
                cowal.add(SlowAPI.getSum(batch));
                latch.countDown();
            });
        });

        latch.await(100, TimeUnit.SECONDS); // Wait for completion
        return cowal.stream().mapToInt(Integer::intValue).sum();
    }

    public static <T> List<List<T>> splitList(List<T> list, int chunkSize) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("Chunk size must be greater than zero.");
        }

        int numOfChunks = (list.size() + chunkSize - 1) / chunkSize;

        return IntStream.range(0, numOfChunks)
                .mapToObj(i -> list.subList(i * chunkSize, Math.min((i + 1) * chunkSize, list.size())))
                .collect(Collectors.toList());
    }

}