package io.inspect.koreanstockinspector.finance.crawling.tdd;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
public class GainLossResult {
	@Getter private GainLossData firstPrev;
	@Getter private GainLossData secondPrev;
	@Getter private GainLossData thirdPrev;
	@Getter private GainLossData fourthPrev;

	@Builder
	public GainLossResult(
		GainLossData firstPrev,
		GainLossData secondPrev,
		GainLossData thirdPrev,
		GainLossData fourthPrev
	){
		this.firstPrev = firstPrev;
		this.secondPrev = secondPrev;
		this.thirdPrev = thirdPrev;
		this.fourthPrev = fourthPrev;
	}
}
