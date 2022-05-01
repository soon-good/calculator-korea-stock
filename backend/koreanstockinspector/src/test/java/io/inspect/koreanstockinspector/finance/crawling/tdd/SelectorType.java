package io.inspect.koreanstockinspector.finance.crawling.tdd;

import lombok.Getter;

public enum SelectorType {
    CLASS("class=", "."), ID("id=", "#");
    @Getter
    private final String fullSelector;
    @Getter private final String shortSelector;

    SelectorType(String fullSelector, String shortSelector){
        this.fullSelector = fullSelector;
        this.shortSelector = shortSelector;
    }
}
