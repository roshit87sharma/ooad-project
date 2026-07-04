package com.ridebooking.service;

import com.ridebooking.model.Rider;
import java.util.List;

/**
 * RiderService — DIP: Controllers depend on this interface, not the implementation.
 */
public interface RiderService {
    Rider registerRider(Rider rider);
    Rider getRiderById(Long riderId);
    List<Rider> getAllRiders();
    void rateRider(Long riderId, float rating);
}
