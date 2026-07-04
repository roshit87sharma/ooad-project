import { Link, useLocation } from 'react-router-dom';
import { useState } from 'react';

const navLinks = [
  { path: '/',        label: 'Home',      icon: '🏠' },
  { path: '/book',    label: 'Book Ride',  icon: '🚗' },
  { path: '/riders',  label: 'Riders',     icon: '👤' },
  { path: '/drivers', label: 'Drivers',    icon: '🚕' },
  { path: '/track',   label: 'Track',      icon: '📍' },
  { path: '/trips',   label: 'Trip Plan',  icon: '🚆' },
];

export default function Navbar() {
  const location = useLocation();
  const [mobileOpen, setMobileOpen] = useState(false);

  return (
    <nav className="bg-surface border-b border-border sticky top-0 z-50 backdrop-blur-xl bg-surface/90">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <Link to="/" className="flex items-center gap-2 group">
            <div className="w-9 h-9 rounded-xl bg-gradient-to-br from-accent to-emerald-400 flex items-center justify-center text-lg font-bold text-primary group-hover:scale-110 transition-transform">
              R
            </div>
            <span className="text-xl font-bold text-text tracking-tight">
              Ride<span className="text-accent">Flow</span>
            </span>
          </Link>

          {/* Desktop Nav */}
          <div className="hidden md:flex items-center gap-1">
            {navLinks.map(link => (
              <Link
                key={link.path}
                to={link.path}
                className={`px-4 py-2 rounded-lg text-sm font-medium transition-all duration-200 ${
                  location.pathname === link.path
                    ? 'bg-accent/15 text-accent'
                    : 'text-text-dim hover:text-text hover:bg-surface-2'
                }`}
              >
                <span className="mr-1.5">{link.icon}</span>
                {link.label}
              </Link>
            ))}
          </div>

          {/* Mobile toggle */}
          <button
            onClick={() => setMobileOpen(!mobileOpen)}
            className="md:hidden p-2 rounded-lg text-text-dim hover:text-text hover:bg-surface-2"
          >
            {mobileOpen ? '✕' : '☰'}
          </button>
        </div>

        {/* Mobile Nav */}
        {mobileOpen && (
          <div className="md:hidden pb-4 animate-fade-in">
            {navLinks.map(link => (
              <Link
                key={link.path}
                to={link.path}
                onClick={() => setMobileOpen(false)}
                className={`block px-4 py-3 rounded-lg text-sm font-medium transition-all ${
                  location.pathname === link.path
                    ? 'bg-accent/15 text-accent'
                    : 'text-text-dim hover:text-text hover:bg-surface-2'
                }`}
              >
                <span className="mr-2">{link.icon}</span>
                {link.label}
              </Link>
            ))}
          </div>
        )}
      </div>
    </nav>
  );
}
