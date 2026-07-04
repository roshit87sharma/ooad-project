// Fare Model

public class Fare {
    private static final double BASE_FARE = 50.0;
    private static final double COST_PER_KM = 15.0;
    private static final double COST_PER_MINUTE = 2.0;

    private double baseFare;
    private double distanceCost;
    private double timeCost;
    private double tax;
    private double totalFare;

    public Fare(double distance, double durationMinutes) {
        this.baseFare = BASE_FARE;
        this.distanceCost = distance * COST_PER_KM;
        this.timeCost = durationMinutes * COST_PER_MINUTE;
        calculateTotalFare();
    }

    private void calculateTotalFare() {
        double subtotal = baseFare + distanceCost + timeCost;
        this.tax = subtotal * 0.05; // 5% tax
        this.totalFare = subtotal + tax;
    }

    public double getBaseFare() { return baseFare; }
    public double getDistanceCost() { return distanceCost; }
    public double getTimeCost() { return timeCost; }
    public double getTax() { return tax; }
    public double getTotalFare() { return totalFare; }

    @Override
    public String toString() {
        return String.format("Fare Details:\n" +
                "Base Fare: Rs.%.2f\n" +
                "Distance Cost: Rs.%.2f\n" +
                "Time Cost: Rs.%.2f\n" +
                "Tax: Rs.%.2f\n" +
                "Total Fare: Rs.%.2f", 
                baseFare, distanceCost, timeCost, tax, totalFare);
    }
}
