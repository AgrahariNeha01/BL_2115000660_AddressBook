package com.example.AddressBook.service;

import com.example.AddressBook.dto.AddressBookDTO;
import com.example.AddressBook.model.AddressBookModel;
import com.example.AddressBook.repository.AddressBookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressBookService {

    private final AddressBookRepository repo;

    public AddressBookService(AddressBookRepository repo) {
        this.repo = repo;
    }

    public void add(AddressBookDTO dto) {
        AddressBookModel model = new AddressBookModel();
        model.setName(dto.getName());
        model.setPhone(dto.getPhone());
        model.setEmail(dto.getEmail());
        repo.save(model);
    }

    public List<AddressBookModel> getAll() {
        return repo.findAll();
    }

    public Optional<AddressBookModel> getById(int id) {
        return repo.findById(id);
    }

    public boolean update(int id, AddressBookDTO dto) {
        Optional<AddressBookModel> optional = repo.findById(id);
        if (optional.isPresent()) {
            AddressBookModel model = optional.get();
            model.setName(dto.getName());
            model.setPhone(dto.getPhone());
            model.setEmail(dto.getEmail());
            repo.save(model);
            return true;
        }
        return false;
    }

    public boolean delete(int id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}
