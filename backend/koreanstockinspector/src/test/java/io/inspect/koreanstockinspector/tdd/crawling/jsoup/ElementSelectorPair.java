package io.inspect.koreanstockinspector.tdd.crawling.jsoup;

import lombok.Builder;
import lombok.Getter;

public class ElementSelectorPair {
    @Getter private final ElementType elementType;
    @Getter private final SelectorType selectorType;
    @Getter private final SpecifierType specifierType;
    @Getter private final String specifier;

    private final StringBuilder builder = new StringBuilder();

    @Builder
    public ElementSelectorPair(ElementType elementType, SelectorType selectorType, SpecifierType specifierType, String specifier){
        this.elementType = elementType;
        this.selectorType = selectorType;
        this.specifierType = specifierType;
        this.specifier = specifier;
    }

    // 테스트코드를 만들면서 소기능으로 분해됬다. 좋은 습관이다.
    public String ofEl(ElementSelectorPair pair){
        return pair.getElementType().getEl();
    }

    // 테스트코드를 만들면서 소기능으로 분해됬다. 좋은 습관이다.
    public String ofFullSelector(ElementSelectorPair pair){
        return pair.getSelectorType().getFullSelector();
    }

    public String ofSpecifier(ElementSelectorPair pair){
        return pair.getSpecifier();
    }

    public StringBuilder fullSelector(ElementSelectorPair pair){
        return builder
                .append(ofEl(pair))                                         // div
                .append("[")                                                //      [
                .append(ofFullSelector(pair))                               //          class=
                .append("\"").append(ofSpecifier(pair)).append("\"")        //          "ul_col2wrap pd_t25"
                .append("]");                                               //      ]
    }

    public String noneSelector(ElementSelectorPair pair){
        return ofEl(pair);
    }

    public StringBuilder ofSelector(){
        if(specifierType.equals(SpecifierType.FULL)){
            return fullSelector(this);
        }
        else if(specifierType.equals(SpecifierType.NONE)){
            return new StringBuilder(noneSelector(this));
        }
        return fullSelector(this); // default;
    }

    public StringBuilder ofSelector(ElementSelectorPair pair){
        if(specifierType.equals(SpecifierType.FULL)){
            return fullSelector(pair);
        }
        return fullSelector(pair); // default;
    }
}
