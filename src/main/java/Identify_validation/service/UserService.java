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

        user.setEmailVerified(false);

        // Phone OTP use nahi kar rahe
        user.setPhoneVerified(true);

        user.setAccountActive(false);

        Random random = new Random();

        String emailOtp = String.valueOf(100000 + random.nextInt(900000));

        user.setEmailOtp(emailOtp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("TrustVerify Email OTP");
        message.setText(
                "Hello " + user.getName()
                        + "\n\nYour OTP is : " + emailOtp
                        + "\n\nPlease do not share this OTP."
        );

        mailSender.send(message);

        System.out.println("========================");
        System.out.println("EMAIL OTP : " + emailOtp);
        System.out.println("========================");

        return userRepository.save(user);
    }

    public String verifyEmailOtp(String email, String otp) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getEmailOtp() != null &&
                user.getEmailOtp().equals(otp)) {

            user.setEmailVerified(true);

            // Email verify hote hi account active
            user.setAccountActive(true);

            userRepository.save(user);

            return "Email Verified Successfully";
        }

        return "Invalid OTP";
    }

    // Dummy method (phone OTP use nahi kar rahe)
    public String verifyPhoneOtp(String phone, String otp) {
        return "Phone OTP Verification Disabled";
    }

    public String login(LoginRequest request) {

        User user = userRepository
                .findByEmailAndPassword(
                        request.getEmail(),
                        request.getPassword())
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