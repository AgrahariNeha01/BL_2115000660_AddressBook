package com.example.AddressBook.controller;

import com.example.AddressBook.dto.AddressBookDTO;
import com.example.AddressBook.model.AddressBookModel;
import com.example.AddressBook.service.AddressBookService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/addressbook")
public class AddressBookController {

    private final AddressBookService service;

    public AddressBookController(AddressBookService service) {
        this.service = service;
    }

    // ✅ Address Book Entry Add Karega (POST) - Validation ke saath
    @PostMapping("/add")
    public ResponseEntity<String> addEntry(@Valid @RequestBody AddressBookDTO dto) {
        log.info("Adding contact: {}", dto);
        service.add(dto);
        return ResponseEntity.ok("Contact added successfully!");
    }

    // ✅ Sabhi Contacts Fetch Karega (GET)
    @GetMapping("/all")
    public ResponseEntity<List<AddressBookModel>> getAll() {
        log.info("Fetching all contacts");
        return ResponseEntity.ok(service.getAll());
    }

    // ✅ Contact Delete Karega (DELETE)
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteEntry(@RequestParam String name) {
        log.warn("Deleting contact: {}", name);
        service.deleteEntry(name);
        return ResponseEntity.ok("Contact deleted successfully!");
    }
}
