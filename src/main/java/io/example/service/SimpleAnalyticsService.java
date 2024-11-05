package io.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class SimpleAnalyticsService implements AnalyticsService {

    private static final Logger log = LoggerFactory.getLogger(SimpleAnalyticsService.class);

    @Override
    public void trackEvent(String eventName, Object... params) {
        log.info("Event tracked: {} with params: {}", eventName, Arrays.toString(params));
    }
}
