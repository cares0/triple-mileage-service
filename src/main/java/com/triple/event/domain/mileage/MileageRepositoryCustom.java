package com.triple.event.domain.mileage;

import java.util.Optional;

public interface MileageRepositoryCustom {

    Optional<Mileage> findOptionalByUserId(String userId);
}
