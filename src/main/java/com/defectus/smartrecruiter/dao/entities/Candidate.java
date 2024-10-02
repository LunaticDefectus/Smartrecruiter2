package com.defectus.smartrecruiter.dao.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Table;

@AllArgsConstructor
@Data
@Entity
public class Candidate extends User{
    private String role = "CANDIDATE";
    public Candidate() {
        super.setRole("CANDIDATE");
    }

}
