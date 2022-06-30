package com.triple.clubmileage.domain.place.service;

import com.triple.clubmileage.domain.exception.EntityNotFoundException;
import com.triple.clubmileage.domain.place.Place;
import com.triple.clubmileage.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    public Place getOneById(String placeId) {
        return placeRepository.findById(placeId).orElseThrow(() ->
                new EntityNotFoundException("해당 PlaceId를 가진 장소를 찾을 수 없음"));
    }
}
