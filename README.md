# Triple Club Mileage Service

<br/><br/>

>## **목차**

<br/>

### 1. 애플리케이션 실행 방법
&nbsp;&nbsp; 1) 직접 애플리케이션을 테스트하는 경우  
&nbsp;&nbsp; 2) 배포된 애플리케이션을 테스트하는 경우

<br/>

### 2. 프로젝트 설계
&nbsp;&nbsp; 1) E-R 다이어그램   
&nbsp;&nbsp; 2) 테이블 모델   
&nbsp;&nbsp; 3) DDL   
&nbsp;&nbsp; 4) REST API 스펙   
&nbsp;&nbsp; 5) 프로젝트 구조

<br/><br/>

>## **들어가기 전에**

<br/>

### 1. **다른 서버에서 리뷰가 등록된 다음에 이 애플리케이션으로 이벤트 등록 정보가 넘어온다고 가정합니다.**
* 리뷰 등록은 다른 서버에서 처리한다고 가정했기에 구현하지 않았습니다.
* DB에 리뷰가 저장되어 있어야 이벤트 등록에 문제가 발생하지 않습니다.
  
<br/>

### 2. **리뷰는 아래의 경우로 등록될 수 있다고 가정했습니다.**
* 글만 작성하는 경우
* 사진만 등록하는 경우
* 글과 사진 모두 등록하는 경우

<br/>

### 3. **이벤트의 타입은 리뷰뿐만 아니라 다른 타입도 가능할 것을 고려했습니다.**
* 이벤트 타입의 확장성을 고려하여 구현했습니다.
* 각 타입별로 처리해야 할 로직이 다르다고 가정합니다.
* 예시를 위해 이 애플리케이션에서는 항공 예약시 발생하는 이벤트도 있다고 가정했습니다.
* 항공 예약 시 발생하는 이벤트 등록과 적립 로직은 구현하지 않고, 프로젝트 구조만 구현했습니다.

<br/>

### 4. **발생하기 힘든 버그는 어느 정도 배제했습니다.**
* 예를 들어, 리뷰가 등록될 경우 무조건 단 한번의 ADD 이벤트 등록 요청이 발생한다고 가정했습니다.
* 따라서 동일 리뷰에 대해 ADD 이벤트 등록 요청이 2번 이상 발생할 경우는 예외를 발생시키지 않습니다.
* 이는 예외를 배제했을 때 쿼리 최적화가 가능해지는 경우에만 적용했습니다.

<br/>

### 5. 테스트 중에 문제가 발생하거나 프로젝트 테스트 방법, 구현 과정에 대해 더 자세한 설명이 필요한 경우 메일을 보내주세요.
* cares00000@gmail.com

<br/>

___

# **1. 애플리케이션 실행 방법**

<br/><br/>

> ## **1) 직접 애플리케이션을 테스트하는 경우**

* **Intellij 와 MySQL 서버가 필요합니다.**

<br/>

