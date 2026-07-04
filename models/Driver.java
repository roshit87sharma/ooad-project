// Driver Model

public class Driver extends User {
    public enum DriverStatus {
        OFFLINE, AVAILABLE, BUSY, DRIVING_TO_PICKUP, ON_TRIP
    }

    private String licenseNumber;
    private String vehicleNumber;
    private DriverStatus status;
    private double latitude;
    private double longitude;
    private int completedRides;

    public Driver(String userId, String name, String email, String phone, 
                  String licenseNumber, String vehicleNumber) {
        super(userId, name, email, phone);
        this.licenseNumber = licenseNumber;
        this.vehicleNumber = vehicleNumber;
        this.status = DriverStatus.OFFLINE;
        this.completedRides = 0;
    }

    public String getLicenseNumber() { return licenseNumber; }
    public String getVehicleNumber() { return vehicleNumber; }
    public DriverStatus getStatus() { return status; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public int getCompletedRides() { return completedRides; }

    public void setStatus(DriverStatus status) { this.status = status; }
    public void setLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public void incrementCompletedRides() { this.completedRides++; }

    public boolean isAvailable() {
        return status == DriverStatus.AVAILABLE;
    }
}
