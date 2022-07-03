package com.triple.clubmileage.web.mileagehistory.response;

import com.triple.clubmileage.domain.mileagehistory.MileageHistory;
import com.triple.clubmileage.web.event.response.EventDetailResponse;
import lombok.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.*;

@Getter @Setter
@Builder
@AllArgsConstructor(access = PRIVATE)
public class MileageHistoryDetailResponse {

    private String id;
    private EventDetailResponse event;
    private LocalDateTime createdDate;
    private Integer modifiedPoint;
    private Integer contentPoint;
    private Integer bonusPoint;
    private String modifyingFactor;
    private String content;

    public static MileageHistoryDetailResponse toResponse(MileageHistory mileageHistory) {
        return MileageHistoryDetailResponse.builder()
                .id(mileageHistory.getId())
                .event(EventDetailResponse.toResponse(mileageHistory.getEvent()))
                .createdDate(mileageHistory.getCreatedDate())
                .modifiedPoint(mileageHistory.getModifiedPoint())
                .contentPoint(mileageHistory.getContentPoint())
                .bonusPoint(mileageHistory.getBonusPoint())
                .modifyingFactor(mileageHistory.getModifyingFactor().getKorName())
                .content(mileageHistory.getContent())
                .build();
    }

}
