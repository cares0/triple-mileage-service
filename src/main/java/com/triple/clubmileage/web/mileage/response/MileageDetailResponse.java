package com.triple.clubmileage.web.mileage.response;

import com.triple.clubmileage.domain.mileage.Mileage;
import com.triple.clubmileage.web.user.response.UserDetailResponse;
import lombok.*;

import java.util.Objects;

import static lombok.AccessLevel.*;

@Getter @Setter
@Builder
@AllArgsConstructor(access = PRIVATE)
public class MileageDetailResponse {

    private String id;
    private Integer point;
    private UserDetailResponse user;

    public static MileageDetailResponse toResponse(Mileage mileage) {
        UserDetailResponse userDetailResponse = Objects.isNull(mileage.getUser()) ?
                null : UserDetailResponse.toResponse(mileage.getUser());

        return MileageDetailResponse.builder()
                .id(mileage.getId())
                .point(mileage.getPoint())
                .user(userDetailResponse)
                .build();
    }
}
