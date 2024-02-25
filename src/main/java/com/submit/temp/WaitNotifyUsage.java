package com.submit.temp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaitNotifyUsage {

    private int count = 0;

    public void produceMessage() throws InterruptedException {

        while (true) {
            synchronized (this) {
                while (count == 5) {
                    System.out.println("count == 5 , wait ....");
                    wait();
                }
                count++;
                System.out.println("produce count {}" + count);
                Thread.sleep(1000);
                notify();
            }
        }
    }

    public void consumeMessage() throws InterruptedException {

        while (true) {
            synchronized (this) {
                while (count == 0) {
                    System.out.println("count == 0, wait ...");
                    wait();
                }
                System.out.println("consume count {}" + count);
                count--;
                Thread.sleep(1000);
                notify();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        WaitNotifyUsage waitNotifyUsage = new WaitNotifyUsage();

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.submit(() -> {
            try {
                waitNotifyUsage.produceMessage();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        executorService.submit(() -> {
            try {
                waitNotifyUsage.consumeMessage();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread.sleep(50000);
    }
}
