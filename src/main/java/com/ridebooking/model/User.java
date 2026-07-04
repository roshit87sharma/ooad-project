package com.ridebooking.model;

import jakarta.persistence.*;

/**
 * User — matches UML class diagram (abstract base class).
 * Attributes: userId, name, phone, email.
 * Methods: login(), logout().
 * Inheritance: User → Rider, User → Driver.
 * Uses @MappedSuperclass so Rider and Driver get their own tables.
 */
@MappedSuperclass
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    // Protected constructors (for subclasses only)
    protected User() {}

    protected User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // UML diagram methods
    public abstract void login();
    public abstract void logout();

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