### 1. 소스 코드를 다운로드해서 압축을 풀어주세요.
* [소스코드 다운로드](https://drive.google.com/file/d/16N6tJ1Sm4Cu_z3CIJMDJGh5sQjsNWJxw/view?usp=sharing)
<br/>

### 2. Intellij 에서 File - Open 으로 들어가서 다운받은 프로젝트의 build.gradle 을 선택해주세요.
<br/>

### 3. Open as a project 를 선택해서 프로젝트를 빌드해주세요.
<br/>

### 4. src/main/resources/application.yml 파일을 열어 다음 부분을 수정해주세요.
```yaml
spring:
  profiles:
    default: dev   /* prod -> dev */ 
```
<br/>

### 5. src/main/resources/ 경로에 application-dev.yml 파일을 만들고 작성해주세요.
* 아래를 참고하여 DB 설정 정보를 넣어주세요. (MySQL 기준입니다.)
```yaml
spring:
  config:
    activate:
      on-profile: dev
  datasource:
    url: jdbc:mysql://{DB 호스트 주소}:{포트번호}/{스키마명}
    username: DB 호스트 이름
    password: DB 비밀변호
    driver-class-name: com.mysql.cj.jdbc.Driver /* 다른 DB 사용 시 맞는 드라이버를 설정해주세요 */  

  jpa:
    hibernate:
      ddl-auto: none
```
<br/>

### 6. 연결한 DB에 아래에 기술한 [DDL](https://github.com/cares0/triple-mileage-service#3-ddl) 을 실행시켜주세요. 
<br/>

### 7. 프로젝트를 실행하고 문제가 없다면 정상적으로 테스트가 가능합니다.

<br/><br/>

### **직접 테스트 할 경우 주의점**
1. User, Mileage, Place, Review 는 직접 DML로 등록해주어야 합니다.
```sql
-- user
insert into users (created_date, last_modified_date, name, user_id) 
values (now(), now(), '유저1', '96927c62-6cdb-4918-91fe-8b01d348d33f');

-- mileage
insert into mileage (created_date, last_modified_date, point, user_id, mileage_id) 
values (now(), now(), 0, '96927c62-6cdb-4918-91fe-8b01d348d33f', '2119445e-9b19-456c-ae46-90e1d1f19363');

-- place
insert into place (created_date, last_modified_date, name, place_id) 
values (now(), now(), '장소1', '83a0d51c-a508-4330-8dd0-fb1576bffbcb');

-- user1 review
insert into review (created_date, last_modified_date, content, place_id, user_id, review_id) 
values (now(), now(), '리뷰1 - 장소1', '83a0d51c-a508-4330-8dd0-fb1576bffbcb', '96927c62-6cdb-4918-91fe-8b01d348d33f', 'b8ef60da-cfe8-4c30-a044-ccb39e3431b6');
```
<br/>

2. DML로 등록한 User, Place, Review를 통해 이벤트 등록 API를 적절히 호출해주세요. (아래는 예시입니다.)
```json
{
    "type": "REVIEW",
    "action": "ADD",
    "reviewId": "b8ef60da-cfe8-4c30-a044-ccb39e3431b6",
    "content": "",           /* content, attachedPhotoIds는 테스트 하고 싶은 경우에 따라 자율적으로 넣어주세요. */
    "attachedPhotoIds": [],  /* 예를 들어, 글만 등록 한 경우의 점수를 알고 싶다면 content만 작성하시면 됩니다 */ 
    "userId": "96927c62-6cdb-4918-91fe-8b01d348d33f",
    "placeId": "83a0d51c-a508-4330-8dd0-fb1576bffbcb"
}
```
<br/>

3. Review 테이블에 존재하는 Review 데이터만 테스트하는 것을 권장합니다.
* 예를 들어, Review 테이블에 데이터를 저장하지 않고 ADD 이벤트를 호출했을 경우, 보너스 점수에서 문제가 발생할 수 있습니다.
* Review 테이블에 Place의 ID를 가지고 데이터를 찾아 조회된 리뷰가 1개라면 최초의 리뷰로 인지하고 보너스 점수를 부여하기 때문입니다.
* 첫 리뷰는 등록했지만, 두 번째 리뷰는 Review 테이블에 등록하지 않고 이벤트를 호출한다면 최초의 리뷰로 판단되어 보너스 점수가 부여되어 버립니다.

<br/><br/>

> ## **2) 배포된 애플리케이션을 테스트하는 경우**

<br/> 

### **서버 정보**  
* **서버 IP: 3.39.227.64**
* **포트: 80**
* Postman을 통해서 테스트 하는 것을 권장드립니다.

<br/>

### 테스트 데이터 설명
테스트를 위한 데이터를 넣어놨습니다. 식별자는 아래와 같습니다.

| 회원  | 회원 ID                                | 마일리지 ID                              |
|:----|:-------------------------------------|:-------------------------------------|
| 회원1 | 96927c62-6cdb-4918-91fe-8b01d348d33f | 2119445e-9b19-456c-ae46-90e1d1f19363 | 
| 회원2 | 3c4dd476-39c2-4cf7-ad91-4872c7629b84 | 3ed33b34-76e9-4557-b8d0-5e59eeeede8c |

| 장소 이름 | 장소 ID                                |
|:------|:-------------------------------------|
| 장소1   | 83a0d51c-a508-4330-8dd0-fb1576bffbcb |
| 장소2   | cdc09e7e-7467-4226-99bf-36269b8c54c7 |
| 장소3   | 2472f4f4-41a5-4c10-bf85-fcebf41fc4e8 |

| 유저가 작성한 리뷰     | 리뷰 ID                                | 이벤트 등록 여부 |
|:---------------|:-------------------------------------|:----------|
| 회원1의 리뷰1 - 장소1 | b8ef60da-cfe8-4c30-a044-ccb39e3431b6 | 등록 완료     |
| 회원1의 리뷰2 - 장소2 | 798a6c0d-5b2b-46e7-8ea8-05d6240008a2 | 등록 완료     |
| 회원1의 리뷰3 - 장소3 | 676c4648-498e-426f-a88d-5bbc648d074c | 등록 X      |
| 회원2의 리뷰1 - 장소1 | cc65772f-f05b-4c07-9e27-c03d42035860 | 등록 X      |
| 회원2의 리뷰2 - 장소2 | 3aeec395-ee1a-4dbe-9ab5-19e08ed7b395 | 등록 X      |

리뷰 등록, 수정, 삭제는 다른 서버에서 진행되고, 발생한 이벤트에 대해서만 이 서버로 들어온다고 설정했습니다.   
따라서 테스트를 위해 리뷰 데이터를 5개 미리 DB에 등록해놨습니다.

* 회원이 2명 존재하고, 장소1, 장소2에 대해 각 회원마다 리뷰를 한 개씩 작성해 놓은 상황입니다.
* 회원1의 경우에는 리뷰1과, 리뷰2에 대한 이벤트를 발생시켜 마일리지 이력을 10개 만들어 놓았습니다.
* 회원2의 경우에는 리뷰는 등록되어 있지만, 이벤트를 발생시키지 않아 마일리지 이력은 없습니다.
* 회원1의 리뷰3은 장소3에 대한 리뷰이며, 이벤트를 발생시키지 않아 마일리지 이력은 없습니다.  
  (첫 리뷰 작성 시 보너스 포인트가 적용되는지 테스트 하기 위한 데이터입니다.)

<br/>

### 1. 조회 API 테스트 (자세한 사항은 [2-4. REST API 스펙](https://github.com/cares0/triple-mileage-service#4-rest-api-%EC%8A%A4%ED%8E%99) 을 참고해주세요.)
* 조회는 회원 1의 데이터를 가지고 테스트합니다.

<br/>

&nbsp;&nbsp; **1) 회원 1의 마일리지 조회**
* `/users/96927c62-6cdb-4918-91fe-8b01d348d33f/mileages` 로 요청을 보냅니다.

