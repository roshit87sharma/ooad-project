package com.ridebooking.repository;

import com.ridebooking.model.Driver;
import com.ridebooking.model.enums.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    List<Driver> findByAvailabilityStatus(DriverStatus status);
}
