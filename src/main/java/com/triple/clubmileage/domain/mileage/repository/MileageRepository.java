package com.triple.clubmileage.domain.mileage.repository;

import com.triple.clubmileage.domain.mileage.Mileage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MileageRepository extends JpaRepository<Mileage, String>, MileageRepositoryCustom {

}
