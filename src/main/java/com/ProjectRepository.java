package com.portfolio_dev;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Basic CRUD methods are inherited automatically:
    // .findAll(), .findById(), .save(), .deleteById()
	List<Project> findByNameContainingIgnoreCase(String name);
}