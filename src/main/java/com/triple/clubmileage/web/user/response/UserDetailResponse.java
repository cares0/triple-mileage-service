package com.triple.clubmileage.web.user.response;

import com.triple.clubmileage.domain.user.User;
import lombok.*;

import static lombok.AccessLevel.*;

@Getter @Setter
@Builder
@AllArgsConstructor(access = PRIVATE)
public class UserDetailResponse {

    private String id;
    private String name;

    public static UserDetailResponse toResponse(User user) {
        return UserDetailResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
