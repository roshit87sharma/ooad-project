import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Card, Button, Badge, Input } from '../components/UI';
import RideStatusStepper from '../components/RideStatusStepper';
import { rideAPI } from '../services/api';

export default function RideTracking() {
  const { rideId: paramId } = useParams();
  const navigate = useNavigate();
  const [rideIdInput, setRideIdInput] = useState(paramId || '1');
  const [ride, setRide] = useState(null);
  const [loading, setLoading] = useState(false);
  const [actionLoading, setActionLoading] = useState('');
  const [error, setError] = useState('');

  const fetchRide = async (id) => {
    if (!id) return;
    setLoading(true);
    setError('');
    try {
      const res = await rideAPI.track(id);
      setRide(res.data);
    } catch (e) {
      setError(e.response?.data?.message || 'Ride not found');
      setRide(null);
    }
    setLoading(false);
  };

  useEffect(() => {
    if (paramId) fetchRide(paramId);
  }, [paramId]);

  const handleAction = async (action) => {
    setActionLoading(action);
    setError('');
    try {
      if (action === 'accept') await rideAPI.accept(ride.rideId, 1);
      if (action === 'start') await rideAPI.start(ride.rideId);
      if (action === 'complete') await rideAPI.complete(ride.rideId);
      if (action === 'cancel') await rideAPI.cancel(ride.rideId);
      await fetchRide(ride.rideId);
    } catch (e) {
      setError(e.response?.data?.message || `Failed to ${action} ride`);
    }
    setActionLoading('');
  };

  const getAvailableActions = () => {
    if (!ride) return [];
    const actions = {
      REQUESTED:       [{ key: 'accept', label: '🚕 Accept Ride', variant: 'primary' },
                        { key: 'cancel', label: '❌ Cancel', variant: 'danger' }],
      DRIVER_ASSIGNED: [{ key: 'start', label: '▶️ Start Ride', variant: 'primary' },
                        { key: 'cancel', label: '❌ Cancel', variant: 'danger' }],
      IN_PROGRESS:     [{ key: 'complete', label: '✅ Complete Ride', variant: 'success' },
                        { key: 'cancel', label: '❌ Cancel', variant: 'danger' }],
      COMPLETED:       [],
      CANCELLED:       [],
    };
    return actions[ride.rideStatus] || [];
  };

  return (
    <div className="max-w-3xl mx-auto animate-fade-in">
      <h1 className="text-3xl font-bold mb-2">Track <span className="text-accent">Ride</span></h1>
      <p className="text-text-dim mb-8">
        State Pattern — ride transitions: REQUESTED → DRIVER_ASSIGNED → IN_PROGRESS → COMPLETED
      </p>

      {/* Search */}
      <Card className="mb-6">
        <div className="flex gap-3">
          <Input
            label="Ride ID"
            type="number"
            value={rideIdInput}
            onChange={e => setRideIdInput(e.target.value)}
            className="flex-1"
          />
          <div className="flex items-end">
            <Button onClick={() => fetchRide(rideIdInput)} loading={loading}>
              🔍 Track
            </Button>
          </div>
        </div>
      </Card>

      {error && (
        <div className="bg-danger/10 border border-danger/30 rounded-xl p-4 text-danger text-sm mb-6 animate-fade-in">
          {error}
        </div>
      )}

      {ride && (
        <div className="space-y-6 animate-slide-up">
          {/* Status Stepper */}
          <Card>
            <h3 className="text-lg font-bold mb-2">Ride Status</h3>
            <RideStatusStepper currentStatus={ride.rideStatus} />
          </Card>

          {/* Ride Details */}
          <Card>
            <h3 className="text-lg font-bold mb-4">Ride Details</h3>
            <div className="grid grid-cols-2 gap-4">
              <div className="bg-surface-2 rounded-xl p-4">
                <p className="text-xs text-text-dim mb-1">Ride ID</p>
                <p className="font-mono font-bold text-accent text-lg">#{ride.rideId}</p>
              </div>
              <div className="bg-surface-2 rounded-xl p-4">
                <p className="text-xs text-text-dim mb-1">Fare</p>
                <p className="font-bold text-accent text-lg">₹{ride.fare?.toFixed(2)}</p>
              </div>
              <div className="bg-surface-2 rounded-xl p-4">
                <p className="text-xs text-text-dim mb-1">Type</p>
                <Badge color="accent">{ride.rideType}</Badge>
              </div>
              <div className="bg-surface-2 rounded-xl p-4">
                <p className="text-xs text-text-dim mb-1">Distance</p>
                <p className="font-medium">{ride.distance?.toFixed(2)} km</p>
              </div>
            </div>

            {/* Locations */}
            <div className="mt-4 space-y-3">
              <div className="flex items-start gap-3 bg-surface-2 rounded-xl p-4">
                <span className="text-accent text-lg">📍</span>
                <div>
                  <p className="text-xs text-text-dim">Pickup</p>
                  <p className="font-medium">{ride.pickupLocation?.address}</p>
                  <p className="text-xs text-text-dim mt-0.5">{ride.pickupLocation?.latitude?.toFixed(4)}, {ride.pickupLocation?.longitude?.toFixed(4)}</p>
                </div>
              </div>
              <div className="flex items-start gap-3 bg-surface-2 rounded-xl p-4">
                <span className="text-danger text-lg">🏁</span>
                <div>
                  <p className="text-xs text-text-dim">Drop</p>
                  <p className="font-medium">{ride.dropLocation?.address}</p>
                  <p className="text-xs text-text-dim mt-0.5">{ride.dropLocation?.latitude?.toFixed(4)}, {ride.dropLocation?.longitude?.toFixed(4)}</p>
                </div>
              </div>
            </div>

            {/* Driver Info */}
            {ride.driver && (
              <div className="mt-4 bg-accent/5 border border-accent/20 rounded-xl p-4">
                <p className="text-xs text-text-dim mb-2">Assigned Driver</p>
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 rounded-full bg-accent/20 flex items-center justify-center font-bold text-accent">
                    {ride.driver.name?.charAt(0)}
                  </div>
                  <div>
                    <p className="font-bold">{ride.driver.name}</p>
                    <p className="text-sm text-text-dim">{ride.driver.vehicle?.vehicleType} · {ride.driver.vehicle?.vehicleNumber}</p>
                  </div>
                  <div className="ml-auto text-right">
                    <div className="flex items-center gap-1">
                      <span className="text-warning">★</span>
                      <span className="text-sm">{ride.driver.driverRating?.toFixed(1)}</span>
                    </div>
                  </div>
                </div>
              </div>
            )}
          </Card>

          {/* Actions */}
          {getAvailableActions().length > 0 && (
            <Card>
              <h3 className="text-lg font-bold mb-4">Actions</h3>
              <div className="flex gap-3 flex-wrap">
                {getAvailableActions().map(action => (
                  <Button
                    key={action.key}
                    variant={action.variant}
                    onClick={() => handleAction(action.key)}
                    loading={actionLoading === action.key}
                    size="lg"
                    className="flex-1"
                  >
                    {action.label}
                  </Button>
                ))}
              </div>
            </Card>
          )}

          {/* Payment link */}
          {ride.rideStatus === 'COMPLETED' && (
            <Button
              size="lg"
              className="w-full"
              onClick={() => navigate(`/payment/${ride.rideId}`)}
            >
              💳  Proceed to Payment — ₹{ride.fare?.toFixed(2)}
            </Button>
          )}
        </div>
      )}
    </div>
  );
}
