package com.triple.clubmileage.domain.mileage.repository;

import com.triple.clubmileage.domain.mileage.Mileage;

import java.util.Optional;

public interface MileageRepositoryCustom {

    Optional<Mileage> findByUserId(String userId);

    Optional<Mileage> findByUserIdWithUser(String userId);
}
