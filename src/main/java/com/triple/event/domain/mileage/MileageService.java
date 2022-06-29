package com.triple.event.domain.mileage;

import com.triple.event.domain.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MileageService {

    private final MileageRepository mileageRepository;

    public Mileage getOneByUserId(String userId) {
        return mileageRepository.findOptionalByUserId(userId).orElseThrow(() ->
                new EntityNotFoundException("해당 UserId를 가진 마일리지를 찾을 수 없음"));
    }
}