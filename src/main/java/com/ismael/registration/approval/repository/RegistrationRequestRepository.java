package com.ismael.registration.approval.repository;

import com.ismael.registration.approval.model.RegistrationRequest;
import com.ismael.registration.approval.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Long> {
    List<RegistrationRequest> findByApprovedFalse();

    RegistrationRequest findByUserAndApprovedFalse(User user);

    RegistrationRequest findByUserAndApproved(User user, boolean b);
}