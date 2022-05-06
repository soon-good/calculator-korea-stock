package io.inspect.koreanstockinspector.tdd.common;

import io.inspect.koreanstockinspector.tdd.finance.crawling.gainloss.GainLossPeriods;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum PeriodType {
    FIRST_PREV("firstPrev", 3){
        @Override
        public String getPeriodValue(GainLossPeriods gainLossPeriods) {
            return gainLossPeriods.getFirstPrev();
        }
    },
    SECOND_PREV("secondPrev", 2){
        @Override
        public String getPeriodValue(GainLossPeriods gainLossPeriods) {
            return gainLossPeriods.getSecondPrev();
        }
    },
    THIRD_PREV("thirdPrev", 1){
        @Override
        public String getPeriodValue(GainLossPeriods gainLossPeriods) {
            return gainLossPeriods.getThirdPrev();
        }
    },
    FOURTH_PREV("fourthPrev", 0){
        @Override
        public String getPeriodValue(GainLossPeriods gainLossPeriods) {
            return gainLossPeriods.getFourthPrev();
        }
    };

    @Getter private final String periodLabel;
    @Getter private final int indexAs;

    static Map<Integer, PeriodType> periodMap = new HashMap<>();

    PeriodType(String periodLabel, int indexAs){
        this.periodLabel = periodLabel;
        this.indexAs = indexAs;
    }

    public abstract String getPeriodValue(GainLossPeriods gainLossPeriods);
}
