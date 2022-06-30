package com.triple.event.web.mileagehistory;

import com.triple.event.domain.mileagehistory.MileageHistory;
import com.triple.event.domain.mileagehistory.MileageHistoryQueryService;
import com.triple.event.web.mileagehistory.response.MileageHistoryDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mileage-histories")
public class MileageHistoryController {

    private final MileageHistoryQueryService mileageHistoryQueryService;

    @GetMapping("/{mileageHistoryId}")
    public MileageHistoryDetailResponse mileageHistoryDetailWithEvent(@PathVariable String mileageHistoryId) {
        MileageHistory mileageHistory = mileageHistoryQueryService.getOneByIdWithEvent(mileageHistoryId);
        return MileageHistoryDetailResponse.toResponse(mileageHistory);
    }
}
