package com.example.Real_time.Auction.System.controller;

import com.example.Real_time.Auction.System.entity.Bid;
import com.example.Real_time.Auction.System.entity.Item;
import com.example.Real_time.Auction.System.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<Item> createItem(@Valid @RequestBody Item item) {
        return ResponseEntity.ok(itemService.createItem(item));
    }

    @GetMapping
    public ResponseEntity<Page<Item>> getActiveItems(Pageable pageable) {
        return ResponseEntity.ok(itemService.getActiveItems(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Item>> searchItems(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(itemService.searchItems(keyword, pageable));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.getItem(itemId));
    }

    @PostMapping("/{itemId}/bids")
    public ResponseEntity<Bid> placeBid(@PathVariable Long itemId, @Valid @RequestBody Bid bid) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        bid.setUser(new com.example.Real_time.Auction.System.entity.User() {{ setUsername(username); }});
        return ResponseEntity.ok(itemService.placeBid(itemId, bid));
    }

    @GetMapping("/{itemId}/winner")
    public ResponseEntity<Bid> getWinner(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.getWinner(itemId));
    }
}