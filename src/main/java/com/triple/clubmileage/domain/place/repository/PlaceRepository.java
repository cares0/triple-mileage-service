package com.triple.clubmileage.domain.place.repository;

import com.triple.clubmileage.domain.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, String> {
}
