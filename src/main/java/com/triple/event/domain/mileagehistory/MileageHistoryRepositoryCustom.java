package com.triple.event.domain.mileagehistory;

import java.util.Optional;

public interface MileageHistoryRepositoryCustom {

    Optional<MileageHistory> findMileageHistoryByTypeId(String typeId);
}
