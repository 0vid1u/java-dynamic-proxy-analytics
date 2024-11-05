package io.example.tests;

import io.example.proxy.AnalyticsProxyFactory;
import io.example.service.AnalyticsService;
import io.example.fixtures.User;
import io.example.fixtures.UserService;
import io.example.fixtures.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnalyticsProxyIntegrationTest {

    private UserService userService;
    private AnalyticsService analyticsService;
    private AnalyticsProxyFactory factory;

    @BeforeEach
    void setUp() {
        analyticsService = mock(AnalyticsService.class);
        factory = new AnalyticsProxyFactory(analyticsService);
        userService = factory.create(UserService.class, new UserServiceImpl());
    }

    @Test
    @DisplayName("Should integrate with analytics service correctly")
    void shouldIntegrateWithAnalyticsService() {
        // Given
        ArgumentCaptor<String> eventCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object[]> paramsCaptor = ArgumentCaptor.forClass(Object[].class);

        // When
        userService.login("testUser");

        // Then
        verify(analyticsService).trackEvent(eventCaptor.capture(), paramsCaptor.capture());
        assertEquals("user.login", eventCaptor.getValue());
        assertEquals("testUser", paramsCaptor.getValue()[0]);
    }

    @Test
    @DisplayName("Should preserve original method behavior while tracking")
    void shouldPreserveOriginalBehavior() {
        // When
        User user = userService.getProfile("123");

        // Then
        assertNotNull(user);
        verify(analyticsService).trackEvent(eq("user.profile.view"), any());
    }

    @Test
    @DisplayName("Should handle exceptional cases correctly")
    void shouldHandleExceptions() {
        // Given
        UserService brokenService = factory.create(UserService.class, new UserService() {
            @Override
            public void login(String username) {
                throw new RuntimeException("Service unavailable");
            }

            @Override
            public void logout(String username) {}

            @Override
            public User getProfile(String userId) {
                return null;
            }
        });

        // Then
        assertThrows(RuntimeException.class, () -> brokenService.login("user"));
        verify(analyticsService).trackEvent(eq("user.login"), any());
    }
}
