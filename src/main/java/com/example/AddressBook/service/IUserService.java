package com.example.AddressBook.service;

import com.example.AddressBook.dto.LoginDTO;
import com.example.AddressBook.dto.UserDTO;

public interface IUserService {
    String register(UserDTO dto);
    String login(LoginDTO dto);
    String forgotPassword(String email, String newPassword);
    String resetPassword(String email, String currentPassword, String newPassword);
    void sendLoginNotification(String email);
}
