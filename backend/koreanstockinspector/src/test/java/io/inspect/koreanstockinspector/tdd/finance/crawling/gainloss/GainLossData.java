package io.inspect.koreanstockinspector.tdd.finance.crawling.gainloss;

import io.inspect.koreanstockinspector.tdd.common.PeriodType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
public class GainLossData {
    @Getter private String periodValue;
    @Getter private PeriodType periodType;
    @Getter private BigDecimal totalProfit;
    @Getter private BigDecimal operatingProfit;
    @Getter private BigDecimal netIncome;

    @Builder
    public GainLossData(
        String periodValue, PeriodType periodType, BigDecimal totalProfit, BigDecimal operatingProfit, BigDecimal netIncome
    ){
        this.periodValue = periodValue;
        this.periodType = periodType;
        this.totalProfit = totalProfit;
        this.operatingProfit = operatingProfit;
        this.netIncome = netIncome;
    }
}
