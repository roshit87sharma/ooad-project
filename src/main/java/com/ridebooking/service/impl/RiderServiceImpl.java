package com.ridebooking.service.impl;

import com.ridebooking.model.Rider;
import com.ridebooking.repository.RiderRepository;
import com.ridebooking.service.RiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * RiderServiceImpl — SRP: Only handles rider-related business logic.
 * DIP: Implements RiderService interface.
 */
@Service
public class RiderServiceImpl implements RiderService {

    private final RiderRepository riderRepository;

    @Autowired
    public RiderServiceImpl(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

    @Override
    public Rider registerRider(Rider rider) {
        return riderRepository.save(rider);
    }

    @Override
    public Rider getRiderById(Long riderId) {
        return riderRepository.findById(riderId)
                .orElseThrow(() -> new RuntimeException("Rider not found with ID: " + riderId));
    }

    @Override
    public List<Rider> getAllRiders() {
        return riderRepository.findAll();
    }

    @Override
    public void rateRider(Long riderId, float rating) {
        Rider rider = getRiderById(riderId);
        float currentRating = rider.getRiderRating();
        float newRating = (currentRating + rating) / 2;
        rider.setRiderRating(newRating);
        riderRepository.save(rider);
    }
}
