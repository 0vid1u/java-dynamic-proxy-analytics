package io.example.proxy;

import io.example.service.AnalyticsService;

import java.lang.reflect.Proxy;

public class AnalyticsProxyFactory {

    private final AnalyticsService analyticsService;

    public AnalyticsProxyFactory(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> interfaceClass, T implementation) {
        return (T) Proxy.newProxyInstance(
            interfaceClass.getClassLoader(),
            new Class<?>[] { interfaceClass },
            new AnalyticsProxyHandler(implementation, analyticsService)
        );
    }
}
