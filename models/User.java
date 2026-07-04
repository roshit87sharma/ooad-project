// Models - Domain entities

public abstract class User {
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private double rating;

    public User(String userId, String name, String email, String phone) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.rating = 5.0;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public double getRating() { return rating; }

    public void setRating(double rating) { this.rating = rating; }
}
