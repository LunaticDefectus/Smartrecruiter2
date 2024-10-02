package com.defectus.smartrecruiter.dao.repositories;

import com.defectus.smartrecruiter.dao.entities.Application;
import com.defectus.smartrecruiter.dao.entities.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    List<Application> findByRecruiterEmail(String email);

    List<Application> findByJobPostIn(List<JobPost> jobPosts);
}
