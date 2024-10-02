package com.defectus.smartrecruiter.web;

import com.defectus.smartrecruiter.dao.entities.JobPost;
import com.defectus.smartrecruiter.dao.entities.Application;
import com.defectus.smartrecruiter.service.JobPostService;
import com.defectus.smartrecruiter.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

  /*  @Autowired
    private JobPostService jobPostService;

    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/dashboard")
    public String showRecruiterDashboard(Authentication authentication, Model model) {
        String recruiterEmail = authentication.getName();
        List<JobPost> jobPosts = jobPostService.findJobPostsByRecruiterEmail(recruiterEmail);

        model.addAttribute("jobPosts", jobPosts);
        return "Dashboard";
    }

    @GetMapping("/recruiter/candidates")
    public String showCandidatesDashboard(Authentication authentication, Model model) {
        String recruiterEmail = authentication.getName();
        List<Application> applications = applicationService.findApplicationsByRecruiterEmail(recruiterEmail);

        model.addAttribute("applications", applications);
        return "DashboardC";
    }*/
}
