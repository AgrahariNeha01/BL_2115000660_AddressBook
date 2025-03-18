package com.example.AddressBook.service;

import com.example.AddressBook.dto.LoginDTO;
import com.example.AddressBook.dto.UserDTO;
import com.example.AddressBook.model.UserModel;
import com.example.AddressBook.repository.UserRepository;
import com.example.AddressBook.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;  // ✅ Injected JwtUtil

    public String register(UserDTO dto) {
        if (repo.findByEmail(dto.getEmail()).isPresent()) {
            return "Email already registered!";
        }

        UserModel user = new UserModel(0, dto.getName(), dto.getEmail(), passwordEncoder.encode(dto.getPassword()), "USER");
        repo.save(user);
        return "User registered successfully!";
    }

    public String login(LoginDTO dto) {
        Optional<UserModel> userOpt = repo.findByEmail(dto.getEmail());
        if (userOpt.isPresent() && passwordEncoder.matches(dto.getPassword(), userOpt.get().getPassword())) {
            return jwtUtil.generateToken(dto.getEmail()); // ✅ Fixed
        }
        return "Invalid email or password!" ;
    }
}
