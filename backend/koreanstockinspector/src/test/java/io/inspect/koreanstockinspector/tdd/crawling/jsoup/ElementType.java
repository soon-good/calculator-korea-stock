package io.inspect.koreanstockinspector.tdd.crawling.jsoup;

import lombok.Getter;

public enum ElementType {
    DIV("div"), TABLE("table"), TBODY("table"), TR("TR"), TH("th");

    @Getter
    private final String el;

    ElementType(String el){
        this.el = el;
    }
}
