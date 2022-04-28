package io.inspect.koreanstockinspector.request.fnguide;

import lombok.Builder;
import lombok.Getter;

public class ParameterPair {

    @Getter private ParameterType parameterType;
    @Getter private String value;

    @Builder
    public ParameterPair(
            ParameterType parameterType,
            String value
    ){
        this.parameterType = parameterType;
        this.value = value;
    }

}
