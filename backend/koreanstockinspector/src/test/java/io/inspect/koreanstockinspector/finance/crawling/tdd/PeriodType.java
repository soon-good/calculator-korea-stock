package io.inspect.koreanstockinspector.finance.crawling.tdd;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum PeriodType {
    FIRST_PREV("firstPrev", 3),
    SECOND_PREV("secondPrev", 2),
    THIRD_PREV("thirdPrev", 1),
    FOURTH_PREV("fourthPrev", 0);

    @Getter private final String periodLabel;
    @Getter private final int indexAs;

    static Map<Integer, PeriodType> periodMap = new HashMap<>();

    PeriodType(String periodLabel, int indexAs){
        this.periodLabel = periodLabel;
        this.indexAs = indexAs;
    }
}
