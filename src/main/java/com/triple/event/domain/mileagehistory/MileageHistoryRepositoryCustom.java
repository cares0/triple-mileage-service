package com.triple.event.domain.mileagehistory;

import java.util.List;

public interface MileageHistoryRepositoryCustom {

    List<MileageHistory> findAllByTypeId(String typeId);
}
