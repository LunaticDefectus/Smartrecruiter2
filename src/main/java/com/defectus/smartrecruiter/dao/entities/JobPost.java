package com.defectus.smartrecruiter.dao.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class JobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String companyName;
    private String location;
    private String jobType;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date jobDate ;

    @Column(columnDefinition = "LONGTEXT")
    @Lob
    private String jobDescription;

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    @JsonBackReference
    private User recruiter;

    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;

    @ManyToMany
    @JoinTable(
            name = "jobpost_skills",
            joinColumns = @JoinColumn(name = "jobpost_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> requiredSkills;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Application> applications;
}

