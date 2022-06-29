package com.triple.event.web.mileagehistory.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.triple.event.domain.event.Event;
import com.triple.event.domain.mileagehistory.MileageHistory;
import com.triple.event.web.event.response.EventDetailResponse;
import lombok.*;

import static lombok.AccessLevel.*;

@Getter @Setter
@Builder
@AllArgsConstructor(access = PRIVATE)
public class MileageHistoryDetailResponse {

    private String id;
    private EventDetailResponse event;
    private Integer modifiedPoint;
    private Integer contentPoint;
    private Integer bonusPoint;
    private String modifyingFactor;
    private String content;

    public static MileageHistoryDetailResponse toResponse(MileageHistory mileageHistory) {
        return MileageHistoryDetailResponse.builder()
                .id(mileageHistory.getId())
                .event(EventDetailResponse.toResponse(mileageHistory.getEvent()))
                .modifiedPoint(mileageHistory.getModifiedPoint())
                .contentPoint(mileageHistory.getContentPoint())
                .bonusPoint(mileageHistory.getBonusPoint())
                .modifyingFactor(mileageHistory.getModifyingFactor().getKorName())
                .content(mileageHistory.getContent())
                .build();
    }
}
