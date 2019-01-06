package com.alibaba.druid.lierl;

import com.alibaba.druid.util.Utils;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadTraceTest {

    private final static AtomicInteger dataSourceIdSeed         = new AtomicInteger(0);

    public static void main(String[] args) {
        String trace = Utils.toString(Thread.currentThread().getStackTrace());
        System.out.println(trace);

        System.out.println(dataSourceIdSeed.getAndIncrement());
        System.out.println(dataSourceIdSeed.getAndIncrement());
    }
}
