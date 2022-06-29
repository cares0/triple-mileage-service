package com.triple.event.domain.event;

import com.triple.event.domain.mileagehistory.ModifyingFactor;

public class PointCalculator {

    public static Integer getContentPoint(ModifyingFactor modifyingFactor) {
        Integer point = 0;
        switch (modifyingFactor) {
            case REVIEW_TEXT:
            case REVIEW_PHOTO:
                point = 1;
                break;
            case REVIEW_TEXT_AND_PHOTO:
                point = 2;
        }
        return point; // 삭제된 경우는 0
    }
}
