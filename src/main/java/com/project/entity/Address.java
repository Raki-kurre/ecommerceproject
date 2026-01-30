package com.project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String house;
    private String area;
    private String landmark;
    private String city;
    private String pincode;

    private Double latitude;
    private Double longitude;

    private boolean selected; // 🔥 IMPORTANT

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ===== GETTERS & SETTERS =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getHouse() { return house; }
    public void setHouse(String house) { this.house = house; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getLandmark() { return landmark; }
    public void setLandmark(String landmark) { this.landmark = landmark; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}