package io.inspect.koreanstockinspector.finance.crawling.tdd.gainloss;

import java.math.BigDecimal;

import io.inspect.koreanstockinspector.finance.crawling.tdd.common.PeriodType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
public class EachGainLossDto {
	@Getter private String periodValue;
	@Getter private PeriodType periodType;
	@Getter private BigDecimal totalProfit; 			// 매출액
	@Getter private BigDecimal operatingProfit;		// 영업이익
	@Getter private BigDecimal netIncome; 			// 당기순이익

	@Builder
	public EachGainLossDto(
		String periodValue, PeriodType periodType,
		BigDecimal totalProfit, BigDecimal operatingProfit, BigDecimal netIncome
	){
		this.periodValue = periodValue;
		this.periodType = periodType;
		this.totalProfit = totalProfit;
		this.operatingProfit = operatingProfit;
		this.netIncome = netIncome;
	}
}
