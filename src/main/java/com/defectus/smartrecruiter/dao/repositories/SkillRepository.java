package com.defectus.smartrecruiter.dao.repositories;

import com.defectus.smartrecruiter.dao.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
}
