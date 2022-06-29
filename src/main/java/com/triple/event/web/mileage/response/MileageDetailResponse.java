package com.triple.event.web.mileage.response;

import com.triple.event.domain.mileage.Mileage;
import com.triple.event.web.user.response.UserDetailResponse;
import lombok.*;

import static lombok.AccessLevel.*;

@Getter @Setter
@Builder
@AllArgsConstructor(access = PRIVATE)
public class MileageDetailResponse {

    private String id;
    private Integer point;
    private UserDetailResponse user;

    public static MileageDetailResponse toResponse(Mileage mileage) {
        return MileageDetailResponse.builder()
                .id(mileage.getId())
                .point(mileage.getPoint())
                .user(UserDetailResponse.toResponse(mileage.getUser()))
                .build();
    }
}
