package com.triple.clubmileage.domain.event.util;

import com.triple.clubmileage.domain.mileagehistory.ModifyingFactor;

public interface PointCalculator {

    Integer getContentPoint(ModifyingFactor modifyingFactor);
}
