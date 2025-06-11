package com.example.Real_time.Auction.System.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.ZonedDateTime;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @Positive
    @Column(name = "starting_price", nullable = false)
    private Double startingPrice;

    @NotNull
    @Column(name = "auction_end_time", nullable = false)
    private ZonedDateTime auctionEndTime;

    @NotBlank
    @Column(nullable = false)
    private String status;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getStartingPrice() { return startingPrice; }
    public void setStartingPrice(Double startingPrice) { this.startingPrice = startingPrice; }
    public ZonedDateTime getAuctionEndTime() { return auctionEndTime; }
    public void setAuctionEndTime(ZonedDateTime auctionEndTime) { this.auctionEndTime = auctionEndTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}