<br/>

&nbsp;&nbsp; **2) 회원1의 마일리지의 이력 리스트 조회**
* `/mileages/2119445e-9b19-456c-ae46-90e1d1f19363/mileage-histories` 요청을 보냅니다.
* page를 통해 페이지를 지정할 수 있습니다. (0부터 시작, 기본 값 0)
* size를 통해 페이지 당 보여줄 데이터 수를 지정할 수 있습니다. (기본 값 10)
* eventType을 통해 조회할 이벤트 유형을 지정할 수 있습니다.
* startDate, endDate를 통해 조회할 이력의 날짜 범위를 지정할 수 있습니다. (둘 다 지정해야 합니다)
* 참고로 회원 1의 이력은 2022-06-29 ~ 2022-07-02 까지 등록일을 배분시켜 놓았습니다.

<br/>

&nbsp;&nbsp; **3) 마일리지 이력 정보 단건 조회** 
* 2번에서 조회한 마일리지 이력 리스트에는 ID 값이 응답되어 있습니다.
* 조회할 마일리지 이력 ID를 `/mileage-histories/{mileageHistoryId}` 의 경로변수에 넣어 요청을 보냅니다.

<br/>

&nbsp;&nbsp; **4) 이벤트의 정보 단건 조회**
* 2번 혹은 3번에서 조회한 마일리지 이력에는 이벤트 ID 값이 응답되어 있습니다.
* 조회할 이벤트 ID를 `/events/{eventId}` 의 경로변수에 넣어 요청을 보냅니다.

