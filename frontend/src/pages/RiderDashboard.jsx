import { useState, useEffect } from 'react';
import { Card, Button, Badge, StatusDot, Skeleton } from '../components/UI';
import { riderAPI, bookingAPI } from '../services/api';

export default function RiderDashboard() {
  const [riders, setRiders] = useState([]);
  const [selectedRider, setSelectedRider] = useState(null);
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [loadingBookings, setLoadingBookings] = useState(false);

  useEffect(() => {
    riderAPI.getAll()
      .then(res => { setRiders(res.data); setLoading(false); })
      .catch(() => setLoading(false));
  }, []);

  const loadBookings = async (riderId) => {
    setLoadingBookings(true);
    setSelectedRider(riderId);
    try {
      const res = await bookingAPI.getByRider(riderId);
      setBookings(res.data);
    } catch {
      setBookings([]);
    }
    setLoadingBookings(false);
  };

  const statusColor = (s) => {
    const map = { CONFIRMED: 'success', CANCELLED: 'danger', COMPLETED: 'accent' };
    return map[s] || 'dim';
  };

  if (loading) {
    return (
      <div className="space-y-4">
        <Skeleton className="h-8 w-48" />
        <div className="grid md:grid-cols-2 gap-4">
          {[1,2].map(i => <Skeleton key={i} className="h-40" />)}
        </div>
      </div>
    );
  }

  return (
    <div className="animate-fade-in">
      <h1 className="text-3xl font-bold mb-2">Rider <span className="text-accent">Dashboard</span></h1>
      <p className="text-text-dim mb-8">Registered riders from the database <Badge color="dim">Spring Data JPA</Badge></p>

      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
        {riders.map(rider => (
          <Card
            key={rider.userId}
            className={`cursor-pointer ${selectedRider === rider.userId ? 'border-accent' : ''}`}
            onClick={() => loadBookings(rider.userId)}
          >
            <div className="flex items-center gap-4">
              <div className="w-14 h-14 rounded-full bg-gradient-to-br from-accent/30 to-emerald-400/30 flex items-center justify-center text-2xl font-bold text-accent">
                {rider.name?.charAt(0)}
              </div>
              <div className="flex-1">
                <h3 className="font-bold text-lg">{rider.name}</h3>
                <p className="text-text-dim text-sm">{rider.email}</p>
                <p className="text-text-dim text-sm">📞 {rider.phone}</p>
              </div>
            </div>
            <div className="mt-4 flex items-center justify-between">
              <Badge color="accent">ID: {rider.userId}</Badge>
              <div className="flex items-center gap-1">
                <span className="text-warning">★</span>
                <span className="text-sm font-medium">{rider.riderRating?.toFixed(1)}</span>
              </div>
            </div>
          </Card>
        ))}
      </div>

      {/* Booking History */}
      {selectedRider && (
        <div className="mt-8 animate-slide-up">
          <h2 className="text-xl font-bold mb-4">
            Booking History — Rider #{selectedRider}
          </h2>
          {loadingBookings ? (
            <div className="space-y-3">{[1,2].map(i => <Skeleton key={i} className="h-24" />)}</div>
          ) : bookings.length === 0 ? (
            <Card className="text-center py-8">
              <p className="text-text-dim">No bookings yet. <a href="/book" className="text-accent hover:underline">Book a ride →</a></p>
            </Card>
          ) : (
            <div className="space-y-3">
              {bookings.map(b => (
                <Card key={b.bookingId} className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                  <div className="flex-1">
                    <div className="flex items-center gap-3 mb-2">
                      <span className="font-mono font-bold text-accent">#{b.bookingId}</span>
                      <Badge color={statusColor(b.bookingStatus)}>{b.bookingStatus}</Badge>
                      {b.ride && <Badge color="dim">{b.ride.rideType}</Badge>}
                    </div>
                    {b.ride && (
                      <div className="text-sm text-text-dim space-y-1">
                        <p>📍 {b.ride.pickupLocation?.address} → {b.ride.dropLocation?.address}</p>
                        <p>💰 ₹{b.ride.fare?.toFixed(2)} · 🛣️ {b.ride.distance?.toFixed(2)} km</p>
                        <StatusDot status={b.ride.rideStatus} />
                      </div>
                    )}
                  </div>
                  <div className="text-right text-xs text-text-dim">
                    {b.bookingTime && new Date(b.bookingTime).toLocaleString()}
                  </div>
                </Card>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
}
