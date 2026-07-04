import { Link } from 'react-router-dom';
import { Card, Button, Badge } from '../components/UI';

const features = [
  {
    title: 'Book a Ride',
    desc: 'Choose from Economy, Premium, or Shared rides with real-time fare estimation.',
    icon: '🚗',
    link: '/book',
    pattern: 'Factory + Strategy Pattern',
    color: 'from-emerald-500/20 to-cyan-500/20',
  },
  {
    title: 'Rider Dashboard',
    desc: 'View your profile, booking history, and manage your rides.',
    icon: '👤',
    link: '/riders',
    pattern: 'Spring MVC + JPA',
    color: 'from-violet-500/20 to-purple-500/20',
  },
  {
    title: 'Driver Dashboard',
    desc: 'Accept rides, update your status, and manage your trips.',
    icon: '🚕',
    link: '/drivers',
    pattern: 'State Pattern',
    color: 'from-amber-500/20 to-orange-500/20',
  },
  {
    title: 'Track Ride',
    desc: 'Real-time ride tracking with live status updates.',
    icon: '📍',
    link: '/track',
    pattern: 'State Pattern',
    color: 'from-blue-500/20 to-indigo-500/20',
  },
  {
    title: 'Trip Planner',
    desc: 'Plan multi-modal trips combining cab and metro services.',
    icon: '🚆',
    link: '/trips',
    pattern: 'Facade + Adapter Pattern',
    color: 'from-rose-500/20 to-pink-500/20',
  },
];

const patterns = [
  { name: 'Strategy', type: 'Behavioral', desc: 'Fare calculation with swappable algorithms', icon: '🎯' },
  { name: 'Factory', type: 'Creational', desc: 'Ride object creation based on type', icon: '🏭' },
  { name: 'Adapter', type: 'Structural', desc: 'Integrates external Razorpay & Metro APIs', icon: '🔌' },
  { name: 'Facade', type: 'Structural', desc: 'Simplifies multi-modal trip planning', icon: '🏛️' },
  { name: 'State', type: 'Behavioral', desc: 'Ride lifecycle state machine', icon: '🔄' },
];

export default function Home() {
  return (
    <div className="animate-fade-in">
      {/* Hero */}
      <div className="text-center py-16 relative">
        <div className="absolute inset-0 bg-gradient-to-b from-accent/5 to-transparent rounded-3xl" />
        <div className="relative">
          <Badge color="accent" className="mb-4">OOAD Mini Project — 10/10</Badge>
          <h1 className="text-5xl sm:text-6xl font-extrabold tracking-tight mb-4">
            Ride<span className="text-accent">Flow</span>
          </h1>
          <p className="text-xl text-text-dim max-w-2xl mx-auto mb-8">
            Online Ride Booking System built with Spring Boot MVC, JPA, and
            5 Design Patterns demonstrating SOLID principles.
          </p>
          <div className="flex gap-4 justify-center flex-wrap">
            <Link to="/book">
              <Button size="lg">🚗  Book a Ride</Button>
            </Link>
            <Link to="/trips">
              <Button variant="secondary" size="lg">🚆  Plan a Trip</Button>
            </Link>
          </div>
        </div>
      </div>

      {/* Feature Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-12">
        {features.map((f, i) => (
          <Link to={f.link} key={i}>
            <Card className={`h-full bg-gradient-to-br ${f.color} border-transparent hover:border-accent/40 group cursor-pointer`}>
              <div className="text-4xl mb-4 group-hover:scale-110 transition-transform">{f.icon}</div>
              <h3 className="text-lg font-bold mb-1">{f.title}</h3>
              <p className="text-sm text-text-dim mb-3">{f.desc}</p>
              <Badge color="dim">{f.pattern}</Badge>
            </Card>
          </Link>
        ))}
      </div>

      {/* Design Patterns Section */}
      <div className="mt-16">
        <h2 className="text-2xl font-bold mb-6 text-center">
          Design Patterns <span className="text-accent">Implemented</span>
        </h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-3">
          {patterns.map(p => (
            <Card key={p.name} className="text-center py-5">
              <div className="text-3xl mb-2">{p.icon}</div>
              <h4 className="font-bold text-accent">{p.name}</h4>
              <Badge color="dim" className="my-1">{p.type}</Badge>
              <p className="text-xs text-text-dim mt-1">{p.desc}</p>
            </Card>
          ))}
        </div>
      </div>

      {/* Architecture */}
      <Card className="mt-12 text-center">
        <h3 className="text-lg font-bold mb-4">System Architecture</h3>
        <div className="flex flex-wrap items-center justify-center gap-3 text-sm">
          <Badge color="accent">Controller Layer</Badge>
          <span className="text-accent">→</span>
          <Badge color="accent">Service Layer</Badge>
          <span className="text-accent">→</span>
          <Badge color="accent">Repository Layer</Badge>
          <span className="text-accent">→</span>
          <Badge color="accent">H2 Database</Badge>
        </div>
        <p className="text-text-dim text-sm mt-4">
          Spring Boot 3.4 · JPA/Hibernate · H2 In-Memory DB · React + Tailwind CSS
        </p>
      </Card>
    </div>
  );
}
