package com.ines.service;

import com.ines.model.GeoLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocationTrackingServiceTest {

    private LocationTrackingService locationTrackingService;

    @BeforeEach
    void setUp() {
        locationTrackingService = new LocationTrackingService();
    }

    @Test
    void updateUserLocation_shouldStoreUserLocation() {
        // Arrange
        String username = "testUser";
        GeoLocation location = new GeoLocation(40.7128, -74.0060); // New York City

        // Act
        locationTrackingService.updateUserLocation(username, location);

        // Assert
        Set<String> nearbyUsers = locationTrackingService.getNearbyUsers(40.7128, -74.0060, 1);
        assertTrue(nearbyUsers.contains(username));
    }

    @Test
    void getNearbyUsers_shouldReturnEmptySetWhenNoUsersWithinRadius() {
        // Arrange
        String user1 = "user1";
        GeoLocation location1 = new GeoLocation(40.7128, -74.0060); // New York City

        locationTrackingService.updateUserLocation(user1, location1);

        // Act
        Set<String> nearbyUsers = locationTrackingService.getNearbyUsers(34.0522, -118.2437, 5); // 5km radius in LA

        // Assert
        assertTrue(nearbyUsers.isEmpty());
    }

    @Test
    void calculateDistance_shouldReturnCorrectDistance() {
        // Arrange
        GeoLocation location1 = new GeoLocation(40.7128, -74.0060); // New York City
        GeoLocation location2 = new GeoLocation(34.0522, -118.2437); // Los Angeles

        // Act
        double distance = locationTrackingService.calculateDistance(
                location1.latitude(),
                location1.longitude(),
                location2.latitude(),
                location2.longitude()
        );

        // Assert
        assertTrue(distance > 3900000 && distance < 4100000); // Rough distance in meters between NYC and LA
    }
}
