package com.defectus.smartrecruiter.service;

import com.defectus.smartrecruiter.dao.entities.JobPost;
import com.defectus.smartrecruiter.dao.entities.JobStatus;
import com.defectus.smartrecruiter.dao.entities.User;
import com.defectus.smartrecruiter.dao.repositories.JobPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobPostService {

    @Autowired
    private JobPostRepository jobPostRepository;

    public List<JobPost> findAllJobPosts() {
        return jobPostRepository.findAll();
    }

    public Optional<JobPost> findJobById(Integer jobId) {
        return jobPostRepository.findById(jobId);
    }

    public JobPost saveJobPost(JobPost jobPost) {
        return jobPostRepository.save(jobPost);
    }

    public void deleteJobPost(Integer id) {
        jobPostRepository.deleteById(id);
    }

    public List<JobPost> findJobPostsByRecruiterEmail(String email) {
        return jobPostRepository.findByRecruiterEmail(email);
    }
    public List<JobPost> getJobPostsByRecruiter(User recruiter) {
        return jobPostRepository.findByRecruiter(recruiter);
    }
    public List<JobPost> findByRecruiter(User recruiter) {
        return jobPostRepository.findByRecruiter(recruiter);
    }
    public List<JobPost> getAllJobs() {
        return jobPostRepository.findAll();
    }
    public List<JobPost> searchJobs(String keyword) {
        return jobPostRepository.searchJobs(keyword);
    }

    public JobPost getJobPostById(Integer id) {
        return jobPostRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Job post not found"));
    }

    public void deleteJobPostById(Integer id) {
        jobPostRepository.deleteById(id);
    }

    public void toggleJobStatus(Integer id) {
        JobPost jobPost = getJobPostById(id);
        jobPost.setJobStatus(jobPost.getJobStatus() == JobStatus.ACTIVE ? JobStatus.DISABLED : JobStatus.ACTIVE);
        jobPostRepository.save(jobPost);
    }
}

