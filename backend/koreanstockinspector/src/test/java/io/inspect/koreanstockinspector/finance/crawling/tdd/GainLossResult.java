package io.inspect.koreanstockinspector.finance.crawling.tdd;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
public class GainLossResult {
	@Getter private EachGainLossDto firstPrev;
	@Getter private EachGainLossDto secondPrev;
	@Getter private EachGainLossDto thirdPrev;
	@Getter private EachGainLossDto fourthPrev;

	@Builder
	public GainLossResult(
		EachGainLossDto firstPrev,
		EachGainLossDto secondPrev,
		EachGainLossDto thirdPrev,
		EachGainLossDto fourthPrev
	){
		this.firstPrev = firstPrev;
		this.secondPrev = secondPrev;
		this.thirdPrev = thirdPrev;
		this.fourthPrev = fourthPrev;
	}
}
