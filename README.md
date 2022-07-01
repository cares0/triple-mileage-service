# Triple Club Mileage Service

## **목차**
### 1. 애플리케이션 실행 방법
&nbsp;&nbsp; 1) 직접 애플리케이션을 테스트하는 경우  
&nbsp;&nbsp; 2) 배포된 애플리케이션을 테스트하는 경우
### 2. 프로젝트 설계 설명
&nbsp;&nbsp; 1) E-R 다이어그램   
&nbsp;&nbsp; 2) 테이블 모델   
&nbsp;&nbsp; 3) DDL   
&nbsp;&nbsp; 4) REST API 스펙   
&nbsp;&nbsp; 5) 프로젝트 구조

<br/>

## **들어가기 전에**
1. 다른 서버에서 리뷰가 등록된 다음에 이 애플리케이션으로 이벤트 등록 정보가 넘어온다고 가정합니다.
   * 리뷰 등록은 다른 서버에서 처리한다고 가정했기에 구현하지 않았습니다.
   * 리뷰가 등록되어 있어야 이벤트도 등록이 가능합니다.

   <br/>

2. 리뷰는 아래의 경우로 등록될 수 있다고 가정했습니다.
    * 글만 작성하는 경우
    * 사진만 등록하는 경우
    * 글과 사진 모두 등록하는 경우

   <br/>

3. 이벤트의 타입은 리뷰 뿐만 아니라 다른 타입도 가능할 것을 고려했습니다.
   * 이벤트 타입의 확장성을 고려하여 구현했습니다.
   * 각 타입별로 처리해야 할 로직이 다르다고 가정합니다.
   * 예시를 위해 이 애플리케이션에서는 항공 예약시 발생하는 이벤트도 있다고 가정했습니다.
   * 항공 예약 시 발생하는 이벤트 등록과 적립 로직은 구현하지 않고, 프로젝트 구조만 구현했습니다.


<br>

___

<br/>

## **1. 애플리케이션 실행 방법**

> ### 1) 직접 애플리케이션을 테스트하는 경우

> ### 2) 배포된 애플리케이션을 테스트하는 경우

<br/>

___

<br/>

## **2. 프로젝트 설계**

