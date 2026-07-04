export function Card({ children, className = '', glow = false, ...props }) {
  return (
    <div
      className={`bg-surface border border-border rounded-2xl p-6 transition-all duration-300 hover:border-accent/30 ${
        glow ? 'animate-pulse-glow' : ''
      } ${className}`}
      {...props}
    >
      {children}
    </div>
  );
}

export function Button({ children, variant = 'primary', size = 'md', loading = false, className = '', ...props }) {
  const base = 'inline-flex items-center justify-center font-semibold rounded-xl transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed';
  const variants = {
    primary: 'bg-accent text-primary hover:bg-accent-hover hover:shadow-lg hover:shadow-accent/20 active:scale-[0.98]',
    secondary: 'bg-surface-2 text-text border border-border hover:bg-surface-3 hover:border-accent/30',
    danger: 'bg-danger/20 text-danger border border-danger/30 hover:bg-danger/30',
    success: 'bg-success/20 text-success border border-success/30 hover:bg-success/30',
    ghost: 'text-text-dim hover:text-text hover:bg-surface-2',
  };
  const sizes = {
    sm: 'px-3 py-1.5 text-xs gap-1.5',
    md: 'px-5 py-2.5 text-sm gap-2',
    lg: 'px-8 py-3.5 text-base gap-2',
  };

  return (
    <button className={`${base} ${variants[variant]} ${sizes[size]} ${className}`} disabled={loading} {...props}>
      {loading && <span className="w-4 h-4 border-2 border-current/30 border-t-current rounded-full animate-spin" />}
      {children}
    </button>
  );
}

export function Badge({ children, color = 'accent', className = '' }) {
  const colors = {
    accent:  'bg-accent/15 text-accent border-accent/30',
    danger:  'bg-danger/15 text-danger border-danger/30',
    warning: 'bg-warning/15 text-warning border-warning/30',
    success: 'bg-success/15 text-success border-success/30',
    dim:     'bg-surface-2 text-text-dim border-border',
  };
  return (
    <span className={`inline-flex items-center px-2.5 py-1 text-xs font-medium rounded-full border ${colors[color]} ${className}`}>
      {children}
    </span>
  );
}

export function Input({ label, error, className = '', ...props }) {
  return (
    <div className={className}>
      {label && <label className="block text-sm font-medium text-text-dim mb-1.5">{label}</label>}
      <input
        className={`w-full px-4 py-2.5 bg-surface-2 border rounded-xl text-text placeholder:text-text-dim/50 focus:outline-none focus:ring-2 focus:ring-accent/40 focus:border-accent transition-all ${
          error ? 'border-danger' : 'border-border'
        }`}
        {...props}
      />
      {error && <p className="mt-1 text-xs text-danger">{error}</p>}
    </div>
  );
}

export function Select({ label, options = [], className = '', ...props }) {
  return (
    <div className={className}>
      {label && <label className="block text-sm font-medium text-text-dim mb-1.5">{label}</label>}
      <select
        className="w-full px-4 py-2.5 bg-surface-2 border border-border rounded-xl text-text focus:outline-none focus:ring-2 focus:ring-accent/40 focus:border-accent transition-all appearance-none"
        {...props}
      >
        {options.map(opt => (
          <option key={opt.value} value={opt.value}>{opt.label}</option>
        ))}
      </select>
    </div>
  );
}

export function Skeleton({ className = '' }) {
  return <div className={`skeleton h-4 ${className}`} />;
}

export function StatusDot({ status }) {
  const colors = {
    REQUESTED: 'bg-warning',
    DRIVER_ASSIGNED: 'bg-blue-400',
    IN_PROGRESS: 'bg-accent',
    COMPLETED: 'bg-success',
    CANCELLED: 'bg-danger',
    AVAILABLE: 'bg-success',
    BUSY: 'bg-warning',
    OFFLINE: 'bg-text-dim',
    ON_TRIP: 'bg-accent',
  };
  return (
    <span className="inline-flex items-center gap-1.5">
      <span className={`w-2 h-2 rounded-full ${colors[status] || 'bg-text-dim'}`} />
      <span className="text-xs font-medium">{status?.replace(/_/g, ' ')}</span>
    </span>
  );
}
