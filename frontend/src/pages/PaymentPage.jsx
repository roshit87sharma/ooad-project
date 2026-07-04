import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Card, Button, Badge, Select } from '../components/UI';
import { paymentAPI } from '../services/api';

export default function PaymentPage() {
  const { rideId } = useParams();
  const navigate = useNavigate();
  const [mode, setMode] = useState('CARD');
  const [loading, setLoading] = useState(false);
  const [payment, setPayment] = useState(null);
  const [error, setError] = useState('');

  const handlePayment = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await paymentAPI.process(rideId, mode);
      setPayment(res.data);
    } catch (e) {
      setError(e.response?.data?.message || 'Payment failed');
    }
    setLoading(false);
  };

  if (payment) {
    return (
      <div className="max-w-lg mx-auto animate-slide-up">
        <Card glow className="text-center">
          <div className="text-6xl mb-4">💳</div>
          <h2 className="text-2xl font-bold text-success mb-2">Payment Successful!</h2>
          <p className="text-text-dim mb-6">
            Processed via <Badge color="accent">Adapter Pattern → Razorpay SDK</Badge>
          </p>

          <div className="bg-surface-2 rounded-xl p-6 text-left space-y-3 mb-6">
            <div className="flex justify-between items-center">
              <span className="text-text-dim text-sm">Payment ID</span>
              <span className="font-mono font-bold text-accent">#{payment.paymentId}</span>
            </div>
            <div className="flex justify-between items-center">
              <span className="text-text-dim text-sm">Amount</span>
              <span className="text-2xl font-bold text-accent">₹{payment.amount?.toFixed(2)}</span>
            </div>
            <div className="flex justify-between items-center">
              <span className="text-text-dim text-sm">Mode</span>
              <Badge color="accent">{payment.paymentMode}</Badge>
            </div>
            <div className="flex justify-between items-center">
              <span className="text-text-dim text-sm">Status</span>
              <Badge color="success">{payment.paymentStatus}</Badge>
            </div>
            <div className="flex justify-between items-center">
              <span className="text-text-dim text-sm">Time</span>
              <span className="text-sm">{payment.transactionTime && new Date(payment.transactionTime).toLocaleString()}</span>
            </div>
          </div>

          {/* Adapter Pattern Explanation */}
          <div className="bg-accent/5 border border-accent/20 rounded-xl p-4 text-left mb-6">
            <p className="text-xs font-bold text-accent mb-2">🔌 ADAPTER PATTERN FLOW</p>
            <div className="flex items-center gap-2 text-xs text-text-dim flex-wrap">
              <Badge color="dim">PaymentService</Badge>
              <span className="text-accent">→</span>
              <Badge color="accent">PaymentGateway (Interface)</Badge>
              <span className="text-accent">→</span>
              <Badge color="dim">RazorpayAdapter</Badge>
              <span className="text-accent">→</span>
              <Badge color="warning">Razorpay SDK</Badge>
            </div>
          </div>

          <Button onClick={() => navigate('/')} className="w-full">
            🏠  Back to Home
          </Button>
        </Card>
      </div>
    );
  }

  return (
    <div className="max-w-lg mx-auto animate-fade-in">
      <h1 className="text-3xl font-bold mb-2">Process <span className="text-accent">Payment</span></h1>
      <p className="text-text-dim mb-8">
        Adapter Pattern — PaymentGateway interface adapts Razorpay SDK
      </p>

      <Card>
        <div className="text-center mb-6">
          <p className="text-text-dim text-sm">Ride #{rideId}</p>
          <p className="text-4xl font-bold text-accent mt-2">💳</p>
        </div>

        <Select
          label="Payment Method"
          value={mode}
          onChange={e => setMode(e.target.value)}
          options={[
            { value: 'CARD', label: '💳 Credit/Debit Card' },
            { value: 'UPI', label: '📱 UPI' },
            { value: 'WALLET', label: '👛 Digital Wallet' },
            { value: 'CASH', label: '💵 Cash' },
          ]}
        />

        {error && (
          <div className="mt-4 bg-danger/10 border border-danger/30 rounded-xl p-4 text-danger text-sm">
            {error}
          </div>
        )}

        <Button onClick={handlePayment} loading={loading} size="lg" className="w-full mt-6">
          Pay Now
        </Button>

        <p className="text-center text-xs text-text-dim mt-4">
          Secured by Razorpay Adapter Pattern
        </p>
      </Card>
    </div>
  );
}
