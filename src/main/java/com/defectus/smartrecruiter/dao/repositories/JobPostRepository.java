package com.defectus.smartrecruiter.dao.repositories;

import com.defectus.smartrecruiter.dao.entities.JobPost;
import com.defectus.smartrecruiter.dao.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost, Integer> {
    List<JobPost> findByRecruiterEmail(String email);

    List<JobPost> findByRecruiter(User recruiter);
    @Query("SELECT j FROM JobPost j WHERE j.title LIKE %:keyword% ")
    List<JobPost> searchJobs(@Param("keyword") String keyword);
}
