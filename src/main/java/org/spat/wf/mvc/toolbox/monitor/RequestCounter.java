package org.spat.wf.mvc.toolbox.monitor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class RequestCounter {

    public static RequestCounter instance() {
        return SingletonHolder.instance;
    }

    protected RequestCounter() {}

    private final AtomicInteger counter = new AtomicInteger(0);


    private volatile long totalTime = 0;    //总时间
    private volatile int totalCount = 0;  //总请求数

    public int increment() {
        return counter.incrementAndGet();
    }

    public RequestStats decrement(long time) {

        return new RequestStats(
            counter.decrementAndGet(),
            totalTime += time,
            totalCount++
        );
    }

    public RequestStats getCurrentState() {
        return new RequestStats(
                counter.get(),
                totalTime,
                totalCount
        );
    }


    static class SingletonHolder {
        static RequestCounter instance = new RequestCounter();
    }

}