<br/>

### 2. 이벤트 등록 API 테스트
* 등록은 회원2의 데이터를 가지고 테스트합니다.
* 첫 리뷰 보너스의 경우 회원1 - 리뷰3의 데이터를 가지고 테스트합니다.

<br>

&nbsp;&nbsp; **1) 리뷰 작성 이벤트를 등록하는 경우**
* 회원2의 ID, 장소1 ID, 리뷰1 ID를 가지고 `/events` 로 요청을 보냅니다.
* contents, attachedPhotoIds 의 유무에 따라 부여되는 점수가 달라집니다. (null 값은 허용되지 않습니다.)
```json
{
    "type": "REVIEW",
    "action": "ADD",
    "reviewId": "cc65772f-f05b-4c07-9e27-c03d42035860",
    "content": "",           /* content, attachedPhotoIds는 테스트 하고 싶은 경우에 따라 자율적으로 넣어주세요. */
    "attachedPhotoIds": [],  /* 글만 등록 한 경우의 점수를 알고 싶다면 content만 작성하시면 됩니다 */ 
    "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
    "placeId": "83a0d51c-a508-4330-8dd0-fb1576bffbcb"
}
```
<br/>

&nbsp;&nbsp; **2) 리뷰 수정 이벤트를 등록하는 경우**
* 수정되었다고 가정할 content, attachedPhotoIds 를 자율적으로 넣어서 `events` 로 요청을 보냅니다.
```json
{
    "type": "REVIEW",
    "action": "MOD",
    "reviewId": "cc65772f-f05b-4c07-9e27-c03d42035860",
    "content": "",           /* content, attachedPhotoIds는 테스트 하고 싶은 경우에 따라 자율적으로 수정해주세요. */
    "attachedPhotoIds": [],  
    "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
    "placeId": "83a0d51c-a508-4330-8dd0-fb1576bffbcb"
}
```
<br/>

&nbsp;&nbsp; **3) 리뷰 삭제 이벤트를 등록하는 경우**
* 따로 content, attachedPhotoIds 필드에 값을 넣어주지 않아도 됩니다.
```json
{
    "type": "REVIEW",
    "action": "DELETE",
    "reviewId": "cc65772f-f05b-4c07-9e27-c03d42035860",
    "content": "",         
    "attachedPhotoIds": [],
    "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
    "placeId": "83a0d51c-a508-4330-8dd0-fb1576bffbcb"
}
```
<br/>

&nbsp;&nbsp; **4) 첫 리뷰 보너스 점수를 테스트하는 경우**
* 회원1의 ID, 장소3 ID, 리뷰3 ID를 가지고 `/events` 로 요청을 보냅니다.
* 보너스 점수 외에 내용점수는 content, attachedPhotoIds 에 의존하는 것은 동일합니다.
```json
{
    "type": "REVIEW",
    "action": "ADD",
    "reviewId": "cc65772f-f05b-4c07-9e27-c03d42035860",
    "content": "",
    "attachedPhotoIds": [],
    "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
    "placeId": "83a0d51c-a508-4330-8dd0-fb1576bffbcb"
}
```
<br/>

&nbsp;&nbsp; **5) 이벤트에 따라 회원의 총 마일리지가 변경되는 것을 테스트하는 경우**
* 확인할 회원의 ID를 가지고 `/users/{userId}/mileages` 로 요청을 보내면 확인 가능합니다.

<br/>

___

<br/>

# **2. 프로젝트 설계**

<br/><br/>

