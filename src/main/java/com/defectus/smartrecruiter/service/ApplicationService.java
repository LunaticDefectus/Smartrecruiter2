package com.defectus.smartrecruiter.service;

import com.defectus.smartrecruiter.dao.entities.*;
import com.defectus.smartrecruiter.dao.repositories.ApplicationRepository;
import com.defectus.smartrecruiter.dao.repositories.JobPostRepository;
import com.defectus.smartrecruiter.dao.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobPostRepository jobPostRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Application> findAllApplications() {
        return applicationRepository.findAll();
    }

    public Optional<Application> findApplicationById(Integer id) {
        return applicationRepository.findById(id);
    }

    public Application saveApplication(Application application) {
        return applicationRepository.save(application);
    }

    public void deleteApplication(Integer id) {
        applicationRepository.deleteById(id);
    }
    public List<Application> findApplicationsByRecruiterEmail(String email) {
        return applicationRepository.findByRecruiterEmail(email);
    }
    public List<Application> getApplicationsByJobPosts(List<JobPost> jobPosts) {
        return applicationRepository.findByJobPostIn(jobPosts);
    }

    public void updateApplicationStatus(Integer applicationId, String status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalStateException("Application not found"));
        application.setStatus(ApplicationStatus.valueOf(status));
        applicationRepository.save(application);
    }

    public void applyForJob(String username, Integer jobId, byte[] cvFile, String fileType, String originalFilename, double pertinenceScore) throws Exception {
        // Fetch job post by ID
        JobPost jobPost = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new Exception("Job not found"));

        // Fetch the user by email
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new Exception("User not found"));

        // Ensure the user is a Candidate
        if (!(user instanceof Candidate)) {
            throw new Exception("User is not a candidate");
        }

        // Cast the user to Candidate
        Candidate applicant = (Candidate) user;

        // Check if the file type is a valid PDF
        if (!"application/pdf".equals(fileType)) {
            throw new IllegalArgumentException("Only PDF files are allowed");
        }

        // Create a new Application
        Application application = new Application();
        application.setJobPost(jobPost);
        application.setApplicant(applicant);
        application.setCvText(cvFile);
        application.setFileType(fileType);
        application.setCvFilename(originalFilename); // Set the file type to PDF
        application.setStatus(ApplicationStatus.PENDING);  // Set default status
        application.setPertinenceScore(pertinenceScore);
        // Save the application
        applicationRepository.save(application);

        System.out.println("Application saved successfully for " + username);
    }




}
