package com.portfolio_dev;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AboutRepository extends JpaRepository<About, Long> {
    // Usually, you only need one "About" record. 
    // You can find the first one automatically.
}