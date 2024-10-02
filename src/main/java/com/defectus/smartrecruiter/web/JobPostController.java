package com.defectus.smartrecruiter.web;

import com.defectus.smartrecruiter.config.CustomUserDetailsService;
import com.defectus.smartrecruiter.dao.entities.Application;
import com.defectus.smartrecruiter.dao.entities.Candidate;
import com.defectus.smartrecruiter.dao.entities.JobPost;
import com.defectus.smartrecruiter.dao.entities.ApplicationStatus;

import com.defectus.smartrecruiter.dao.entities.User;
import com.defectus.smartrecruiter.dao.repositories.JobPostRepository;
import com.defectus.smartrecruiter.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:4200")
@Controller

public class JobPostController {

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private JobPostService jobPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private CVTextExtractorService cvTextExtractorService;

    @Autowired
    private PertinenceService pertinenceService;


    @Autowired
    private CustomUserDetailsService userDetailsService;

    @PostMapping("/jobpost")
    public String submitJobPost(@ModelAttribute JobPost jobPost, Model model) {
        // Get logged-in user's email from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // email is the username in this case

        // Retrieve the user (recruiter) based on the email
        User recruiter = userDetailsService.findUserByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Recruiter not found"));

        // Set the recruiter for the job post
        jobPost.setRecruiter(recruiter);

        // Save the job post
        jobPostService.saveJobPost(jobPost);
        model.addAttribute("jobPost", jobPost);
        return "redirect:/recruiter/dashboard";  // Redirect after successful submission
    }

    @GetMapping("/recruiter/job")
    public String showJobPostForm(Model model, Authentication authentication) {
        String email = authentication.getName();  // Get the logged-in user's email
        User recruiter = userDetailsService.findUserByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Recruiter not found"));
        model.addAttribute("recruiterId", recruiter.getId());  // Pass recruiter ID to the view
        model.addAttribute("jobPost", new JobPost());
        return "JobPost";  // The HTML form will automatically pick up the recruiter ID
    }
    @GetMapping("/recruiter/dashboard")
    public String showRecruiterDashboardCandidate(Model model) {
        // Get logged-in recruiter's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Retrieve recruiter by email
        User recruiter = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Recruiter not found"));

        // Fetch job posts for this recruiter
        List<JobPost> recruiterJobPosts = jobPostService.getJobPostsByRecruiter(recruiter);

        // Pass data to the dashboard view
        model.addAttribute("jobPosts", recruiterJobPosts);
        return "Dashboard";  // Render the recruiter dashboard template
    }


    // Delete job post
    @GetMapping("/recruiter/job/delete/{id}")
    public String deleteJob(@PathVariable Integer id) {
        jobPostService.deleteJobPostById(id);
        return "redirect:/recruiter/dashboard";
    }

    // Toggle job status
    @GetMapping("/recruiter/job/toggle-status/{id}")
    public String toggleJobStatus(@PathVariable Integer id) {
        jobPostService.toggleJobStatus(id);
        return "redirect:/recruiter/dashboard";
    }
    @GetMapping("/recruiter/dashboardc")
    public String showApplications(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Find the recruiter by email
        User recruiter = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Recruiter not found"));

        // Get the recruiter's job posts
        List<JobPost> recruiterJobPosts = jobPostService.getJobPostsByRecruiter(recruiter);

        // Fetch applications for these job posts
        List<Application> applications = applicationService.getApplicationsByJobPosts(recruiterJobPosts);

        // Add applications to the model
        model.addAttribute("applications", applications);

        return "DashboardC"; // Renders the candidate applications view
    }

    // Handle status update for an application
    @PostMapping("/recruiter/dashboardc/update")
    public String updateApplicationStatus(@RequestParam("id") Integer id, @RequestParam("status") String status) {
        applicationService.updateApplicationStatus(id, status);
        // Redirect back to the dashboard without showing the ID or "update" in the URL
        return "redirect:/recruiter/dashboardc";
    }



    // REST endpoint to fetch all job posts
    @GetMapping("/api/jobs")
    @ResponseBody
    public ResponseEntity<List<JobPost>> getAllJobPosts() {
        List<JobPost> jobPosts = jobPostService.findAllJobPosts();
        return ResponseEntity.ok(jobPosts);  // Return job posts as JSON
    }

    // REST endpoint to fetch a single job post by ID
    @PostMapping("/api/jobs/apply")
    public ResponseEntity<Map<String, String>> applyForJob(
            @RequestParam("jobId") Integer jobId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        Map<String, String> response = new HashMap<>();
        try {
            if (file.isEmpty()) {
                response.put("message", "No file uploaded");
                return ResponseEntity.badRequest().body(response);
            }

            byte[] cvFile = file.getBytes();
            String fileType = file.getContentType();
            String originalFilename = file.getOriginalFilename();

            if (!"application/pdf".equals(fileType)) {
                response.put("message", "Only PDF files are allowed");
                return ResponseEntity.badRequest().body(response);
            }

            // Extract CV text and calculate pertinence
            String cvText = cvTextExtractorService.extractTextFromPDF(file);
            JobPost jobPost = jobPostService.findJobById(jobId)
                    .orElseThrow(() -> new IllegalStateException("Job not found with id: " + jobId));
            String jobDescription = jobPost.getJobDescription();
            double pertinenceScore = pertinenceService.calculatePertinence(cvText, jobDescription);

            // Save the application with pertinence score
            applicationService.applyForJob(currentUser.getUsername(), jobId, cvFile, fileType, originalFilename, pertinenceScore);

            response.put("message", "Application submitted successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Failed to submit the application. Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @GetMapping("api/auth/me")
    public ResponseEntity<Map<String, String>> getCurrentUserEmail(Authentication authentication) {
        String email = authentication.getName();
        Map<String, String> response = new HashMap<>();
        response.put("email", email);
        return ResponseEntity.ok(response);
    }


    }
