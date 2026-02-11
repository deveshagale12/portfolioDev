package com.portfolio_dev;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkRepository extends JpaRepository<WorkExperience, Long> {
    // This must extend JpaRepository to get findById, deleteById, save, etc.
}