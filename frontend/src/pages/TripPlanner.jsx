import { useState } from 'react';
import { Card, Button, Input, Select, Badge } from '../components/UI';
import { tripAPI } from '../services/api';

const metroStations = [
  'Central', 'MG Road', 'Indiranagar', 'Whitefield',
  'Majestic', 'Yeshwanthpur', 'Peenya', 'Nagasandra',
];

export default function TripPlanner() {
  const [form, setForm] = useState({
    origin: 'Home, JP Nagar',
    fromStation: 'Central',
    toStation: 'Whitefield',
    destination: 'Office, ITPL',
  });
  const [trip, setTrip] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const update = (k, v) => setForm(p => ({ ...p, [k]: v }));

  const planTrip = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await tripAPI.planMultiModal(form.origin, form.fromStation, form.toStation, form.destination);
      setTrip(res.data);
    } catch (e) {
      setError(e.response?.data?.message || 'Trip planning failed');
    }
    setLoading(false);
  };

  const legIcon = (mode) => {
    if (mode.includes('METRO')) return '🚆';
    return '🚗';
  };

  const legColor = (mode) => {
    if (mode.includes('METRO')) return 'from-blue-500/20 to-indigo-500/20 border-blue-500/30';
    return 'from-emerald-500/20 to-cyan-500/20 border-accent/30';
  };

  return (
    <div className="max-w-4xl mx-auto animate-fade-in">
      <h1 className="text-3xl font-bold mb-2">Multi-Modal <span className="text-accent">Trip Planner</span></h1>
      <p className="text-text-dim mb-8">
        <Badge color="dim">Facade Pattern</Badge> — Combines cab booking, metro service, and fare estimation into a single planTrip() call
      </p>

      <div className="grid md:grid-cols-2 gap-6">
        {/* Form */}
        <Card>
          <h3 className="text-lg font-bold mb-4">Plan Your Trip</h3>
          <div className="space-y-4">
            <Input
              label="📍 Origin Address"
              value={form.origin}
              onChange={e => update('origin', e.target.value)}
              placeholder="Your starting point"
            />
            <Select
              label="🚆 From Metro Station"
              value={form.fromStation}
              onChange={e => update('fromStation', e.target.value)}
              options={metroStations.map(s => ({ value: s, label: s }))}
            />
            <Select
              label="🚆 To Metro Station"
              value={form.toStation}
              onChange={e => update('toStation', e.target.value)}
              options={metroStations.map(s => ({ value: s, label: s }))}
            />
            <Input
              label="🏁 Destination Address"
              value={form.destination}
              onChange={e => update('destination', e.target.value)}
              placeholder="Your destination"
            />

            <Button onClick={planTrip} loading={loading} size="lg" className="w-full">
              🗺️  Plan Trip
            </Button>
          </div>
        </Card>

        {/* Results */}
        <div>
          {error && (
            <div className="bg-danger/10 border border-danger/30 rounded-xl p-4 text-danger text-sm mb-4">
              {error}
            </div>
          )}

          {trip && (
            <div className="space-y-4 animate-slide-up">
              {/* Summary */}
              <Card glow>
                <h3 className="text-lg font-bold mb-4">Trip Summary</h3>
                <div className="grid grid-cols-2 gap-4">
                  <div className="bg-surface-2 rounded-xl p-4 text-center">
                    <p className="text-xs text-text-dim">Total Fare</p>
                    <p className="text-2xl font-bold text-accent">₹{trip.totalFare?.toFixed(0)}</p>
                  </div>
                  <div className="bg-surface-2 rounded-xl p-4 text-center">
                    <p className="text-xs text-text-dim">Total Time</p>
                    <p className="text-2xl font-bold text-text">{trip.totalTime} min</p>
                  </div>
                </div>
              </Card>

              {/* Legs */}
              <Card>
                <h3 className="text-lg font-bold mb-4">Trip Legs</h3>
                <div className="space-y-3">
                  {trip.legs?.map((leg, i) => (
                    <div key={i} className={`bg-gradient-to-r ${legColor(leg.mode)} border rounded-xl p-4 animate-fade-in`} style={{ animationDelay: `${i * 150}ms` }}>
                      <div className="flex items-center justify-between mb-2">
                        <div className="flex items-center gap-2">
                          <span className="text-2xl">{legIcon(leg.mode)}</span>
                          <Badge color={leg.mode.includes('METRO') ? 'accent' : 'success'}>
                            {leg.mode.replace(/_/g, ' ')}
                          </Badge>
                        </div>
                        <span className="font-bold text-accent text-lg">₹{leg.fare?.toFixed(0)}</span>
                      </div>
                      <p className="text-sm text-text">{leg.description}</p>
                      <p className="text-xs text-text-dim mt-1">⏱️ {leg.timeMinutes} minutes</p>
                    </div>
                  ))}
                </div>
              </Card>

              {/* Facade Pattern Explanation */}
              <Card className="bg-accent/5 border-accent/20">
                <p className="text-xs font-bold text-accent mb-3">🏛️ FACADE PATTERN FLOW</p>
                <div className="flex flex-col gap-2 text-xs">
                  <div className="flex items-center gap-2 flex-wrap">
                    <Badge color="accent">TripController</Badge>
                    <span className="text-accent">→</span>
                    <Badge color="accent">MultiModalTripFacade.planTrip()</Badge>
                  </div>
                  <div className="pl-8 space-y-1 text-text-dim">
                    <p>├─ Step 1: <span className="text-text">FareEstimator.calculateCabFare()</span> → Cab to station</p>
                    <p>├─ Step 2: <span className="text-text">MetroServiceAdapter.getMetroFare()</span> → Metro ride</p>
                    <p>└─ Step 3: <span className="text-text">FareEstimator.calculateCabFare()</span> → Cab from station</p>
                  </div>
                </div>
              </Card>
            </div>
          )}

          {!trip && !error && (
            <Card className="text-center py-12">
              <p className="text-4xl mb-4">🗺️</p>
              <p className="text-text-dim">Enter your trip details and click "Plan Trip" to see the multi-modal breakdown</p>
            </Card>
          )}
        </div>
      </div>
    </div>
  );
}
