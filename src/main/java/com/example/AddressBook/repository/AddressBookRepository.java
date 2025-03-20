package com.example.AddressBook.repository;

import com.example.AddressBook.model.AddressBookModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressBookRepository extends JpaRepository<AddressBookModel, Integer> {
    Optional<AddressBookModel> findByName(String name);
    boolean existsByName(String name);
}
