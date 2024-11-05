package io.example.service;

public interface AnalyticsService {

    void trackEvent(String eventName, Object... params);

}
