package com.ines.controller;

import com.ines.exception.ResourceNotFoundException;
import com.ines.model.Alert;
import com.ines.model.Customer;
import com.ines.model.GeoLocation;
import com.ines.repository.CustomerRepository;
import com.ines.service.LocationTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Set;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final LocationTrackingService locationTrackingService;
    private final SimpMessagingTemplate messagingTemplate;
    private final CustomerRepository customerRepository;

    @MessageMapping("/location/{email}")
    @SendTo("/topic/nearby-alerts")
    public void processLocation(@DestinationVariable String email, GeoLocation location) {
        Customer user = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        locationTrackingService.updateUserLocation(email, location);
    }


    public void notifyNearbyUsers(Alert alert) {
        Set<String> nearbyUsers = locationTrackingService.getNearbyUsers(
                alert.getLatitude(),
                alert.getLongitude(),
                5.0
        );
        for (String username : nearbyUsers) {
            org.slf4j.LoggerFactory.getLogger(WebSocketController.class).info("Notifying user: {}", username);
            messagingTemplate.convertAndSend(
                    "/topic/nearby-alerts/" + username,
                    alert
            );
        }

        org.slf4j.LoggerFactory.getLogger(WebSocketController.class).info("Notification sent to {} users", nearbyUsers.size());
    }
}