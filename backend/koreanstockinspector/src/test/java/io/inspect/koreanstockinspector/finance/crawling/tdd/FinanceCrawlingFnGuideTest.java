package io.inspect.koreanstockinspector.finance.crawling.tdd;

import io.inspect.koreanstockinspector.request.fnguide.FnGuidePageParam;
import io.inspect.koreanstockinspector.request.fnguide.ParameterPair;
import io.inspect.koreanstockinspector.request.fnguide.ParameterType;

import org.assertj.core.api.Assertions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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

    @Test
    public void TEST_INT_STREAM(){
        List<String> words = List.of("A","B","C");

        // IntStream.range(0, words.size())
        //     .

    }

    public StringBuilder appendKeyPair(ParameterPair pair, StringBuilder builder){
        if(Optional.ofNullable(builder).isEmpty()) builder = new StringBuilder();

        builder.append(pair.getParameterType().getParamName())
            .append("=")
            .append(pair.getValue());

        return builder;
    }

    public StringBuilder appendAnd(StringBuilder builder){
        return builder.append("&");
    }

    public String stripLastAnd(StringBuilder builder){
        if(builder.toString().lastIndexOf("&") == builder.toString().length()-1)
            return builder.toString().substring(0, builder.toString().length()-1);
            // if()
        // return builder.toString().substring(0, builder.toString().lastIndexOf("&"));
        return builder.toString();
    }

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

        StringBuilder builder = new StringBuilder();
        builder.append(FnGuidePageParam.PageType.FINANCE.getBaseUrl()).append("?");

        fnGuideParam.getParameterPairs()
            .stream()
            .map(pair -> appendKeyPair(pair, builder))
            .forEach(strBuilder -> appendAnd(strBuilder));

        String targetUrl = stripLastAnd(builder);
        Assertions.assertThat(targetUrl).isEqualTo("http://comp.fnguide.com/SVO2/ASP/SVD_Finance.asp?pGb=1&gicode=A005930");

        // 이 부분은 조금 수정이 필요하다. (for 문 ~ requestUrl 생성 부분)
        // for(ParameterPair pair : fnGuideParam.getParameterPairs()){
        //     builder.append(pair.getParameterType().getParamName())
        //             .append("=")
        //             .append(pair.getValue());
        //
        //     builder.append("&");
        // }
        //
        // String requestUrl = builder.toString();
        // requestUrl = requestUrl.substring(0, requestUrl.length()-1);
        //
        // Assertions.assertThat(requestUrl).isEqualTo("http://comp.fnguide.com/SVO2/ASP/SVD_Finance.asp?pGb=1&gicode=A005930");
    }

//    @Test
//    public void stringBuildertest(){
//        StringBuilder builder = new StringBuilder();
//        builder.append(FnGuidePageParam.PageType.FINANCE);
//
//        List.of()
//    }

    public Document requestFnGuidePage(String url, Param param){
//        Jsoup.connect()
        return null;
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
