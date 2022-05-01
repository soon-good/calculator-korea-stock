package io.inspect.koreanstockinspector.finance.crawling.tdd;

import lombok.Getter;

public enum SpecifierType {
    FULL("FULL"), SHORT("SHORT");
    @Getter
    private final String specifierTypeName;

    SpecifierType(String specifierTypeName){
        this.specifierTypeName = specifierTypeName;
    }
}
