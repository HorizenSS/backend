package com.ines.dto;
import com.ines.model.Alert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertResponseDto {
    private Long id;
    private String title;
    private String description;
    private Alert.AlertType type;
    private Alert.Severity severity;
    private Alert.AlertStatus status;
    private Double latitude;
    private Double longitude;
    private CustomerDTO user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
