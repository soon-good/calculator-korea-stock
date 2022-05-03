package io.inspect.koreanstockinspector.tdd.crawling.jsoup;

import lombok.Getter;

public enum SelectorType {
    CLASS("class=", "."),
    ID("id=", "#"),
    SCOPE("scope=", "scope"),
    NONE("", "");

    @Getter private final String fullSelector;
    @Getter private final String shortSelector;

    SelectorType(String fullSelector, String shortSelector){
        this.fullSelector = fullSelector;
        this.shortSelector = shortSelector;
    }
}
