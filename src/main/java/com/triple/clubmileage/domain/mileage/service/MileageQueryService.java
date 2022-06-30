package com.triple.clubmileage.domain.mileage.service;

import com.triple.clubmileage.domain.exception.EntityNotFoundException;
import com.triple.clubmileage.domain.mileage.Mileage;
import com.triple.clubmileage.domain.mileage.repository.MileageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MileageQueryService {

    private final MileageRepository mileageRepository;

    public Mileage getOneByUserId(String userId) {
        return mileageRepository.findByUserId(userId).orElseThrow(() ->
                new EntityNotFoundException("해당 UserId를 가진 마일리지를 찾을 수 없음"));
    }

    public Mileage getOneByUserIdWithUser(String userId) {
        return mileageRepository.findByUserIdWithUser(userId).orElseThrow(() ->
                new EntityNotFoundException("해당 UserId를 가진 마일리지를 찾을 수 없음"));
    }
}