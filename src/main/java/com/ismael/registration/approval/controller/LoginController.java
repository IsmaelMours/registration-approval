package com.ismael.registration.approval.controller;

import com.ismael.registration.approval.model.RegistrationRequest;
import com.ismael.registration.approval.model.User;
import com.ismael.registration.approval.repository.RegistrationRequestRepository;
import com.ismael.registration.approval.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegistrationRequestRepository registrationRequestRepository;

    @GetMapping
    public String showLoginForm() {
        return "login/login";
    }

    @PostMapping
    public String loginUser(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            HttpSession session,
                            Model model) {

        // Verify user credentials
        User user = userRepository.findByUsernameAndPassword(username, password);

        if (user != null) {
            // Check if the user is approved
            RegistrationRequest registrationRequest = registrationRequestRepository.findByUserAndApproved(user, true);

            if (registrationRequest != null) {
                // User is approved, store user in session
                session.setAttribute("loggedInUser", user);

                // Redirect to a success page
                return "redirect:/login/success";
            } else {
                // User is not approved, redirect to an error page
                model.addAttribute("errorMessage", "Your account is not approved. Please contact the administrator.");
                return "redirect:/login/error";
            }
        } else {
            // Invalid credentials, redirect to an error page
            model.addAttribute("errorMessage", "Invalid username or password");
            return "redirect:/login/error";
        }
    }


    @GetMapping("/success")
    public String loginSuccess(HttpSession session, Model model) {
        // Get the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // You can now use loggedInUser in your view or controller logic
        model.addAttribute("user", loggedInUser);

        return "login/success";
    }

    @GetMapping("/error")
    public String loginError(@RequestParam(name = "errorMessage", required = false) String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "login/error";
    }

    private boolean isUserApproved(User user) {
        // Check if the user is approved based on registration requests
        RegistrationRequest registrationRequest = registrationRequestRepository.findByUserAndApprovedFalse(user);
        return registrationRequest == null;
    }
}
