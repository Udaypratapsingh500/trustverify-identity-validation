package Identify_validation.service;

import Identify_validation.dto.LoginRequest;
import Identify_validation.entity.User;
import Identify_validation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    public User registerUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByPhone(user.getPhone())) {
            throw new RuntimeException("Phone already exists");
        }

        // Customer ID Generate
        String customerId = "TV" + System.currentTimeMillis();
        user.setCustomerId(customerId);

        user.setEmailVerified(false);
        user.setPhoneVerified(false);
        user.setAccountActive(false);

        Random random = new Random();

        String emailOtp =
                String.valueOf(100000 + random.nextInt(900000));

        String phoneOtp =
                String.valueOf(100000 + random.nextInt(900000));

        user.setEmailOtp(emailOtp);
        user.setPhoneOtp(phoneOtp);

        // Email Send
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(user.getEmail());

        message.setSubject(
                "TrustVerify Registration Verification"
        );

        message.setText(
                "Hello " + user.getName() +
                        "\n\nCustomer ID : " + customerId +
                        "\nEmail OTP : " + emailOtp +
                        "\n\nPlease verify your account." +
                        "\n\nTrustVerify Team"
        );

        mailSender.send(message);

        // Console Print
        System.out.println("================================");
        System.out.println("CUSTOMER ID : " + customerId);
        System.out.println("EMAIL OTP   : " + emailOtp);
        System.out.println("PHONE OTP   : " + phoneOtp);
        System.out.println("================================");

        return userRepository.save(user);
    }

    public String verifyEmailOtp(String email, String otp) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (user.getEmailOtp() != null &&
                user.getEmailOtp().equals(otp)) {

            user.setEmailVerified(true);

            // Demo Mode
            user.setPhoneVerified(true);

            // Activate Account
            user.setAccountActive(true);

            userRepository.save(user);

            return "Email Verified Successfully";
        }

        return "Invalid OTP";
    }

    public String verifyPhoneOtp(String phone, String otp) {

        User user = userRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (user.getPhoneOtp() != null &&
                user.getPhoneOtp().equals(otp)) {

            user.setPhoneVerified(true);

            if (user.isEmailVerified()) {
                user.setAccountActive(true);
            }

            userRepository.save(user);

            return "Phone Verified Successfully";
        }

        return "Invalid OTP";
    }

    public String login(LoginRequest request) {

        User user = userRepository
                .findByEmailAndPassword(
                        request.getEmail(),
                        request.getPassword()
                )
                .orElse(null);

        if (user == null) {
            return "Invalid Email or Password";
        }

        if (!user.isAccountActive()) {
            return "Please verify your account";
        }

        return "Login Successful";
    }
}