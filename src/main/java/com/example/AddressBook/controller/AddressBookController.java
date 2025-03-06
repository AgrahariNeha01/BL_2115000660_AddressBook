package com.example.AddressBook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/addressbook")
public class AddressBookController {

    private Map<Integer, String> addressBook = new HashMap<>();

    @GetMapping
    public ResponseEntity<Map<Integer, String>> getAll() {
        return ResponseEntity.ok(addressBook);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable int id) {
        return addressBook.containsKey(id) ? ResponseEntity.ok(addressBook.get(id)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<String> add(@RequestBody Map<String, Object> requestData) {
        int id = (int) requestData.get("id");
        String name = (String) requestData.get("name");

        addressBook.put(id, name);
        return ResponseEntity.ok("Added Successfully!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Map<String, Object> requestData) {
        if (addressBook.containsKey(id)) {
            String name = (String) requestData.get("name");
            addressBook.put(id, name);
            return ResponseEntity.ok("Updated Successfully!");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        if (addressBook.remove(id) != null) {
            return ResponseEntity.ok("Deleted Successfully!");
        }
        return ResponseEntity.notFound().build();
    }
}
