package org.spat.wf.mvc.toolbox.monitor;

public class RequestStats {

    private final long currentTime = System.currentTimeMillis();

    private final int concurrentRequest;
    private final long totalTime;
    private final int totalCount;

    public RequestStats(int concurrentRequest, long totalTime, int totalCount) {
        this.concurrentRequest = concurrentRequest;
        this.totalTime = totalTime;
        this.totalCount = totalCount;
    }

    public int getConcurrentRequest() {
        return concurrentRequest;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public long getCurrentTime() {
        return currentTime;
    }
}