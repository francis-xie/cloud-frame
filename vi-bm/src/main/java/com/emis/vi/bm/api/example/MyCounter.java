package com.emis.vi.bm.api.example;

public class MyCounter {
    int counter;
    public synchronized void increase()  {
        counter++;
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
