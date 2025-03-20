package com.example.AddressBook.model;

import com.example.AddressBook.dto.AddressBookDTO;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "addressbook")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressBookModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 15, unique = true)
    private String phone;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    public AddressBookModel(AddressBookDTO d) {
        name = d.getName();
        phone = d.getPhone();
        email = d.getEmail();
    }

    // **Add this new constructor**
    public AddressBookModel(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}


