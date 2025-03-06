package com.example.AddressBook.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addressbook")  // Ensure table name matches DB
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressBookModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)  // Ensure name is not null
    private String name;

    @Column(nullable = false, length = 15, unique = true)  // Ensure unique phone
    private String phone;

    @Column(nullable = false, length = 100, unique = true)  // Ensure unique email
    private String email;
}
