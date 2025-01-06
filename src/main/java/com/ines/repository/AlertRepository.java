package com.ines.repository;

import com.ines.model.Alert;
import com.ines.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByUserOrderByCreatedAtDesc(Customer userId);

    @Query(value = """
        SELECT * FROM alerts 
        WHERE status = 'ACTIVE' 
        AND (6371 * acos(cos(radians(:latitude)) * cos(radians(latitude)) 
        * cos(radians(longitude) - radians(:longitude)) 
        + sin(radians(:latitude)) * sin(radians(latitude)))) <= :radius
        ORDER BY created_at DESC
        """, nativeQuery = true)
    List<Alert> findNearbyAlerts(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radius") Double radiusInKm
    );
}
