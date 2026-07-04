import { useState, useEffect } from 'react';
import { Card, Button, Badge, StatusDot, Skeleton } from '../components/UI';
import { driverAPI, rideAPI } from '../services/api';

export default function DriverDashboard() {
  const [drivers, setDrivers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState({});

  const fetchDrivers = async () => {
    try {
      const res = await driverAPI.getAll();
      setDrivers(res.data);
    } catch { setDrivers([]); }
    setLoading(false);
  };

  useEffect(() => { fetchDrivers(); }, []);

  const handleAction = async (driverId, action) => {
    setActionLoading(prev => ({ ...prev, [driverId + action]: true }));
    try {
      if (action === 'login') await driverAPI.login(driverId);
      if (action === 'logout') await driverAPI.logout(driverId);
      await fetchDrivers();
    } catch {}
    setActionLoading(prev => ({ ...prev, [driverId + action]: false }));
  };

  if (loading) {
    return (
      <div className="space-y-4">
        <Skeleton className="h-8 w-48" />
        <div className="grid md:grid-cols-2 gap-4">{[1,2].map(i => <Skeleton key={i} className="h-48" />)}</div>
      </div>
    );
  }

  return (
    <div className="animate-fade-in">
      <h1 className="text-3xl font-bold mb-2">Driver <span className="text-accent">Dashboard</span></h1>
      <p className="text-text-dim mb-8">
        Manage driver availability <Badge color="dim">State Pattern</Badge>
      </p>

      {drivers.length === 0 ? (
        <Card className="text-center py-12">
          <p className="text-4xl mb-4">🚕</p>
          <p className="text-text-dim">No available drivers. All drivers may be offline or busy.</p>
        </Card>
      ) : (
        <div className="grid md:grid-cols-2 gap-6">
          {drivers.map(driver => (
            <Card key={driver.userId}>
              <div className="flex items-start gap-4">
                <div className="w-16 h-16 rounded-2xl bg-gradient-to-br from-amber-500/30 to-orange-400/30 flex items-center justify-center text-3xl">
                  🚕
                </div>
                <div className="flex-1">
                  <div className="flex items-center justify-between">
                    <h3 className="text-lg font-bold">{driver.name}</h3>
                    <StatusDot status={driver.availabilityStatus} />
                  </div>
                  <p className="text-text-dim text-sm">{driver.email}</p>
                  <p className="text-text-dim text-sm">📞 {driver.phone}</p>
                  <div className="flex items-center gap-1 mt-1">
                    <span className="text-warning">★</span>
                    <span className="text-sm">{driver.driverRating?.toFixed(1)}</span>
                  </div>
                </div>
              </div>

              {/* Vehicle Info */}
              {driver.vehicle && (
                <div className="mt-4 bg-surface-2 rounded-xl p-4">
                  <p className="text-xs text-text-dim uppercase tracking-wide mb-2">Vehicle (1:1 Association)</p>
                  <div className="grid grid-cols-2 gap-2 text-sm">
                    <div><span className="text-text-dim">Type:</span> <span className="font-medium">{driver.vehicle.vehicleType}</span></div>
                    <div><span className="text-text-dim">Number:</span> <span className="font-mono text-accent">{driver.vehicle.vehicleNumber}</span></div>
                    {driver.vehicle.model && <div><span className="text-text-dim">Model:</span> {driver.vehicle.model}</div>}
                    {driver.vehicle.color && <div><span className="text-text-dim">Color:</span> {driver.vehicle.color}</div>}
                  </div>
                </div>
              )}

              {/* Actions */}
              <div className="mt-4 flex gap-2">
                <Button
                  variant="success"
                  size="sm"
                  className="flex-1"
                  loading={actionLoading[driver.userId + 'login']}
                  onClick={() => handleAction(driver.userId, 'login')}
                >
                  Go Online
                </Button>
                <Button
                  variant="danger"
                  size="sm"
                  className="flex-1"
                  loading={actionLoading[driver.userId + 'logout']}
                  onClick={() => handleAction(driver.userId, 'logout')}
                >
                  Go Offline
                </Button>
              </div>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
}
