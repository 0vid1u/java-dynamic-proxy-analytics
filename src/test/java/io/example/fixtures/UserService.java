package io.example.fixtures;

import io.example.annotation.TrackEvent;

public interface UserService {

    @TrackEvent("user.login")
    void login(String username);

    @TrackEvent("user.logout")
    void logout(String username);

    @TrackEvent("user.profile.view")
    User getProfile(String userId);
}
