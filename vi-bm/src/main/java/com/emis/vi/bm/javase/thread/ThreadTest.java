package com.emis.vi.bm.javase.thread;

import com.emis.vi.bm.javase.LoggingTest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.logging.Logger;

/**
 * 多线程是Java最基本的一种并发模型：https://www.liaoxuefeng.com/wiki/1252599548343744/1255943750561472
 * Java语言内置了多线程支持：一个Java程序实际上是一个JVM进程，JVM进程用一个主线程来执行main()方法，
 * 在main()方法内部，我们又可以启动多个线程。此外，JVM还有负责垃圾回收的其他工作线程等。
 * 因此，对于大多数Java程序来说，我们说多任务，实际上是说如何使用多线程实现多任务。
 * Java多线程编程的特点又在于：
 * 多线程模型是Java程序最基本的并发模型； 后续读写网络、数据库、Web开发等都依赖Java多线程模型。
 * Java线程的状态有以下几种：
 * New：新创建的线程，尚未执行；
 * Runnable：运行中的线程，正在执行run()方法的Java代码；
 * Blocked：运行中的线程，因为某些操作被阻塞而挂起；
 * Waiting：运行中的线程，因为某些操作在等待中；
 * Timed Waiting：运行中的线程，因为执行sleep()方法正在计时等待；
 * Terminated：线程已终止，因为run()方法执行完毕。
 */
public class ThreadTest {
    public static final Object lock = new Object(); //线程同步锁
    public static int count = 0;

    public static void main(String[] args) {
        Log log = LogFactory.getLog(LoggingTest.class);
        /*Java语言内置了多线程支持。当Java程序启动的时候，实际上是启动了一个JVM进程，
          然后，JVM启动主线程来执行main()方法。在main()方法中，我们又可以启动其他线程。*/
        log("main start...");
        //实例化一个Thread实例，然后调用它的start()方法：
        Thread t = new Thread(); //通过实例变量t来表示这个新线程对象，并开始执行。
        t.start(); // 启动新线程，这个线程启动后实际上什么也不做就立刻结束了。

        //我们希望新线程能执行指定的代码
        //方法一：从Thread派生一个自定义类，然后覆写run()方法：
        MyThread t1 = new MyThread(); //Thread t1 = new MyThread();
        //守护线程是指为其他线程服务的线程。在JVM中，所有非守护线程都执行完毕后，无论有没有守护线程，虚拟机都会自动退出。
        t1.setDaemon(true); //标记为守护线程；守护线程不能持有需要关闭的资源（如打开文件等）。
        t1.start(); // 启动新线程，start()方法会在内部自动调用实例的run()方法
        //或者简写为：
        Thread t2 = new Thread() {
            public void run() {
                try {
                    //一个线程还可以等待另一个线程直到其运行结束。可以通过t1.join()等待t1线程结束后再继续运行：
                    //如果t1线程已经结束，对实例t1调用join()会立刻返回。
                    //此外，join(long)的重载方法也可以指定一个等待时间，超过等待时间后就不再继续等待。
                    t1.join(); //join就是指等待该线程结束，然后才继续往下执行自身线程。
                } catch (InterruptedException e) {
                    log.error(e, e);
                }
                log("thread t2 run...");
                //要模拟并发执行的效果，我们可以在线程中调用Thread.sleep()，强迫当前线程暂停一段时间：
                try {
                    Thread.sleep(10); //sleep()传入的参数是毫秒。
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i=0; i<10000; i++) {
                    synchronized(ThreadTest.lock) {
                        ThreadTest.count -= 1;
                    }
                }
                System.out.println("thread t2 end.");
            }
        };
        t2.start();
        //可以对线程设定优先级，优先级高的线程被操作系统调度的优先级较高，操作系统对高优先级线程可能调度更频繁，
        //但我们决不能通过设置优先级来确保高优先级的线程一定会先执行。
        t2.setPriority(10); // 1~10, 默认值5
        try {
            Thread.sleep(20); //sleep()传入的参数是毫秒。
            t1.interrupt(); // 中断t1线程，interrupt()方法仅仅向t1线程发出了“中断请求”，至于t1线程是否能立刻响应，要看具体代码。
            //t1.running = false; // 还可以设置标志位置为false中断线程
            t1.join(); // 等待t1线程结束
        } catch (InterruptedException e) {
            log.error(e, e);
            //如果线程处于等待状态，例如，t1.join()会让main线程进入等待状态，此时，如果对main线程调用interrupt()，
            //join()方法会立刻抛出InterruptedException，因此，目标线程只要捕获到join()方法抛出的InterruptedException，
            //就说明有其他线程对其调用了interrupt()方法，通常情况下该线程应该立刻结束运行。
        }
        /*简述：这里我们用main线程表示主线程，
          在main线程里面创建Thread对象，调用start()启动新线程。当start()方法被调用时，JVM就创建了一个新线程。
          而这新线程在main线程执行的同时会并发执行，当run()方法结束时，新线程就结束了。而main()方法结束时，主线程也结束了。
          从这新线程开始运行以后，两个线程就开始同时运行了，并且由操作系统调度，程序本身无法确定线程的调度顺序。
        */

        //方法二：创建Thread实例时，传入一个Runnable实例：
        Thread t3 = new Thread(new MyRunnable());
        t3.start(); // 启动新线程
        //或者用Java8引入的lambda语法进一步简写为：
        Thread t4 = new Thread(() -> {
            System.out.println("start new thread t4：Runnable to Java8:lambda!");
        });
        t4.start(); // 启动新线程

        log("main end...");
    }

    static void log(String s) {
        System.out.println(Thread.currentThread().getName() + ": " + s);
    }
}

class MyThread extends Thread {
    /*线程间共享变量需要使用volatile关键字标记，确保每个线程都能读取到更新后的变量值。
      volatile关键字的目的是告诉虚拟机：每次访问变量时，总是获取主内存的最新值；每次修改变量后，立刻回写到主内存。
      volatile关键字解决的是可见性问题：当一个线程修改了某个共享变量的值，其他线程能够立刻看到修改后的值。
    */
    public volatile boolean running = true;

    @Override
    public void run() {
        System.out.println("start new thread t1：MyThread!");
        int n = 0;
        while (!isInterrupted()) { // 在t1线程的while循环中检测isInterrupted()，线程是否有中断了
            //while (running) { //设置标志位来标识线程是否应该继续运行
            n++;
            System.out.println(n + " hello!");
        }
        for (int i = 0; i < 10000; i++) {
            /*通过加锁和解锁的操作，就能保证指令总是在一个线程执行期间，不会有其他线程会进入此指令区间。
              即使在执行期线程被操作系统中断执行，其他线程也会因为无法获得锁导致无法进入此指令区间。
              只有执行线程将锁释放后，其他线程才有机会获得锁并执行。
              这种加锁和解锁之间的代码块我们称之为临界区（Critical Section），任何时候临界区最多只有一个线程能执行。
              保证一段代码的原子性就是通过加锁和解锁实现的。Java程序使用synchronized关键字对一个对象进行加锁
             */
            synchronized (ThreadTest.lock) {
                ThreadTest.count += 1;
            }
        }
        System.out.println("end new thread t1：MyThread!");
    }
}

class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("start new thread t3：MyRunnable!");
    }
}