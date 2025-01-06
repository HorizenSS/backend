package com.ines.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private AlertType type;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false) // Updated column name
    private Customer user;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertStatus status = AlertStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    public Alert(long l, String s, double v, double v1, String active) {   // Constructor for creating an Alert object
        this.id = l;
        this.title = s;
        this.latitude = v;
        this.longitude = v1;
        this.status = AlertStatus.valueOf(active);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum AlertType {
        SUSPICIOUS_ACTIVITY("SUSPICIOUS ACTIVITY"),
        CRIME("CRIME"),
        HAZARD("HAZARD"),
        EMERGENCY("EMERGENCY"),
        OTHER("OTHER"),
        WEATHER("WEATHER"),
        TRAFFIC("TRAFFIC"),
        ENVIRONMENTAL("ENVIRONMENTAL"),
        PUBLIC_SAFETY("PUBLIC SAFETY"),
        HEALTH("HEALTH"),
        TRANSPORTATION("TRANSPORTATION"),
        FIRE("FIRE"),
        NATURAL_DISASTER("NATURAL DISASTER");

        private final String value;

        AlertType(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @JsonCreator
        public static AlertType fromValue(String value) {
            for (AlertType type : AlertType.values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown enum value: " + value);
        }
    }

    public enum AlertStatus {
        ACTIVE,
        RESOLVED,
        FALSE_ALARM
    }

    public enum Severity {
        LOW,
        MEDIUM,
        HIGH
    }
}
