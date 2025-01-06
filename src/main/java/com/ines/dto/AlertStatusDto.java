package com.ines.dto;

import com.ines.model.Alert.AlertStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertStatusDto {
    @NotNull(message = "Status is required")
    private AlertStatus status;
}
