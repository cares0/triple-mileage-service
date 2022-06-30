package com.triple.clubmileage.web.user;

import com.triple.clubmileage.domain.mileage.Mileage;
import com.triple.clubmileage.domain.mileage.service.MileageQueryService;
import com.triple.clubmileage.web.mileage.response.MileageDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final MileageQueryService mileageQueryService;

    @GetMapping("/{userId}/mileages")
    public MileageDetailResponse mileageDetailWithUser(@PathVariable String userId) {
        Mileage mileage = mileageQueryService.getOneByUserIdWithUser(userId);
        return MileageDetailResponse.toResponse(mileage);
    }

}
