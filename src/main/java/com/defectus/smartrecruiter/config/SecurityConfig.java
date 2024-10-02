package com.defectus.smartrecruiter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))  // Use the custom CORS configuration
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/jobs").permitAll()
                        .requestMatchers("/api/auth").permitAll()
                        .requestMatchers("/api/jobs/apply").authenticated()
                        .requestMatchers("/api/auth/me").authenticated()
                        .requestMatchers("/recruiter/**").hasAuthority("RECRUITER")
                        .requestMatchers("/candidate/**").hasAuthority("CANDIDATE")
                        .requestMatchers("/register", "/login", "/css/**").permitAll()
                        .anyRequest().authenticated()
                )
                .userDetailsService(customUserDetailsService)  // Ensure this is wired here
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .successHandler((request, response, authentication) -> {
                            String role = authentication.getAuthorities().iterator().next().getAuthority();
                            if ("RECRUITER".equals(role)) {
                                response.sendRedirect("/recruiter/dashboard");  // Recruiter gets redirected to job posting page
                            } else if ("CANDIDATE".equals(role)) {
                                response.sendRedirect("http://localhost:4200/jobs");  // Or another page for candidates
                            } else {
                                response.sendRedirect("/default");  // Add a default page in case the role isn't matched
                            }
                        })

                        .failureUrl("/login?error")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Use BCrypt for secure password hashing
    }

}