package io.example.mocks;

import io.example.service.AnalyticsService;

import java.util.ArrayList;
import java.util.List;

public class MockAnalyticsService implements AnalyticsService {

    private final List<TrackedEvent> trackedEvents = new ArrayList<>();

    @Override
    public void trackEvent(String eventName, Object... params) {
        trackedEvents.add(new TrackedEvent(eventName, params));
    }

    public List<TrackedEvent> getTrackedEvents() {
        return this.trackedEvents;
    }

    // Helper class to store tracked events
    public static class TrackedEvent {

        private final String eventName;
        private final Object[] params;

        TrackedEvent(String eventName, Object[] params) {
            this.eventName = eventName;
            this.params = params;
        }

        public String getEventName() {
            return eventName;
        }

        public Object[] getParams() {
            return params;
        }
    }
}
