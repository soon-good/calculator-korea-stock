package io.inspect.koreanstockinspector.tdd.crawling.jsoup;

import java.util.List;

public class HTMLSelectQuery {
    private final StringBuilder builder = new StringBuilder();

    public HTMLSelectQuery(){}
    public HTMLSelectQuery(StringBuilder firstSelector){
        builder.append(firstSelector);
    }

    public StringBuilder selectorOf(ElementSelectorPair pair){
        return builder.append(pair.ofSelector());
    }

    // E > F : E 요소의 자식인 F 요소
    // E F : E 요소의 자손인 F 요소
    public String grandChildrenAll(List<ElementSelectorPair> selectorPairs){
        selectorPairs.forEach(
                pair -> selectorOf(pair).append(NextSelectorType.GRAND_CHILD.getSelector())
        );
        return builder.toString().trim();
    }

    public StringBuilder next(ElementSelectorPair pair, NextSelectorType nextSelectorType){
        return builder
                .append(nextSelectorType.getSelector())
                .append(pair.ofSelector());
    }

    public String selectorString(){
        return builder.toString();
    }
}
