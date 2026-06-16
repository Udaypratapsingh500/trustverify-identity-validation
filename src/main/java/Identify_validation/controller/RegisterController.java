package Identify_validation.controller;

import Identify_validation.dto.LoginRequest;
import Identify_validation.entity.User;
import Identify_validation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RegisterController {

    @Autowired
    private UserService userService;

    // Register User
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    // Verify Email OTP
    @PostMapping("/verify-email")
    public String verifyEmail(
            @RequestParam String email,
            @RequestParam String otp) {

        return userService.verifyEmailOtp(email, otp);
    }

    // Verify Phone OTP
    @PostMapping("/verify-phone")
    public String verifyPhone(
            @RequestParam String phone,
            @RequestParam String otp) {

        return userService.verifyPhoneOtp(phone, otp);
    }

    // Login API
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        return userService.login(request);
    }

}