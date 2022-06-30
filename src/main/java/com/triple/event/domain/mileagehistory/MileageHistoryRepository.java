package com.triple.event.domain.mileagehistory;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MileageHistoryRepository extends JpaRepository<MileageHistory, String>, MileageHistoryRepositoryCustom {

    @EntityGraph(attributePaths = {"event"})
    Optional<MileageHistory> findById(String mileageHistoryId);
}
