package com.defectus.smartrecruiter.dao.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Entity
public class Recruiter extends User {
    private String role = "RECRUITER";
    public Recruiter() {
        super.setRole("RECRUITER");
    }
}
