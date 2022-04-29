package io.inspect.koreanstockinspector.request.fnguide;

import lombok.Getter;

public enum ParameterType {
    pGb("pGb", "페이지구분"),
    gicode("gicode", "기업코드");

    @Getter private final String paramName;
    @Getter private final String paramDesc;

    ParameterType(String paramName, String paramDesc){
        this.paramName = paramName;
        this.paramDesc = paramDesc;
    }
}
