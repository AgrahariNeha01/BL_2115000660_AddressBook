package com.example.AddressBook.service;

import com.example.AddressBook.dto.AddressBookDTO;
import com.example.AddressBook.model.AddressBookModel;

import java.util.List;
import java.util.Optional;

public interface IAddressBookService {
    void add(AddressBookDTO dto);
    List<AddressBookModel> getAll();
    Optional<AddressBookModel> getById(int id);
    boolean update(int id, AddressBookDTO dto);
    boolean delete(int id);
    void addEntry(String name, String phone);
    void deleteEntry(String name);

    // Redis-specific methods
//    void saveToCache(int id, AddressBookModel model);
//    Optional<AddressBookModel> getFromCache(int id);
//    void deleteFromCache(int id);
}
