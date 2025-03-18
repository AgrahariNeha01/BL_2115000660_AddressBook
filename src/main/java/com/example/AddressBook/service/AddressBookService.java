package com.example.AddressBook.service;

import com.example.AddressBook.dto.AddressBookDTO;
import com.example.AddressBook.exception.AddressBookException;
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
public class AddressBookService implements IAddressBookService {

    private final AddressBookRepository repo;
    private final List<AddressBookModel> memList = new ArrayList<>();

    public AddressBookService(AddressBookRepository repo) {
        this.repo = repo;
    }

    @PostConstruct
    public void init() {
        memList.addAll(repo.findAll());
    }

    @Override
    public void add(AddressBookDTO dto) {
        AddressBookModel model = new AddressBookModel(0, dto.getName(), dto.getPhone(), dto.getEmail());
        repo.save(model);
        memList.add(model);
    }

    @Override
    public List<AddressBookModel> getAll() {
        return new ArrayList<>(memList);
    }

    @Override
    public Optional<AddressBookModel> getById(int id) {
        return memList.stream().filter(e -> e.getId() == id).findFirst();
    }

    @Override
    public boolean update(int id, AddressBookDTO dto) {
        Optional<AddressBookModel> existing = repo.findById(id);
        if (existing.isPresent()) {
            AddressBookModel model = existing.get();
            model.setName(dto.getName());
            model.setPhone(dto.getPhone());
            model.setEmail(dto.getEmail());
            repo.save(model);
            memList.removeIf(e -> e.getId() == id);
            memList.add(model);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            memList.removeIf(e -> e.getId() == id);
            return true;
        }
        return false;
    }

    @Override
    public void addEntry(String name, String phone) {
        log.info("Adding contact: {} - {}", name, phone);
        AddressBookModel contact = new AddressBookModel(0, name, phone, "");
        repo.save(contact);
    }

    @Override
    public void deleteEntry(String name) {
        log.warn("Deleting contact: {}", name);
        if (repo.existsByName(name)) {
            repo.deleteByName(name);
        } else {
            throw new AddressBookException("Contact not found with name: " + name);
        }
    }
}
