package com.re.badminton_court.repository;

import com.re.badminton_court.model.entity.BadmintonCourt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadmintonCourtRepository extends JpaRepository<BadmintonCourt, Long> {
}
