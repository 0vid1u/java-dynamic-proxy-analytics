package io.example.tests;

import io.example.proxy.AnalyticsProxyFactory;
import io.example.service.AnalyticsService;
import io.example.service.SimpleAnalyticsService;
import io.example.fixtures.UserService;
import io.example.fixtures.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnalyticsProxyPerformanceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        AnalyticsService analyticsService = new SimpleAnalyticsService();
        AnalyticsProxyFactory factory = new AnalyticsProxyFactory(analyticsService);
        userService = factory.create(UserService.class, new UserServiceImpl());
    }

    @Test
    @DisplayName("Should handle multiple concurrent requests")
    void shouldHandleConcurrentRequests() throws InterruptedException {
        // Given
        int numberOfThreads = 10;
        int requestsPerThread = 1000;
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);

        // When
        for (int i = 0; i < numberOfThreads; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < requestsPerThread; j++) {
                        userService.login("user" + j);
                        successCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            }).start();
        }

        // Then
        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertEquals(numberOfThreads * requestsPerThread, successCount.get());
    }
}
