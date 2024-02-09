package com.ismael.registration.approval.controller;

import com.ismael.registration.approval.model.RegistrationRequest;
import com.ismael.registration.approval.repository.RegistrationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private RegistrationRequestRepository registrationRequestRepository;

    @PostMapping("/approve/{id}")
    public String approveRegistrationRequest(@PathVariable Long id) {
        Optional<RegistrationRequest> requestOptional = registrationRequestRepository.findById(id);
        if (requestOptional.isPresent()) {
            RegistrationRequest request = requestOptional.get();
            request.setApproved(true);
            // Perform user creation logic here if needed
            registrationRequestRepository.save(request);
        }
        return "redirect:/admin/registration/list";
    }
}