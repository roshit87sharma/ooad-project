import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Card, Button, Input, Select, Badge } from '../components/UI';
import { riderAPI, rideAPI, bookingAPI } from '../services/api';

export default function BookRide() {
  const navigate = useNavigate();
  const [riders, setRiders] = useState([]);
  const [fareOptions, setFareOptions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [estimating, setEstimating] = useState(false);
  const [booking, setBooking] = useState(null);
  const [error, setError] = useState('');

  const [form, setForm] = useState({
    riderId: '1',
    pickupAddress: 'MG Road, Bangalore',
    pickupLat: '12.9716',
    pickupLng: '77.5946',
    dropAddress: 'Koramangala, Bangalore',
    dropLat: '12.9352',
    dropLng: '77.6245',
    rideType: 'ECONOMY',
  });

  useEffect(() => {
    riderAPI.getAll().then(res => setRiders(res.data)).catch(() => {});
  }, []);

  const estimateFare = async () => {
    setEstimating(true);
    setError('');
    try {
      const dlat = form.dropLat - form.pickupLat;
      const dlng = form.dropLng - form.pickupLng;
      const dist = Math.sqrt(dlat * dlat + dlng * dlng) * 111;
      const res = await rideAPI.estimate(dist > 0 ? dist : 5);
      setFareOptions(res.data);
    } catch (e) {
      setError('Failed to estimate fare');
    }
    setEstimating(false);
  };

  const handleBook = async () => {
    setLoading(true);
    setError('');
    try {
      const payload = {
        riderId: parseInt(form.riderId),
        pickupLat: parseFloat(form.pickupLat),
        pickupLng: parseFloat(form.pickupLng),
        pickupAddress: form.pickupAddress,
        dropLat: parseFloat(form.dropLat),
        dropLng: parseFloat(form.dropLng),
        dropAddress: form.dropAddress,
        rideType: form.rideType,
      };
      const res = await bookingAPI.create(payload);
      setBooking(res.data);
    } catch (e) {
      setError(e.response?.data?.message || 'Booking failed');
    }
    setLoading(false);
  };

  const update = (key, val) => setForm(prev => ({ ...prev, [key]: val }));

  if (booking) {
    return (
      <div className="max-w-lg mx-auto animate-slide-up">
        <Card glow className="text-center">
          <div className="text-5xl mb-4">🎉</div>
          <h2 className="text-2xl font-bold mb-2">Ride Booked!</h2>
          <p className="text-text-dim mb-6">Your ride has been confirmed.</p>

          <div className="bg-surface-2 rounded-xl p-4 text-left space-y-2 mb-6">
            <div className="flex justify-between">
              <span className="text-text-dim text-sm">Booking ID</span>
              <span className="font-mono font-bold text-accent">#{booking.bookingId}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-text-dim text-sm">Ride ID</span>
              <span className="font-mono">#{booking.ride?.rideId}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-text-dim text-sm">Status</span>
              <Badge color="warning">{booking.ride?.rideStatus}</Badge>
            </div>
            <div className="flex justify-between">
              <span className="text-text-dim text-sm">Type</span>
              <Badge color="accent">{booking.ride?.rideType}</Badge>
            </div>
            <div className="flex justify-between">
              <span className="text-text-dim text-sm">Fare</span>
              <span className="font-bold text-accent">₹{booking.ride?.fare?.toFixed(2)}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-text-dim text-sm">Pickup</span>
              <span className="text-sm">{booking.ride?.pickupLocation?.address}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-text-dim text-sm">Drop</span>
              <span className="text-sm">{booking.ride?.dropLocation?.address}</span>
            </div>
          </div>

          <div className="flex gap-3">
            <Button className="flex-1" onClick={() => navigate(`/track/${booking.ride?.rideId}`)}>
              📍 Track Ride
            </Button>
            <Button variant="secondary" className="flex-1" onClick={() => { setBooking(null); setFareOptions([]); }}>
              Book Another
            </Button>
          </div>
        </Card>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto animate-fade-in">
      <h1 className="text-3xl font-bold mb-2">Book a <span className="text-accent">Ride</span></h1>
      <p className="text-text-dim mb-8">Uses <Badge color="dim">Factory Pattern</Badge> for ride creation and <Badge color="dim">Strategy Pattern</Badge> for fare calculation</p>

      <div className="grid md:grid-cols-2 gap-6">
        {/* Booking Form */}
        <Card>
          <h3 className="text-lg font-bold mb-4">Ride Details</h3>
          <div className="space-y-4">
            <Select
              label="Select Rider"
              value={form.riderId}
              onChange={e => update('riderId', e.target.value)}
              options={riders.map(r => ({ value: r.userId, label: `${r.name} (ID: ${r.userId})` }))}
            />
            <div className="border-t border-border pt-4">
              <p className="text-sm font-medium text-accent mb-3">📍 Pickup Location</p>
              <Input label="Address" value={form.pickupAddress} onChange={e => update('pickupAddress', e.target.value)} />
              <div className="grid grid-cols-2 gap-3 mt-3">
                <Input label="Latitude" type="number" step="0.0001" value={form.pickupLat} onChange={e => update('pickupLat', e.target.value)} />
                <Input label="Longitude" type="number" step="0.0001" value={form.pickupLng} onChange={e => update('pickupLng', e.target.value)} />
              </div>
            </div>
            <div className="border-t border-border pt-4">
              <p className="text-sm font-medium text-danger mb-3">📍 Drop Location</p>
              <Input label="Address" value={form.dropAddress} onChange={e => update('dropAddress', e.target.value)} />
              <div className="grid grid-cols-2 gap-3 mt-3">
                <Input label="Latitude" type="number" step="0.0001" value={form.dropLat} onChange={e => update('dropLat', e.target.value)} />
                <Input label="Longitude" type="number" step="0.0001" value={form.dropLng} onChange={e => update('dropLng', e.target.value)} />
              </div>
            </div>
            <Select
              label="Ride Type"
              value={form.rideType}
              onChange={e => update('rideType', e.target.value)}
              options={[
                { value: 'ECONOMY', label: '🚗 Economy — Affordable' },
                { value: 'PREMIUM', label: '🏎️ Premium — Luxury' },
                { value: 'SHARED', label: '👥 Shared — Cheapest' },
              ]}
            />
          </div>
        </Card>

        {/* Fare Estimation */}
        <div className="space-y-4">
          <Card>
            <h3 className="text-lg font-bold mb-4">💰 Fare Estimation</h3>
            <p className="text-xs text-text-dim mb-4">Strategy Pattern — Each ride type uses a different pricing algorithm</p>
            <Button onClick={estimateFare} loading={estimating} variant="secondary" className="w-full mb-4">
              Calculate Fares
            </Button>

            {fareOptions.length > 0 && (
              <div className="space-y-3 animate-fade-in">
                {fareOptions.map((opt, i) => (
                  <div
                    key={i}
                    onClick={() => update('rideType', opt.optionType)}
                    className={`flex items-center justify-between p-4 rounded-xl cursor-pointer transition-all border ${
                      form.rideType === opt.optionType
                        ? 'bg-accent/10 border-accent'
                        : 'bg-surface-2 border-border hover:border-accent/30'
                    }`}
                  >
                    <div>
                      <p className="font-bold">{opt.optionType === 'SHARED' ? '👥' : opt.optionType === 'PREMIUM' ? '🏎️' : '🚗'} {opt.optionType}</p>
                      <p className="text-xs text-text-dim">{opt.estimatedTime} min estimated</p>
                    </div>
                    <span className="text-xl font-bold text-accent">₹{opt.price.toFixed(0)}</span>
                  </div>
                ))}
              </div>
            )}
          </Card>

          {error && (
            <div className="bg-danger/10 border border-danger/30 rounded-xl p-4 text-danger text-sm">{error}</div>
          )}

          <Button onClick={handleBook} loading={loading} size="lg" className="w-full">
            🚗  Confirm Booking
          </Button>
        </div>
      </div>
    </div>
  );
}
