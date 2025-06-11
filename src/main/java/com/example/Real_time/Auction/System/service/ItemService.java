package com.example.Real_time.Auction.System.service;

import com.example.Real_time.Auction.System.entity.Bid;
import com.example.Real_time.Auction.System.entity.Item;
import com.example.Real_time.Auction.System.repository.BidRepository;
import com.example.Real_time.Auction.System.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.chrono.ChronoZonedDateTime;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Item createItem(Item item) {
        item.setStatus("OPEN");
        return itemRepository.save(item);
    }

    public Page<Item> getActiveItems(Pageable pageable) {
        return itemRepository.findByStatus("OPEN", pageable);
    }

    public Page<Item> searchItems(String keyword, Pageable pageable) {
        return itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable);
    }

    public Item getItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        checkAndCloseAuction(item);
        return item;
    }

    @Transactional
    public Bid placeBid(Long itemId, Bid bid) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        if (!item.getStatus().equals("OPEN")) {
            throw new IllegalStateException("Auction is closed");
        }

        if (item.getAuctionEndTime().isBefore(ChronoZonedDateTime.from(LocalDateTime.now()))) {
            item.setStatus("CLOSED");
            itemRepository.save(item);
            throw new IllegalStateException("Auction has ended");
        }

        Bid highestBid = bidRepository.findHighestBidByItemId(itemId);
        if (highestBid != null && bid.getBidAmount() <= highestBid.getBidAmount()) {
            throw new IllegalArgumentException("Bid amount must be higher than current highest bid");
        }

        if (bid.getBidAmount() <= item.getStartingPrice()) {
            throw new IllegalArgumentException("Bid amount must be higher than starting price");
        }

        bid.setItem(item);
        bid.setBidTime(LocalDateTime.now());
        Bid savedBid = bidRepository.save(bid);

        // Broadcast new high bid to WebSocket subscribers
        messagingTemplate.convertAndSend("/topic/items/" + itemId, savedBid);

        return savedBid;
    }

    public Bid getWinner(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        checkAndCloseAuction(item);
        if (!item.getStatus().equals("CLOSED")) {
            throw new IllegalStateException("Auction is still open");
        }

        Bid highestBid = bidRepository.findHighestBidByItemId(itemId);
        if (highestBid == null) {
            throw new EntityNotFoundException("No bids placed for this item");
        }
        return highestBid;
    }

    @Scheduled(fixedRate = 60000) // Run every minute
    public void closeExpiredAuctions() {
        int page = 0;
        int pageSize = 100;
        Page<Item> itemPage;

        do {
            Pageable pageable = PageRequest.of(page, pageSize);
            itemPage = itemRepository.findByStatus("OPEN", pageable);
            itemPage.getContent().forEach(item -> {
                if (item.getAuctionEndTime().isBefore(ChronoZonedDateTime.from(LocalDateTime.now()))) {
                    item.setStatus("CLOSED");
                    itemRepository.save(item);
                    // Optionally notify clients of auction close
                    messagingTemplate.convertAndSend("/topic/items/" + item.getId(),
                            new BidNotification("Auction closed for item " + item.getId()));
                }
            });
            page++;
        } while (itemPage.hasNext());
    }

    private void checkAndCloseAuction(Item item) {
        if (item.getStatus().equals("OPEN") && item.getAuctionEndTime().isBefore(ChronoZonedDateTime.from(LocalDateTime.now()))) {
            item.setStatus("CLOSED");
            itemRepository.save(item);
            // Optionally notify clients of auction close
            messagingTemplate.convertAndSend("/topic/items/" + item.getId(),
                    new BidNotification("Auction closed for item " + item.getId()));
        }
    }
}

// Helper class for WebSocket notifications
class BidNotification {
    private String message;

    public BidNotification(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}