package com.triple.event.domain.mileage;

import com.triple.event.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class MileageQueryServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    MileageQueryService mileageQueryService;

    @Test
    @DisplayName("마일리지 유저아이디로 단건 조회 - 연관 유저 포함")
    public void 마일리지단건조회_아이디로_유저포함() throws Exception {
        // given
        User user = createUser();
        Mileage mileage = createMileage(user);

        // when
        Mileage findMileage = mileageQueryService.getOneByUserIdWithUser(user.getId());

        // then
        Assertions.assertThat(findMileage).isEqualTo(mileage);
        Assertions.assertThat(findMileage.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("마일리지 유저 아이디로 단건 조회")
    public void 마일리지단건조회_유저아이디로() throws Exception {
        // given
        User user = createUser();
        Mileage mileage = createMileage(user);

        // when
        Mileage findMileage = mileageQueryService.getOneByUserId(user.getId());

        // then
        Assertions.assertThat(findMileage).isEqualTo(mileage);
    }

    private Mileage createMileage(User user) {
        Mileage mileage = Mileage.builder().point(0).user(user).build();
        em.persist(mileage);
        return mileage;
    }

    private User createUser() {
        User user = User.builder().name("유저1").build();
        em.persist(user);
        return user;
    }
}