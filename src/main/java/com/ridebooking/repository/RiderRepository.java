package com.ridebooking.repository;

import com.ridebooking.model.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {
    Rider findByEmail(String email);
}
