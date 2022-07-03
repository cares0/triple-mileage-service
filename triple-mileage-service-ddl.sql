create table event
(
    event_id           char (36) not null,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6)  not null,
    event_action       varchar(100) not null,
    event_type         varchar(100) not null,
    type_id            char (36) not null,
    constraint event_id_pk primary key (event_id)
);
create index event_type_id_idx on event (type_id);

create table place
(
    place_id           char (36) not null,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6)  not null,
    name               varchar(255) null,
    constraint place_id_pk primary key (place_id)
);

create table users
(
    user_id            char (36) not null,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6)  not null,
    name               varchar(100) null,
    constraint user_id_pk primary key (user_id)
);

create table mileage
(
    mileage_id         char (36) not null,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6)  not null,
    point              int          not null,
    user_id            char (36) not null,
    constraint mileage_id_pk primary key (mileage_id)
);
create unique index mileage_user_id_idx on mileage (user_id);

create table mileage_history
(
    mileage_history_id char (36) not null,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6)  not null,
    bonus_point        int          not null,
    content            varchar(255) null,
    content_point      int          not null,
    modified_point     int          not null,
    modifying_factor   varchar(100) not null,
    event_id           char (36) not null,
    mileage_id         char (36) not null,
    constraint mileage_history_id_pk primary key (mileage_history_id)
);
create unique index mileage_history_event_id_idx on mileage_history (event_id);
create index mileage_history_mileage_id_idx on mileage_history (mileage_id);

create table review
(
    review_id          char (36) not null,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6)  not null,
    content            varchar(255) null,
    place_id           char (36) null,
    user_id            char (36) null,
    constraint review_id_pk primary key (review_id)
);
create index review_place_id_idx on review (place_id);
create index review_user_id_idx on review (user_id);

create table review_photo
(
    review_photo_id    char (36) not null,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6)  not null,
    review_id          char (36) null,
    constraint review_photo_id_pk primary key (review_photo_id)
);
create index review_photo_review_id_idx on review_photo (review_id);