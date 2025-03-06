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

    public AddressBookController(AddressBookService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> add(@RequestBody AddressBookDTO dto) {
        service.add(dto);
        return ResponseEntity.ok("Added Successfully!");
    }

    @GetMapping
    public ResponseEntity<List<AddressBookModel>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressBookModel> getById(@PathVariable int id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody AddressBookDTO dto) {
        return service.update(id, dto) ?
                ResponseEntity.ok("Updated Successfully!") :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        return service.delete(id) ?
                ResponseEntity.ok("Deleted Successfully!") :
                ResponseEntity.notFound().build();
    }
}
