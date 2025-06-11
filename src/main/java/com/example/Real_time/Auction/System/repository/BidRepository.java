package com.example.Real_time.Auction.System.repository;

import com.example.Real_time.Auction.System.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BidRepository extends JpaRepository<Bid, Long> {
    @Query("SELECT b FROM Bid b WHERE b.item.id = :itemId ORDER BY b.bidAmount DESC LIMIT 1")
    Bid findHighestBidByItemId(Long itemId);
}