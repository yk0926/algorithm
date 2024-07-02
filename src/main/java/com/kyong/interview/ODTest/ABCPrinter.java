package com.kyong.interview.ODTest;

import java.util.concurrent.Semaphore;

/**
 * Description
 *
 * @author kyong
 * @version 1.0
 * @date 2024/7/2 14:31
 */
public class ABCPrinter {
    public static void main(String[] args) {
        ABCPrinter printer = new ABCPrinter(2);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                printer.printA();
            }
        }, "Thread A");
        Thread t2 = new Thread(printer::printB, "Thread B");
        Thread t3 = new Thread(printer::printC, "Thread C");

        t1.start();
        t3.start();
        t2.start();
    }
    private final int max;
    // 从线程 A 开始执行
    private final Semaphore semaphoreA = new Semaphore(1);
    private final Semaphore semaphoreB = new Semaphore(0);
    private final Semaphore semaphoreC = new Semaphore(0);

    public ABCPrinter(int max) {
        this.max = max;
    }

    public void printA() {
        print("A", semaphoreA, semaphoreB);
    }

    public void printB() {
        print("B", semaphoreB, semaphoreC);
    }

    public void printC() {
        print("C", semaphoreC, semaphoreA);
    }

    private void print(String alphabet, Semaphore currentSemaphore, Semaphore nextSemaphore) {
        for (int i = 1; i <= max; i++) {
            try {
                currentSemaphore.acquire();
                System.out.println(Thread.currentThread().getName() + " : " + alphabet);
                // 传递信号给下一个线程
                nextSemaphore.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
