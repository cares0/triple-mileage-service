package com.triple.event.web.mileage;

import com.triple.event.domain.mileagehistory.MileageHistory;
import com.triple.event.domain.mileagehistory.MileageHistoryQueryService;
import com.triple.event.web.common.PageResponse;
import com.triple.event.web.mileagehistory.response.MileageHistoryDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mileages")
public class MileageController {

    private final MileageHistoryQueryService mileageHistoryQueryService;

    @GetMapping("/{mileageId}/mileage-histories")
    public PageResponse<MileageHistoryDetailResponse> mileageDetailWithHistory(
            @PathVariable String mileageId,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {

        Page<MileageHistory> page = mileageHistoryQueryService.getPageByMileageIdWithEvent(mileageId, pageable);
        List<MileageHistoryDetailResponse> contents = convertContentsToResponseList(page);

        return PageResponse.toPageResponse(page, contents);
    }

    private List<MileageHistoryDetailResponse> convertContentsToResponseList(Page<MileageHistory> page) {
        return page.getContent().stream()
                .map(MileageHistoryDetailResponse::toResponse)
                .collect(Collectors.toList());
    }
}

