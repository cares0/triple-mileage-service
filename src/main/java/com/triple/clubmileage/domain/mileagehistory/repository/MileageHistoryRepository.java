package com.triple.clubmileage.domain.mileagehistory.repository;

import com.triple.clubmileage.domain.mileagehistory.MileageHistory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MileageHistoryRepository extends JpaRepository<MileageHistory, String>, MileageHistoryRepositoryCustom {

    @EntityGraph(attributePaths = {"clubmileage"})
    Optional<MileageHistory> findById(String mileageHistoryId);
}
