package com.triple.clubmileage.domain.review.repository;

import com.triple.clubmileage.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, String> {

    @Query("select count(r) from Review r where r.place.id = :placeId")
    Long getCountByPlaceId(@Param("placeId") String placeId);

}