> ### **1) E-R 다이어그램**
![E-R Diagram](https://user-images.githubusercontent.com/97069541/176802230-9d749926-460b-4ba9-8f8e-2f3e0a66021a.jpg)

<br/>

> ### **2) 테이블 모델**
![table-structure](https://user-images.githubusercontent.com/97069541/176828773-d787f10e-9129-418d-9f9e-f3b2272277c4.jpg)

<br/>

> ### **3) DDL**
* 물리적으로 FK는 지정하지 않았습니다.
```sql
create table event
(
    event_id           char (36)    not null,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6)  not null,
    event_action       varchar(100) not null,
    event_type         varchar(100) not null,
    type_id            char (36)    not null,
    constraint event_id_pk primary key (event_id)
);
create index event_type_id_idx on event (type_id);

create table place
(
    place_id           char (36)    not null,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6)  not null,
    name               varchar(255) null,
    constraint place_id_pk primary key (place_id)
);

create table users
(
    user_id            char (36)    not null,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6)  not null,
    name               varchar(100) null,
    constraint user_id_pk primary key (user_id)
);

create table mileage
(
    mileage_id         char (36)    not null,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6)  not null,
    point              int          not null,
    user_id            char (36)    not null,
    constraint mileage_id_pk primary key (mileage_id)
);
create index mileage_user_id_idx on mileage (user_id);

create table mileage_history
(
    mileage_history_id char (36)    not null,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6)  not null,
    bonus_point        int          not null,
    content            varchar(255) null,
    content_point      int          not null,
    modified_point     int          not null,
    modifying_factor   varchar(100) not null,
    event_id           char (36)    not null,
    mileage_id         char (36)    not null,
    constraint mileage_history_id_pk primary key (mileage_history_id)
);
create index mileage_history_event_id_idx on mileage_history (event_id);
create index mileage_history_mileage_id_idx on mileage_history (mileage_id);

create table review
(
    review_id          char (36)    not null,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6)  not null,
    content            varchar(255) null,
    place_id           char (36)    null,
    user_id            char (36)    null,
    constraint review_id_pk primary key (review_id)
);
create index review_place_id_idx on review (place_id);
create index review_user_id_idx on review (user_id);

create table review_photo
(
    review_photo_id    char (36)    not null,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6)  not null,
    review_id          char (36)    null,
    constraint review_photo_id_pk primary key (review_photo_id)
);
create index review_photo_review_id_idx on review_photo (review_id);
```

<br/>

> ### **4) REST API 스펙**
| Method | URI                                     | Description        | Query String                              |
|:-------|:----------------------------------------|:-------------------|:------------------------------------------|
| GET    | /users/{userId}/mileages                | 유저의 마일리지 조회        | X                                         |
| GET    | /mileages/{mileageId}/mileage-histories | 마일리지의 이력 리스트 조회    | size, page, startDate, endDate, eventType |
| GET    | /mileage-histories/{mileageHistoryId}   | 마일리지 이력 단건 조회      |                                           |
| POST   | /events                                 | 이벤트 등록, 마일리지 이력 등록 | X                                         |
| GET    | /events/{eventId}                       | 이벤트 단건 조회          | X                                         |

<br/>

### 1. `/users/{userId}/mileages`
* 응답 데이터 예시
```json
{
  "id": "2119445e-9b19-456c-ae46-90e1d1f19363",   /* 해당 유저의 마일리지 ID */
  "point": 3,                                     /* 해당 유저가 가진 총 포인트 */
  "user": {
    "id": "96927c62-6cdb-4918-91fe-8b01d348d33f", /* 유저 ID */
    "name": "유저1"                                /* 유저 이름 */
  }
}  
```
<br/>

### 2. `/mileages/{mileageId}/mileage-histories`
* 응답 데이터 예시
```json
{
    "currentPage": 0,        /* 현재 페이지 (0부터 시작) */
    "totalPage": 1,          /* 전체 페이지 수 */
    "perPage": 10,           /* 페이지 당 표시되는 데이터 수 */
    "totalElements": 3,      /* 전체 데이터 수 */
    "hasPrevious": false,    /* 이전 페이지 존재 여부 */
    "hasNext": false,        /* 다음 페이지 존재 여부 */
    "contents": [
        {
            "id": "24b8af59-b2bc-43e0-83e6-e91ed9033581",           /* 마일리지 이력 ID */
            "event": {
                "id": "2ef10514-eebd-447d-9c5a-61332df9917a",       /* 이벤트 ID */
                "typeId": "b8ef60da-cfe8-4c30-a044-ccb39e3431b6",   /* 이벤트가 발생한 요소의 ID (여기서는 Review의 ID가 됨) */
                "eventAction": "DELETE",                            /* 이벤트 유형 */
                "eventType": "REVIEW"                               /* 이벤트 타입 */
            },
            "modifiedPoint": -2,                              /* 변경된 점수 */
            "contentPoint": 0,                                /* 내용 점수 */
            "bonusPoint": 0,                                  /* 보너스 점수 */
            "modifyingFactor": "리뷰 삭제",                    /* 마일리지 이력 변경 요인 */
            "content": "장소1에 작성한 리뷰를 삭제했습니다."       /* 마일리지 이력 내용 */
        },
        {
            "id": "9f5192c9-6982-41bb-b7b6-01f03efdf7e2",
            "event": {
                "id": "495f3d3c-f842-4c9b-9ef0-cc834605b06f",
                "typeId": "b8ef60da-cfe8-4c30-a044-ccb39e3431b6",
                "eventAction": "MOD",
                "eventType": "REVIEW"
            },
            "modifiedPoint": -1,
            "contentPoint": 1,
            "bonusPoint": 1,
            "modifyingFactor": "텍스트 리뷰 작성",
            "content": "장소1에 작성한 리뷰를 수정했습니다."
        },
        {
            "id": "ec685e6a-869f-4003-9320-76b130b3eaf0",
            "event": {
                "id": "adb8cf9c-c693-46fd-a2cf-460c8d7b5778",
                "typeId": "b8ef60da-cfe8-4c30-a044-ccb39e3431b6",
                "eventAction": "ADD",
                "eventType": "REVIEW"
            },
            "modifiedPoint": 3,
            "contentPoint": 2,
            "bonusPoint": 1,
            "modifyingFactor": "텍스트와 사진 리뷰 작성",
            "content": "장소1에 리뷰를 작성했습니다."
        }
    ],
    "first": true,           // 첫 페이지 여부
    "last": true             // 마지막 페이지 여부
}
```
<br/>

### 3. `/mileage-histories/{mileageHistoryId}`
* 응답 데이터 예시
```json
{
    "id": "ec685e6a-869f-4003-9320-76b130b3eaf0",
    "event": {
        "id": "adb8cf9c-c693-46fd-a2cf-460c8d7b5778",
        "typeId": "b8ef60da-cfe8-4c30-a044-ccb39e3431b6",
        "eventAction": "ADD",
        "eventType": "REVIEW"
    },
    "modifiedPoint": 3,
    "contentPoint": 2,
    "bonusPoint": 1,
    "modifyingFactor": "텍스트와 사진 리뷰 작성",
    "content": "장소1에 리뷰를 작성했습니다."
}
```
<br/>

### 4. `/events`
* 요청 데이터 예시
```json
{
  "type": "REVIEW",
  "action": "ADD",
  "reviewId": "b8ef60da-cfe8-4c30-a044-ccb39e3431b6",
  "content": "리뷰 내용",   /* ADD, MOD 시에는 content, attachedPhotoIds 중 하나의 필드는 무조건 있어야 함 */
  "attachedPhotoIds": ["3ba2fc13-1bb7-4d5d-8deb-b72402f9b5a7", "200ab535-d711-41cc-a0df-86eea85c9891"],
  "userId": "96927c62-6cdb-4918-91fe-8b01d348d33f",
  "placeId": "83a0d51c-a508-4330-8dd0-fb1576bffbcb"
}
```
* 응답 데이터 예시 (저장된 Event, MileageHistory ID 반환)
```json
{
    "mileageHistoryId": "ec685e6a-869f-4003-9320-76b130b3eaf0",
    "eventId": "adb8cf9c-c693-46fd-a2cf-460c8d7b5778"
}
```
<br/>

### 5. `/events/{eventId}`
* 응답 데이터 예시
```json
{
    "id": "adb8cf9c-c693-46fd-a2cf-460c8d7b5778",
    "typeId": "b8ef60da-cfe8-4c30-a044-ccb39e3431b6",
    "eventAction": "ADD",
    "eventType": "REVIEW"
}
```
<br/>

> ### **5) 프로젝트 구조**
![project-structure](https://user-images.githubusercontent.com/97069541/176664348-7e2dcf80-99b2-40db-9623-a158064cd59a.jpg)

**웹 계층과 도메인 계층을 분리**
* 도메인 계층은 웹 계층을 의존하지 않습니다.
* 웹 계층의 변경이 도메인 계층에 영향을 미치지 않습니다.

<br/>

**패키지 구조**
* com.triple.clubmileage
    * domain
        * 비즈니스 로직 패키지(도메인별로 분리)
            * 서비스 패키지
            * 리포지토리 패키지
            * 엔티티
            * 기타 등등
        * 공통 패키지
        * 예외 패키지
    * web
        * 웹 로직 패키지(도메인별로 분리)
            * Response 패키지
            * Request 패키지
            * 컨트롤러
            * 기타 등등
        * 공통 패키지
        * 예외 패키지

<br/>

___

<br/>

## 프로젝트 구현










