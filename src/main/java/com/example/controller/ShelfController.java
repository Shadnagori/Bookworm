package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.model.Shelf;
import com.example.model.ShelfItem;
import com.example.model.User;
import com.example.service.IShelf;
import com.example.service.IShelfItemService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/shelf")
public class ShelfController {

    private final IShelf shelfService;
    private final IShelfItemService shelfItemService;

    @Autowired
    public ShelfController(IShelf shelfService, IShelfItemService shelfItemService) {
        this.shelfService = shelfService;
        this.shelfItemService = shelfItemService;
    }

    // ---------- Create or update a shelf ----------
    @PostMapping
    public ResponseEntity<Shelf> createOrUpdateShelf(@RequestBody Shelf shelf) {
        Shelf savedShelf = shelfService.createOrUpdateShelf(shelf);
        return ResponseEntity.ok(savedShelf);
    }

    // ---------- Get shelf by ID (optional) ----------
    @GetMapping("/{shelfId}")
    public ResponseEntity<Shelf> getShelfById(@PathVariable int shelfId) {
        return shelfService.getShelfById(shelfId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------- Add item to shelf ----------
    @PostMapping("/items")
    public ResponseEntity<ShelfItem> addShelfItem(@RequestBody ShelfItem item) {
        ShelfItem savedItem = shelfItemService.saveShelfItem(item);
        return ResponseEntity.ok(savedItem);
    }

    // ---------- Delete item from shelf ----------
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<String> deleteShelfItem(@PathVariable int itemId) {
        shelfItemService.deleteShelfItem(itemId);
        return ResponseEntity.ok("Shelf item deleted");
    }

    // ---------- Get logged-in user's shelf with purchased & rented items ----------
    @GetMapping("/my-shelf")
    public ResponseEntity<?> getUserShelf(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }

        Optional<Shelf> shelfOpt = shelfService.getShelfByUserId(user.getUserId());
        if (shelfOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Shelf not found");
        }

        int shelfId = shelfOpt.get().getShelfId();
        List<ShelfItem> purchasedItems = shelfItemService.getPurchasedItemsByShelfId(shelfId);
        List<ShelfItem> rentedItems = shelfItemService.getRentedItemsByShelfId(shelfId);

        Map<String, Object> response = new HashMap<>();
        response.put("purchasedItems", purchasedItems);
        response.put("rentedItems", rentedItems);

        return ResponseEntity.ok(response);
    }
}