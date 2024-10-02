package com.defectus.smartrecruiter.service;

import com.defectus.smartrecruiter.dao.entities.Candidate;
import com.defectus.smartrecruiter.dao.entities.Recruiter;
import com.defectus.smartrecruiter.dao.entities.User;
import com.defectus.smartrecruiter.dao.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerNewUser(User user, String role) {
        if ("CANDIDATE".equals(role)) {
            Candidate candidate = new Candidate();
            candidate.setUsername(user.getUsername());
            candidate.setFirstName(user.getFirstName());
            candidate.setLastName(user.getLastName());
            candidate.setEmail(user.getEmail());
            candidate.setPassword(passwordEncoder.encode(user.getPassword()));
            candidate.setRole("ROLE_CANDIDATE");
            userRepository.save(candidate);
        } else if ("RECRUITER".equals(role)) {
            Recruiter recruiter = new Recruiter();
            recruiter.setUsername(user.getUsername());
            recruiter.setFirstName(user.getFirstName());
            recruiter.setLastName(user.getLastName());
            recruiter.setEmail(user.getEmail());
            recruiter.setPassword(passwordEncoder.encode(user.getPassword()));
            recruiter.setRole("ROLE_RECRUITER");
            userRepository.save(recruiter);
        }
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
