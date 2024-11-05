package io.example.tests;

import io.example.proxy.AnalyticsProxyFactory;
import io.example.fixtures.UserService;
import io.example.fixtures.UserServiceImpl;
import io.example.mocks.MockAnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnalyticsProxyTest {

    private UserService userService;
    private MockAnalyticsService mockAnalytics;

    @BeforeEach
    void setUp() {
        mockAnalytics = new MockAnalyticsService();
        AnalyticsProxyFactory factory = new AnalyticsProxyFactory(mockAnalytics);
        userService = factory.create(UserService.class, new UserServiceImpl());
    }

    @Test
    @DisplayName("Should track login event with correct parameters")
    void shouldTrackLoginEvent() {
        // When
        userService.login("testUser");

        // Then
        List<MockAnalyticsService.TrackedEvent> events = mockAnalytics.getTrackedEvents();
        assertEquals(1, events.size());
        assertEquals("user.login", events.getFirst().getEventName());
        assertEquals("testUser", events.getFirst().getParams()[0]);
    }

    @Test
    @DisplayName("Should track multiple events in correct order")
    void shouldTrackMultipleEvents() {
        // When
        userService.login("user1");
        userService.getProfile("123");
        userService.logout("user1");

        // Then
        List<MockAnalyticsService.TrackedEvent> events = mockAnalytics.getTrackedEvents();
        assertEquals(3, events.size());
        assertEquals("user.login", events.get(0).getEventName());
        assertEquals("user.profile.view", events.get(1).getEventName());
        assertEquals("user.logout", events.get(2).getEventName());
    }

    @Test
    @DisplayName("Should handle null parameters correctly")
    void shouldHandleNullParameters() {
        // When
        userService.login(null);

        // Then
        List<MockAnalyticsService.TrackedEvent> events = mockAnalytics.getTrackedEvents();
        assertEquals(1, events.size());
        assertNull(events.getFirst().getParams()[0]);
    }
}