> ## **1) E-R 다이어그램**
![E-R Diagram](https://user-images.githubusercontent.com/97069541/176802230-9d749926-460b-4ba9-8f8e-2f3e0a66021a.jpg)

<br/><br/>

> ## **2) 테이블 모델**
![table-structure](https://user-images.githubusercontent.com/97069541/176828773-d787f10e-9129-418d-9f9e-f3b2272277c4.jpg)

<br/><br/>

> ## **3) DDL**
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
create unique index mileage_user_id_idx on mileage (user_id);

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
create unique index mileage_history_event_id_idx on mileage_history (event_id);
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

<br/><br/>

> ## **4) REST API 스펙**
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
    "id": "96927c62-6cdb-4918-91fe-8b01d348d33f",   /* 유저 ID */
    "name": "유저1"                                  /* 유저 이름 */
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
            "createdDate": "2022-07-01T16:05:22.371",
            "modifiedPoint": -2,                              /* 변경된 점수 */
            "contentPoint": 0,                                /* 내용 점수 */
            "bonusPoint": 0,                                  /* 보너스 점수 */
            "modifyingFactor": "리뷰 삭제",                    /* 마일리지 이력 변경 요인 */
            "content": "장소1에 작성한 리뷰를 삭제했습니다."     /* 마일리지 이력 내용 */
        },
        {
            "id": "9f5192c9-6982-41bb-b7b6-01f03efdf7e2",
            "event": {
                "id": "495f3d3c-f842-4c9b-9ef0-cc834605b06f",
                "typeId": "b8ef60da-cfe8-4c30-a044-ccb39e3431b6",
                "eventAction": "MOD",
                "eventType": "REVIEW"
            },
            "createdDate": "2022-06-30T12:02:13.411",
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
            "createdDate": "2022-06-29T19:05:31.125",
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
    "id": "ec685e6a-869f-4003-9320-76b130b3eaf0",          /* 마일리지 이력 ID */
    "event": {
        "id": "adb8cf9c-c693-46fd-a2cf-460c8d7b5778",      /* 이벤트 ID */
        "typeId": "b8ef60da-cfe8-4c30-a044-ccb39e3431b6",  /* 이벤트가 발생한 요소의 ID (여기서는 Review의 ID가 됨) */
        "eventAction": "ADD",                              /* 이벤트 유형 */
        "eventType": "REVIEW"                              /* 이벤트 타입 */
    },
    "createdDate": "2022-07-01T16:05:22.371",
    "modifiedPoint": 3,                                    /* 변경된 점수 */
    "contentPoint": 2,                                     /* 내용 점수 */
    "bonusPoint": 1,                                       /* 보너스 점수 */
    "modifyingFactor": "텍스트와 사진 리뷰 작성",            /* 마일리지 이력 변경 요인 */
    "content": "장소1에 리뷰를 작성했습니다."                /* 마일리지 이력 내용 */
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
* 응답 데이터 예시
```json
{
    "mileageHistoryId": "ec685e6a-869f-4003-9320-76b130b3eaf0",   /* 저장된 이벤트 ID */
    "eventId": "adb8cf9c-c693-46fd-a2cf-460c8d7b5778"             /* 저장된 마일리지 이력 ID */
}
```
<br/>

### 5. `/events/{eventId}`
* 응답 데이터 예시
```json
{
    "id": "adb8cf9c-c693-46fd-a2cf-460c8d7b5778",     /* 이벤트 ID */
    "typeId": "b8ef60da-cfe8-4c30-a044-ccb39e3431b6", /* 이벤트가 발생한 요소의 ID (여기서는 Review의 ID가 됨) */
    "eventAction": "ADD",                             /* 이벤트 유형 */
    "eventType": "REVIEW"                             /* 이벤트 타입 */
}
```

<br/><br/>

> ## **5) 프로젝트 구조**
![Untitled Diagram (3)](https://user-images.githubusercontent.com/97069541/177026771-a8de769b-71dc-4ed4-b424-541c385f0887.jpg)

<br/>

### **웹 계층과 도메인 계층을 분리**
* 도메인 계층은 웹 계층을 의존하지 않습니다.
* 웹 계층의 변경이 도메인 계층에 영향을 미치지 않습니다.

<br/>

### **패키지 구조**
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











