package com.ismael.registration.approval.controller;

import com.ismael.registration.approval.model.RegistrationRequest;
import com.ismael.registration.approval.model.User;
import com.ismael.registration.approval.repository.RegistrationRequestRepository;
import com.ismael.registration.approval.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegistrationRequestRepository registrationRequestRepository;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "registration/register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password) {
        // Check if username already exists
        if (userRepository.findByUsername(username) != null) {
            // Handle duplicate username error, for example, return an error message or redirect to registration form
            return "redirect:/registration/error?message=Username already exists";
        }

        // Create a new user
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        // Save user to repository
        userRepository.save(user);

        // Create a registration request
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUser(user);
        registrationRequest.setApproved(false); // Admin approval required

        // Save registration request to repository
        registrationRequestRepository.save(registrationRequest);

        // Redirect to a success page or any other desired page
        return "redirect:/registration/success";
    }

    // Handle registration success
    @GetMapping("/success")
    public String registrationSuccess() {
        return "registration/success";
    }

    // Handle registration errors
    @GetMapping("/error")
    public String registrationError(@RequestParam("message") String message, Model model) {
        model.addAttribute("errorMessage", message);
        return "registration/error";
    }


    @PostMapping("/list")
    public String processRegistrationRequests(Model model) {
        // Add logic to process form submissions if needed
        return "redirect:/registration/list";
    }
}
