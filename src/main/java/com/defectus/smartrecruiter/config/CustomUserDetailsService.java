package com.defectus.smartrecruiter.config;

import com.defectus.smartrecruiter.dao.entities.User;
import com.defectus.smartrecruiter.dao.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        String role = user.getRole();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())  // use email as the username in UserDetails
                .password(user.getPassword())
                .authorities(Collections.singletonList(authority))
                .build();
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
