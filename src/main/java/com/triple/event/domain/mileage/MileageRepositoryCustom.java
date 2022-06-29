package com.triple.event.domain.mileage;

import java.util.Optional;

public interface MileageRepositoryCustom {

    Optional<Mileage> findByUserId(String userId);

    Optional<Mileage> findByUserIdWithUser(String userId);
}
