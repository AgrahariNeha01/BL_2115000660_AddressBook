package com.example.AddressBook.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "Operations related to Users") // Swagger Tag
public class UserController {

    @GetMapping("/{id}")
    @Operation(summary = "Get User by ID", description = "Fetch user details using user ID")
    public String getUserById(@Parameter(description = "ID of the user") @PathVariable int id) {
        return "User with ID: " + id;
    }
}
