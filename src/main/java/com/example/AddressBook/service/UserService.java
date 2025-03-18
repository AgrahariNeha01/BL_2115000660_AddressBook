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
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

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
            emailService.sendSimpleEmail(dto.getEmail(), "Login Alert", "You have successfully logged into your account.");
            return jwtUtil.generateToken(dto.getEmail());
        }
        return "Invalid email or password!";
    }

    public String forgotPassword(String email, String newPassword) {
        Optional<UserModel> userOpt = repo.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "Sorry! We cannot find the user email: " + email;
        }

        UserModel user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        repo.save(user);

        emailService.sendSimpleEmail(email, "Password Changed", "Your password has been successfully updated.");
        return "Password has been changed successfully!";
    }

    public String resetPassword(String email, String currentPassword, String newPassword) {
        Optional<UserModel> userOpt = repo.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "User not found with email: " + email;
        }

        UserModel user = userOpt.get();
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return "Current password is incorrect!";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        repo.save(user);
        return "Password reset successfully!";
    }

    // ✅ Ye method add kiya hai (fix for AuthController)
    public void sendLoginNotification(String email) {
        emailService.sendSimpleEmail(email, "Login Alert", "You have logged in successfully.");
    }
}
