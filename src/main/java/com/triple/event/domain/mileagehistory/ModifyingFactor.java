package com.triple.event.domain.mileagehistory;

public enum ModifyingFactor {

    REVIEW_TEXT("텍스트 리뷰 작성"),
    REVIEW_PHOTO("사진 리뷰 작성"),
    REVIEW_TEXT_AND_PHOTO("텍스트와 사진 리뷰 작성"),
    FIRST_REVIEW("첫 번째 리뷰");

    private final String korName;

    ModifyingFactor(String korName) {
        this.korName = korName;
    }

    public String getKorName() {
        return korName;
    }
}
