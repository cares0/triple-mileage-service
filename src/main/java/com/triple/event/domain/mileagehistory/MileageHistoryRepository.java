package com.triple.event.domain.mileagehistory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MileageHistoryRepository extends JpaRepository<MileageHistory, String>, MileageHistoryRepositoryCustom {

}
