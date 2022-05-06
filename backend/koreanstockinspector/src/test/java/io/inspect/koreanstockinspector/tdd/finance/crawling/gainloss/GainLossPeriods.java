package io.inspect.koreanstockinspector.tdd.finance.crawling.gainloss;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
public class GainLossPeriods {
    @Getter private final String firstPrev;
    @Getter private final String secondPrev;
    @Getter private final String thirdPrev;
    @Getter private final String fourthPrev;

    @Builder
    public GainLossPeriods(String firstPrev, String secondPrev, String thirdPrev, String fourthPrev){
        this.firstPrev = firstPrev;
        this.secondPrev = secondPrev;
        this.thirdPrev = thirdPrev;
        this.fourthPrev = fourthPrev;
    }
}
