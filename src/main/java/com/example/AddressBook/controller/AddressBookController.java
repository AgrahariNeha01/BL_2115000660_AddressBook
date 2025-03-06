package com.example.AddressBook.controller;

import com.example.AddressBook.dto.AddressBookDTO;
import com.example.AddressBook.model.AddressBookModel;
import com.example.AddressBook.service.AddressBookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addressbook")
public class AddressBookController {

    private final AddressBookService service;

    // ✅ Service Inject Ho Chuka Hai (Constructor Injection)
    public AddressBookController(AddressBookService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> add(@RequestBody AddressBookDTO dto) {
        service.add(dto);  // ✅ Service Ka Use
        return ResponseEntity.ok("Added Successfully!");
    }

    @GetMapping
    public ResponseEntity<List<AddressBookModel>> getAll() {
        return ResponseEntity.ok(service.getAll());  // ✅ Service Se Data Fetch
    }
}


