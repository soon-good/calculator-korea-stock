package io.inspect.koreanstockinspector.finance.crawling.tdd;

import lombok.Getter;

public enum NextSelectorType {
    GRAND_CHILD(" "), CHILD(">");

    @Getter private final String selector;

    NextSelectorType(String selector){
        this.selector = selector;
    }
}
