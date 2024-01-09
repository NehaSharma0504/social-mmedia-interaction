package eca.learnings.socialmedia.interaction.utils;

import org.junit.jupiter.api.Test;

public class RateLimiterTest {

    @Test
    public void rateLimiterTest() {
        int maxRequestsPerSecond = 5; // Set the maximum number of requests per second
        int totalRequests = 10; // Total number of requests to simulate

        RateLimiter rateLimiter = new RateLimiter(maxRequestsPerSecond, 2); // 2 permits per second

        // Simulate multiple requests
        for (int i = 0; i < totalRequests; i++) {
            new RequestThread(i, rateLimiter).start();
        }
    }

    // Helper thread class to simulate requests
    static class RequestThread extends Thread {
        private int requestId;
        private RateLimiter rateLimiter;

        public RequestThread(int requestId, RateLimiter rateLimiter) {
            this.requestId = requestId;
            this.rateLimiter = rateLimiter;
        }

        @Override
        public void run() {
            System.out.println("Request " + requestId + " waiting...");
            rateLimiter.processRequest(2); // Acquire 2 permits for each request
            System.out.println("Request " + requestId + " processed.");
            rateLimiter.releasePermits(2); // Release 2 permits after processing the request
        }
    }
}

