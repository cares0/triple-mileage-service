package com.triple.clubmileage.domain.user;

import com.triple.clubmileage.domain.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.UUID;

import static lombok.AccessLevel.*;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id")
    private String id;

    private String name;

    @Builder
    private User(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }
}
