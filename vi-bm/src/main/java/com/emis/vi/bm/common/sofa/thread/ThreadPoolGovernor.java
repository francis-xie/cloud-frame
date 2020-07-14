/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.emis.vi.bm.common.sofa.thread;

import com.emis.vi.bm.common.sofa.thread.NamedThreadFactory;
import com.emis.vi.bm.common.sofa.thread.SofaThreadPoolExecutor;
import com.emis.vi.bm.common.sofa.thread.log.ThreadLogger;
import com.alipay.sofa.common.utils.StringUtil;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author <a href="mailto:guaner.zzx@alipay.com">Alaneuler</a>
 * Created on 2020/3/17
 */
public class ThreadPoolGovernor {
    public static String CLASS_NAME         = com.emis.vi.bm.common.sofa.thread.ThreadPoolGovernor.class
                                                                                        .getCanonicalName();
    private static long                                          period             = 30;
    private static boolean                                       loggable           = false;

    private static ScheduledExecutorService scheduler          = Executors
                                                                                        .newScheduledThreadPool(
                                                                                            1,
                                                                                            new NamedThreadFactory(
                                                                                                "t.p.g"));
    private static ScheduledFuture<?> scheduledFuture;
    private static final Object monitor            = new Object();
    private static GovernorInfoDumper                            governorInfoDumper = new GovernorInfoDumper();

    private static ConcurrentHashMap<String, ThreadPoolExecutor> registry           = new ConcurrentHashMap<String, ThreadPoolExecutor>();

    public synchronized static void startSchedule() {
        if (scheduledFuture == null) {
            scheduledFuture = scheduler.scheduleAtFixedRate(governorInfoDumper, period, period,
                TimeUnit.SECONDS);
            ThreadLogger.info("Started {} with period: {} SECONDS", CLASS_NAME, period);
        } else {
            ThreadLogger
                .warn("{} has already started with period: {} SECONDS.", CLASS_NAME, period);
        }
    }

    public synchronized static void stopSchedule() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            scheduledFuture = null;
            ThreadLogger.info("Stopped {}.", CLASS_NAME);
        } else {
            ThreadLogger.warn("{} is not scheduling!", CLASS_NAME);
        }
    }

    /**
     * Can also be used to manage JDK thread pool.
     * SofaThreadPoolExecutor should **not** call this method!
     * @param name thread pool name
     * @param threadPoolExecutor thread pool instance
     */
    public static void registerThreadPoolExecutor(String name, ThreadPoolExecutor threadPoolExecutor) {
        if (StringUtil.isEmpty(name)) {
            ThreadLogger.error("Rejected registering request of instance {} with empty name: {}.",
                threadPoolExecutor, name);
            return;
        }

        ThreadPoolExecutor existingExecutor = registry.putIfAbsent(name, threadPoolExecutor);
        if (existingExecutor != null) {
            ThreadLogger.error(
                "Rejected registering request of instance {} with duplicate name: {}",
                threadPoolExecutor, name);
        } else {
            ThreadLogger.info("Thread pool with name '{}' registered", name);
        }
    }

    public static void registerThreadPoolExecutor(SofaThreadPoolExecutor threadPoolExecutor) {
        registerThreadPoolExecutor(threadPoolExecutor.getThreadPoolName(), threadPoolExecutor);
    }

    public static void unregisterThreadPoolExecutor(String name) {
        registry.remove(name);
        ThreadLogger.info("Thread pool with name '{}' unregistered", name);
    }

    public static ThreadPoolExecutor getThreadPoolExecutor(String name) {
        return registry.get(name);
    }

    static class GovernorInfoDumper implements Runnable {
        @Override
        public void run() {
            try {
                if (loggable) {
                    for (Map.Entry<String, ThreadPoolExecutor> entry : registry.entrySet()) {
                        ThreadLogger.info("Thread pool '{}' exists with instance: {}",
                            entry.getKey(), entry.getValue());
                    }
                }
            } catch (Throwable e) {
                ThreadLogger.warn("{} is interrupted when running: {}", this, e);
            }
        }
    }

    public static long getPeriod() {
        return period;
    }

    public static void setPeriod(long period) {
        com.emis.vi.bm.common.sofa.thread.ThreadPoolGovernor.period = period;

        synchronized (monitor) {
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
                scheduledFuture = scheduler.scheduleAtFixedRate(governorInfoDumper, period, period,
                    TimeUnit.SECONDS);
                ThreadLogger.info("Reschedule {} with period: {} SECONDS", CLASS_NAME, period);
            }
        }
    }

    public static boolean isLoggable() {
        return loggable;
    }

    public static void setLoggable(boolean loggable) {
        com.emis.vi.bm.common.sofa.thread.ThreadPoolGovernor.loggable = loggable;
    }
}
