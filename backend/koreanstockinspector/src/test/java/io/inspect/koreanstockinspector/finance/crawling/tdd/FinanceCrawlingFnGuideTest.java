package io.inspect.koreanstockinspector.finance.crawling.tdd;

import io.inspect.koreanstockinspector.request.fnguide.FnGuidePageParam;
import io.inspect.koreanstockinspector.request.fnguide.ParameterPair;
import io.inspect.koreanstockinspector.request.fnguide.ParameterType;
import lombok.Builder;
import lombok.Getter;
import org.assertj.core.api.Assertions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class FinanceCrawlingFnGuideTest {
    final StringBuilder urlStringBuilder = new StringBuilder();
    final String baseURL = "http://comp.fnguide.com/SVO2/ASP/SVD_Finance.asp";
    final String tablesDivClassSelector = "ul_col2wrap pd_t25";
    final String tableSelector = "ul_co1_c pd_t1";

    final String gainLossTableSelector = "divSonikY";

    record Param(String pageNoName, Integer pageNoValue, String companyNoName, String companyNoValue){};
    record TimeKeyDataValue(String time, String data){};
    record TableRow(String name, List<TimeKeyDataValue> eachTimeValues){};



    @BeforeEach
    public void testLocalInit(){

    }

    //TODO
    // 우선 내가 구하려고 하는 것은 모든 정보가 아니다. 그냥 난 매출액, 영업이익, 당기순이익 이다.
    // 1) URL 구하는 기능 공통화
    // 2) table 태그 파싱하는 기능 함수로 만들어서 공통화
    // - 소기능으로 분류될만한 것들
    //      ...
    // 3) 구하려는 기능에서 매출액, 영업이익, 당기순이익만 뽑아내는 기능
    // 2), 3) 은 서로 같은 함수 내 다른 작은 메서드들로 분류되는 메서드로 분기될 수 있다.

    // 1) URL 구하는 기능 공통화 및 각종 파라미터, URL 상수화
    // = 내가 원하는것 ? 최대한 가변적이지 않은 프로그램으로 만들기
    @Test
    @DisplayName("크롤링할 페이지의 url 과 파라미터들을 조합한다.")
    public void TEST_REQUEST_URL(){
        ParameterPair pGb = new ParameterPair(ParameterType.pGb, "1");
        ParameterPair gicode = new ParameterPair(ParameterType.gicode, "A005930");

        FnGuidePageParam fnGuideParam = FnGuidePageParam.builder()
                .pageType(FnGuidePageParam.PageType.FINANCE)
                .parameterPairs(List.of(pGb, gicode))
                .build();

        String testURL = fnGuideParam.buildUrl();
        assertThat(testURL).isEqualTo("http://comp.fnguide.com/SVO2/ASP/SVD_Finance.asp?pGb=1&gicode=A005930");
    }

    public Optional<Document> getDocument(String url){
        try {
            return Optional.ofNullable(Jsoup.connect(url).get());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public String newFnGuideUrl(FnGuidePageParam.PageType pageType, List<ParameterPair> parameterPairs){
        return FnGuidePageParam.builder()
                .pageType(FnGuidePageParam.PageType.FINANCE)
                .parameterPairs(parameterPairs)
                .build()
                .buildUrl();
    }

    // 2) 테이블 태그 파싱 기능
    @Test
    @DisplayName("")
    public void TEST_FNGUIDE_CONNECT_SUCCESSFUL(){
        String requestUrl = newFnGuideUrl(FnGuidePageParam.PageType.FINANCE, testParameters1());

        Optional<Document> document = getDocument(requestUrl);
        assertThat(document).isNotEmpty();
    }

    // 2 ) ===== HTML 파싱 기능 테스트코드 시작

    enum ElementType{
        DIV("div"), TABLE("table"), TBODY("table"), TR("TR"), TH("th");

        @Getter private final String el;

        ElementType(String el){
            this.el = el;
        }
    }

    enum SelectorType{
        CLASS("class=", "."), ID("id=", "#");
        @Getter private final String fullSelector;
        @Getter private final String shortSelector;

        SelectorType(String fullSelector, String shortSelector){
            this.fullSelector = fullSelector;
            this.shortSelector = shortSelector;
        }
    }

    enum SpecifierType{
        FULL("FULL"), SHORT("SHORT");
        @Getter private final String specifierTypeName;

        SpecifierType(String specifierTypeName){
            this.specifierTypeName = specifierTypeName;
        }
    }

    static class ElementSelectorPair{
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

        public StringBuilder ofSelector(){
            if(specifierType.equals(SpecifierType.FULL)){
                return fullSelector(this);
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

    static class HTMLSelectQuery {
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

    @Test
    @DisplayName("HTML 셀렉터, HTML 요소 를 결합한 선택자 생성기능 테스트 #1")
    public void TEST_HTML_SELECTOR_SPECIFIER_QUERY_1(){
        String requestUrl = newFnGuideUrl(FnGuidePageParam.PageType.FINANCE, testParameters1());

        getDocument(requestUrl).ifPresent(document -> {

            HTMLSelectQuery htmlSelectQuery = HTMLSelectQuery.builder()
                    .selectorPairs(testElementSelectorPairs1())
                    .build();

            String query = htmlSelectQuery.queryString();

            StringBuilder targetSql = new StringBuilder();

            targetSql
                    .append("div[class=").append("\"").append(tablesDivClassSelector).append("\"").append("]")
                    .append(" ")
                    .append("div[class=").append("\"").append(tableSelector).append("\"").append("]");

            assertThat(query).isEqualTo(targetSql.toString());
        });
    }

    // URL 테스트 용도 파라미터 생성
    public List<ParameterPair> testParameters1(){
        ParameterPair pGb = new ParameterPair(ParameterType.pGb, "1");
        ParameterPair gicode = new ParameterPair(ParameterType.gicode, "A005930");
        return List.of(pGb, gicode);
    }

    public ElementSelectorPair ofElementSelectorPair(ElementType elementType, SelectorType selectorType, SpecifierType specifierType, String specifier){
        Objects.requireNonNull(elementType);
        Objects.requireNonNull(selectorType);
        Objects.requireNonNull(specifierType);

        return ElementSelectorPair.builder()
                .elementType(elementType)
                .selectorType(selectorType)
                .specifierType(specifierType)
                .specifier(specifier)
                .build();
    }

    // 테스트 용도 선택자 리스트 파라미터 생성
    public List<ElementSelectorPair> testElementSelectorPairs1(){
        ElementSelectorPair sel1 = ofElementSelectorPair(ElementType.DIV, SelectorType.CLASS, SpecifierType.FULL, tablesDivClassSelector);
        ElementSelectorPair sel2 = ofElementSelectorPair(ElementType.DIV, SelectorType.CLASS, SpecifierType.FULL, tableSelector);
        return List.of(sel1, sel2);
    }

    // 오케이... 여기까지는 일단 뭐가 있는지 해봤다. 이제부터는 위의 테스트 코드에서 리팩토링을 하면서 진행할 예정
    @Test
    public void TEST_FNGUIDE_PAGE_CONNECT_SUCCESS(){
        Param param = new Param("pGB", 1, "gicode", "A005930");
        String url = urlStringBuilder.append(baseURL).append("?")
                .append(param.pageNoName).append("=").append(param.pageNoValue)
                .append("&")
                .append(param.companyNoName).append("=").append(param.companyNoValue)
                .toString();

        // selector ( DIV, class, "selector")
        // and (" ")
        // selector (DIV, class, "selector");

        try {
            Document document = Jsoup.connect(url).get();
            StringBuilder tableSelectorBuilder = new StringBuilder();
            tableSelectorBuilder
                    .append("div[class=").append("\"").append(tablesDivClassSelector).append("\"").append("]")
                    .append(" ")
                    .append("div[class=").append("\"").append(tableSelector).append("\"").append("]");

//            Elements select = document.select("div[class=\"ul_col2wrap pd_t25\"] div[class=\"ul_co1_c pd_t1\"]");
            Elements allTableEl = document.select(tableSelectorBuilder.toString());

            StringBuilder gainLossTableSelectorBuilder = new StringBuilder();
            gainLossTableSelectorBuilder
                    .append("div[id=\"").append(gainLossTableSelector).append("\"").append("]").append(" > ").append("table");

            // 1) 포괄 손익 계산서
            Elements gainLossTableEl = allTableEl.select(gainLossTableSelectorBuilder.toString());

            StringBuilder gainLossTableYearsSelector = new StringBuilder();
            gainLossTableYearsSelector.append("th[scope=").append("\"").append("col").append("\"").append("]");
            Elements yearsEl = gainLossTableEl.select(gainLossTableYearsSelector.toString());

            List<String> list = yearsEl.eachText();
            List<String> years = IntStream.range(0, list.size())
                    .filter(i -> i != 0)
                    .filter(i -> i < list.size() - 2)
                    .mapToObj(i -> list.get(i))
                    .collect(Collectors.toList());

            System.out.println(years);

            StringBuilder tbodyRowSelector = new StringBuilder();
            tbodyRowSelector.append("tbody tr");
//            tbodyBuilder.append("tbody tr th,td");
//            System.out.println(gainLossTableEl.select(tbodyRowSelector.toString()));
//            gainLossTableEl.select(tbodyRowSelector.toString())
//                    .stream()
//                    .forEach(a -> System.out.println(a));

            Elements eachRowsEl = gainLossTableEl.select(tbodyRowSelector.toString());

            System.out.println(eachRowsEl);

            IntStream.range(0, eachRowsEl.size())
                    .mapToObj(eachRowsEl::eq)
//                    .map(e -> {
//                        new TableRow()
//                    })
                    .forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
