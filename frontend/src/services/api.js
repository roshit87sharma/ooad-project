import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
});

// ─── Rider APIs ───
export const riderAPI = {
  getAll:     ()         => api.get('/riders'),
  getById:    (id)       => api.get(`/riders/${id}`),
  register:   (data)     => api.post('/riders', data),
  rate:       (id, rating) => api.put(`/riders/${id}/rate?rating=${rating}`),
};

// ─── Driver APIs ───
export const driverAPI = {
  getAll:       ()           => api.get('/drivers/available'),
  getById:      (id)         => api.get(`/drivers/${id}`),
  register:     (data)       => api.post('/drivers', data),
  login:        (id)         => api.put(`/drivers/${id}/login`),
  logout:       (id)         => api.put(`/drivers/${id}/logout`),
  updateStatus: (id, status) => api.put(`/drivers/${id}/status?status=${status}`),
};

// ─── Booking APIs ───
export const bookingAPI = {
  create:     (data)     => api.post('/bookings', data),
  cancel:     (id)       => api.put(`/bookings/${id}/cancel`),
  getById:    (id)       => api.get(`/bookings/${id}`),
  getByRider: (riderId)  => api.get(`/bookings/rider/${riderId}`),
};

// ─── Ride APIs ───
export const rideAPI = {
  accept:    (id, driverId) => api.put(`/rides/${id}/accept?driverId=${driverId}`),
  start:     (id)           => api.put(`/rides/${id}/start`),
  complete:  (id)           => api.put(`/rides/${id}/complete`),
  cancel:    (id)           => api.put(`/rides/${id}/cancel`),
  track:     (id)           => api.get(`/rides/${id}/track`),
  status:    (id)           => api.get(`/rides/${id}/status`),
  estimate:  (distance)     => api.get(`/rides/estimate?distance=${distance}`),
};

// ─── Payment APIs ───
export const paymentAPI = {
  process:    (rideId, mode) => api.post(`/payments/ride/${rideId}?mode=${mode}`),
  getByRide:  (rideId)       => api.get(`/payments/ride/${rideId}`),
  refund:     (id)           => api.post(`/payments/${id}/refund`),
};

// ─── Trip APIs ───
export const tripAPI = {
  planMultiModal: (origin, from, to, dest) =>
    api.get(`/trips/multimodal?originAddress=${encodeURIComponent(origin)}&fromStation=${encodeURIComponent(from)}&toStation=${encodeURIComponent(to)}&destinationAddress=${encodeURIComponent(dest)}`),
};

export default api;
