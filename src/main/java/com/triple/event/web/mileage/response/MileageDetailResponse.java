package com.triple.event.web.mileage.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.triple.event.domain.mileage.Mileage;
import com.triple.event.domain.mileagehistory.MileageHistory;
import com.triple.event.domain.user.User;
import com.triple.event.web.mileagehistory.response.MileageHistoryDetailResponse;
import com.triple.event.web.user.response.UserDetailResponse;
import lombok.*;
import org.hibernate.LazyInitializationException;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static lombok.AccessLevel.*;

@Getter @Setter
@Builder
@AllArgsConstructor(access = PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MileageDetailResponse {

    private String id;
    private Integer point;
    private UserDetailResponse user;
    private List<MileageHistoryDetailResponse> mileageHistories;

    public static MileageDetailResponse toResponse(Mileage mileage) {
        UserDetailResponse userDetailResponse = getUserDetailResponse(mileage);
        List<MileageHistoryDetailResponse> mileageHistoryDetailResponses = getMileageHistoryDetailResponses(mileage);

        return MileageDetailResponse.builder()
                .id(mileage.getId())
                .point(mileage.getPoint())
                .user(userDetailResponse)
                .mileageHistories(mileageHistoryDetailResponses)
                .build();
    }

    private static UserDetailResponse getUserDetailResponse(Mileage mileage) {
        return Objects.isNull(mileage.getUser()) ?
                null : UserDetailResponse.toResponse(mileage.getUser());
    }

    private static List<MileageHistoryDetailResponse> getMileageHistoryDetailResponses(Mileage mileage) {
        try {
            return CollectionUtils.isEmpty(mileage.getMileageHistories()) ?
                    null : mileage.getMileageHistories().stream().map((mileageHistory) ->
                    MileageHistoryDetailResponse.toResponse(mileageHistory)).collect(Collectors.toList());
        } catch (LazyInitializationException e) {
            return null;
        }
    }
}
