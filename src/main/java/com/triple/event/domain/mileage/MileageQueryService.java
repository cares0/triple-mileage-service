package com.triple.event.domain.mileage;

import com.triple.event.domain.exception.EntityNotFoundException;
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

    public Mileage getOneByIdWithUser(String mileageId) {
        return mileageRepository.findByIdWithUser(mileageId).orElseThrow(() ->
                new EntityNotFoundException("해당 MileageId를 가진 마일리지를 찾을 수 없음"));
    }
}