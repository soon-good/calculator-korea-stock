package io.inspect.koreanstockinspector.tdd.fnguide.finance.gainloss;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@ToString
public class GainLossDto {
    @Getter private final GainLossColumn type;
//    @Getter private final List<String> values;
    @Getter private final List<BigDecimal> values;

    @Builder
    public GainLossDto(GainLossColumn type, List<BigDecimal> values){
        this.type = type;
        this.values = values;
    }
}
