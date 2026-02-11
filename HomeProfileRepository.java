package com.portfolio_dev;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeProfileRepository extends JpaRepository<HomeProfile, Long> {

}