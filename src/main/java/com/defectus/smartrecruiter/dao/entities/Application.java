package com.defectus.smartrecruiter.dao.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String recruiterEmail;


    @ManyToOne
    @JoinColumn(name = "job_post_id")
    @JsonBackReference
    private JobPost jobPost;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private Candidate applicant;
    @Column(columnDefinition = "LONGBLOB")
    @Lob
    private byte[] cvText;

    private String fileType;

    private String cvFilename;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @ManyToMany
    @JoinTable(
            name = "application_skills",
            joinColumns = @JoinColumn(name = "application_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills;

    private double pertinenceScore;
}
