package io.example.proxy;

import io.example.service.AnalyticsService;
import io.example.annotation.TrackEvent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class AnalyticsProxyHandler implements InvocationHandler {

    private final Object target;
    private final AnalyticsService analyticsService;

    public AnalyticsProxyHandler(Object target, AnalyticsService analyticsService) {
        this.target = target;
        this.analyticsService = analyticsService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Check if method has @TrackEvent annotation
        TrackEvent trackEvent = method.getAnnotation(TrackEvent.class);
        if (trackEvent != null) {
            // Track the event before method execution
            analyticsService.trackEvent(trackEvent.value(), args);
        }

        // Execute the actual method
        return method.invoke(target, args);
    }
}
