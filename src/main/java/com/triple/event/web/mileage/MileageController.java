package com.triple.event.web.mileage;

import com.triple.event.domain.mileage.Mileage;
import com.triple.event.domain.mileage.MileageQueryService;
import com.triple.event.web.mileage.response.MileageDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mileages")
public class MileageController {

    private final MileageQueryService mileageQueryService;

    @GetMapping("/{mileageId}/users")
    public MileageDetailResponse mileageDetailWithUser(@PathVariable String mileageId) {
        Mileage mileage = mileageQueryService.getOneByIdWithUser(mileageId);
        return MileageDetailResponse.toResponse(mileage);
    }
}
