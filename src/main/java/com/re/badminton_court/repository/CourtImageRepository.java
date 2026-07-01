package com.re.badminton_court.repository;

import com.re.badminton_court.model.entity.CourtImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourtImageRepository extends JpaRepository<CourtImage, Long> {
    List<CourtImage> findByBadmintonCourtId(Long courtId);
    void deleteByBadmintonCourtId(Long courtId);
}
