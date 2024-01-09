package eca.learnings.socialmedia.interaction.utils;

import java.util.concurrent.Semaphore;

public class RateLimiter {
    private Semaphore semaphore;

    public RateLimiter(int maxRequestsPerSecond) {
        this.semaphore = new Semaphore(maxRequestsPerSecond);
    }

    // New constructor to set the number of permits
    public RateLimiter(int maxRequestsPerSecond, int permits) {
        this.semaphore = new Semaphore(maxRequestsPerSecond * permits);
    }

    // Acquire multiple permits
    public void processRequest(int permits) {
        try {
            semaphore.acquire(permits);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Release multiple permits
    public void releasePermits(int permits) {
        semaphore.release(permits);
    }
}

