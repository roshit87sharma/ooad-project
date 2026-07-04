import { Badge } from './UI';

const steps = [
  { status: 'REQUESTED',        label: 'Requested',  icon: '📋', color: 'warning' },
  { status: 'DRIVER_ASSIGNED',  label: 'Assigned',   icon: '🚕', color: 'accent' },
  { status: 'IN_PROGRESS',      label: 'In Progress', icon: '🛣️', color: 'accent' },
  { status: 'COMPLETED',        label: 'Completed',  icon: '✅', color: 'success' },
];

export default function RideStatusStepper({ currentStatus }) {
  const currentIdx = steps.findIndex(s => s.status === currentStatus);
  const isCancelled = currentStatus === 'CANCELLED';

  if (isCancelled) {
    return (
      <div className="flex items-center justify-center py-6">
        <div className="flex items-center gap-3 bg-danger/10 border border-danger/30 rounded-2xl px-6 py-4">
          <span className="text-3xl">❌</span>
          <div>
            <p className="text-danger font-semibold text-lg">Ride Cancelled</p>
            <p className="text-text-dim text-sm">This ride was cancelled</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="py-4">
      <div className="flex items-center justify-between relative">
        {/* Progress line */}
        <div className="absolute top-6 left-0 right-0 h-0.5 bg-border mx-12" />
        <div
          className="absolute top-6 left-0 h-0.5 bg-accent transition-all duration-700 mx-12"
          style={{ width: `${Math.max(0, currentIdx) * 33.33}%` }}
        />

        {steps.map((step, idx) => {
          const isActive = idx <= currentIdx;
          const isCurrent = idx === currentIdx;
          return (
            <div key={step.status} className="flex flex-col items-center relative z-10 flex-1">
              <div
                className={`w-12 h-12 rounded-full flex items-center justify-center text-xl transition-all duration-500 ${
                  isCurrent
                    ? 'bg-accent/20 border-2 border-accent scale-110 animate-pulse-glow'
                    : isActive
                    ? 'bg-accent/15 border-2 border-accent/50'
                    : 'bg-surface-2 border-2 border-border'
                }`}
              >
                {step.icon}
              </div>
              <span className={`mt-2 text-xs font-medium ${isActive ? 'text-accent' : 'text-text-dim'}`}>
                {step.label}
              </span>
              {isCurrent && (
                <Badge color={step.color} className="mt-1">Current</Badge>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
}
