package com.triple.event.domain.mileage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MileageRepository extends JpaRepository<Mileage, String>, MileageRepositoryCustom {

}
