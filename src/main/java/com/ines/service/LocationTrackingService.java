package com.ines.service;

import com.ines.model.GeoLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationTrackingService {
    private final Map<String, GeoLocation> activeUsers = new ConcurrentHashMap<>();

    public void updateUserLocation(String username, GeoLocation location) {
        activeUsers.put(username, location);
    }

    public Set<String> getNearbyUsers(double latitude, double longitude, double radiusKm) {
        return activeUsers.entrySet().stream()
                .filter(entry -> isWithinRadius(
                        entry.getValue(),
                        latitude,
                        longitude,
                        radiusKm
                ))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    private boolean isWithinRadius(GeoLocation userLocation, double lat, double lng, double radiusKm) {
        double radiusMeters = radiusKm * 1000;
        double distance = calculateDistance(
                userLocation.latitude(),
                userLocation.longitude(),
                lat,
                lng
        );
        return distance <= radiusMeters;
    }

    double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Convert to meters
        return R * c * 1000;
    }

}
