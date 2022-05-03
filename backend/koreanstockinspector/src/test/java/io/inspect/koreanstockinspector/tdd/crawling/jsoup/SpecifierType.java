package io.inspect.koreanstockinspector.tdd.crawling.jsoup;

import lombok.Getter;

public enum SpecifierType {
    FULL("FULL"), SHORT("SHORT"), NONE("NONE");
    @Getter
    private final String specifierTypeName;

    SpecifierType(String specifierTypeName){
        this.specifierTypeName = specifierTypeName;
    }
}
