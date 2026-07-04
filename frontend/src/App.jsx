import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import RiderDashboard from './pages/RiderDashboard';
import DriverDashboard from './pages/DriverDashboard';
import BookRide from './pages/BookRide';
import RideTracking from './pages/RideTracking';
import PaymentPage from './pages/PaymentPage';
import TripPlanner from './pages/TripPlanner';

function App() {
  return (
    <BrowserRouter>
      <div className="min-h-screen bg-primary">
        <Navbar />
        <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/riders" element={<RiderDashboard />} />
            <Route path="/drivers" element={<DriverDashboard />} />
            <Route path="/book" element={<BookRide />} />
            <Route path="/track/:rideId" element={<RideTracking />} />
            <Route path="/track" element={<RideTracking />} />
            <Route path="/payment/:rideId" element={<PaymentPage />} />
            <Route path="/trips" element={<TripPlanner />} />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  );
}

export default App;
