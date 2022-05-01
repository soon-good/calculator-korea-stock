package io.inspect.koreanstockinspector.finance.crawling.tdd;

import lombok.Builder;

import java.util.List;

public class HTMLSelectQuery {
    private final List<ElementSelectorPair> selectorPairs;
    private final StringBuilder builder = new StringBuilder();

    @Builder
    public HTMLSelectQuery(List<ElementSelectorPair> selectorPairs){
        this.selectorPairs = selectorPairs;
    }

    public StringBuilder query(ElementSelectorPair pair){
        return builder.append(pair.ofSelector());
    }

    public StringBuilder and(){
        return builder.append(" ");
    }

    public String queryString(){
        selectorPairs.forEach(
                pair -> query(pair).append(" ")
        );
        return builder.toString().trim();
    }
}
