package com.triple.clubmileage.domain.event.util;

import com.triple.clubmileage.domain.event.exception.InvalidModifyingFactorException;
import com.triple.clubmileage.domain.mileagehistory.ModifyingFactor;
import org.springframework.stereotype.Component;

@Component
public class PointCalculatorReviewImpl implements PointCalculator{

    @Override
    public Integer getContentPoint(ModifyingFactor modifyingFactor) {
        switch (modifyingFactor) {
            case DELETE_REVIEW:
                return 0;
            case REVIEW_TEXT:
            case REVIEW_PHOTO:
                return 1;
            case REVIEW_TEXT_AND_PHOTO:
                return 2;
        }
        throw new InvalidModifyingFactorException();
    }
}
