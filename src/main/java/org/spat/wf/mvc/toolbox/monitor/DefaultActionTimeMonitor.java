package org.spat.wf.mvc.toolbox.monitor;


public class DefaultActionTimeMonitor implements ActionTimeMonitor {

    private final long time = System.currentTimeMillis();
    private static RequestCounter requestCounter = RequestCounter.instance();
    private static TimeoutStats timeoutStats = TimeoutStats.instance();


    public DefaultActionTimeMonitor() {
        
    	requestCounter.increment();
        
    }

    @Override
    public void post() {
        long interval = System.currentTimeMillis() - time;

        RequestStats requestStats = requestCounter.decrement(interval);

        timeoutStats.log(interval, requestStats);

    }
}
