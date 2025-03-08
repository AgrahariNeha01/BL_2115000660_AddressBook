package com.example.AddressBook.service;

import com.example.AddressBook.dto.AddressBookDTO;
import com.example.AddressBook.model.AddressBookModel;
import com.example.AddressBook.repository.AddressBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class AddressBookService {

    private final AddressBookRepository repo;
    private final List<AddressBookModel> memList = new ArrayList<>(); // ? In-memory List

    public AddressBookService(AddressBookRepository repo) {
        this.repo = repo;
    }

    // ? Jab program start ho, tab DB se saara data list me load ho
    @PostConstruct
    public void init() {
        memList.addAll(repo.findAll());
    }

    public void add(AddressBookDTO dto) {
        AddressBookModel model = new AddressBookModel(0, dto.getName(), dto.getPhone(), dto.getEmail());
        repo.save(model);         // ? DB me save
        memList.add(model);       // ? List me bhi add
    }

    public List<AddressBookModel> getAll() {
        return new ArrayList<>(memList); // ? Memory se return karna hai
    }

    public Optional<AddressBookModel> getById(int id) {
        return memList.stream().filter(e -> e.getId() == id).findFirst(); // ? List se fetch karna hai
    }

    public boolean update(int id, AddressBookDTO dto) {
        Optional<AddressBookModel> existing = repo.findById(id);
        if (existing.isPresent()) {
            AddressBookModel model = existing.get();
            model.setName(dto.getName());
            model.setPhone(dto.getPhone());
            model.setEmail(dto.getEmail());
            repo.save(model);    // ? DB me update
            memList.removeIf(e -> e.getId() == id); // ? List se purana data hatao
            memList.add(model);  // ? List me naya data daalo
            return true;
        }
        return false;
    }

    public boolean delete(int id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);   // ? DB se delete
            memList.removeIf(e -> e.getId() == id); // ? List se bhi delete
            return true;
        }
        return false;
    }
    public void addEntry(String name, String phone) {
        log.info("Adding contact: {} - {}", name, phone);
        AddressBookModel contact = new AddressBookModel(0, name, phone, ""); // Email empty rakh diya
        repo.save(contact);
    }


    public void deleteEntry(String name) {
        log.warn("Deleting contact: {}", name);
        repo.deleteByName(name);
    }
}
