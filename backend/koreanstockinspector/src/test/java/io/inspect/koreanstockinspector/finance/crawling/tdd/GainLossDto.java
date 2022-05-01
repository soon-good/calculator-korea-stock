package io.inspect.koreanstockinspector.finance.crawling.tdd;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
public class GainLossDto {
    @Getter private final GainLossColumn type;
    @Getter private final List<String> values;

    @Builder
    public GainLossDto(GainLossColumn type, List<String> values){
        this.type = type;
        this.values = values;
    }
}
