package io.inspect.koreanstockinspector.tdd.finance.crawling.gainloss;

import io.inspect.koreanstockinspector.tdd.common.PeriodType;
import io.inspect.koreanstockinspector.request.fnguide.FnGuidePageParam;
import io.inspect.koreanstockinspector.request.fnguide.ParameterPair;
import io.inspect.koreanstockinspector.request.fnguide.ParameterType;
import io.inspect.koreanstockinspector.tdd.crawling.jsoup.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class FinanceCrawlingFnGuideTest {
    final StringBuilder urlStringBuilder = new StringBuilder();
    final String baseURL = "http://comp.fnguide.com/SVO2/ASP/SVD_Finance.asp";
    final String tablesDivClassSelector = "ul_col2wrap pd_t25";
    final String tableSelector = "ul_co1_c pd_t1";

    final String yearlyGainLossTableSelector = "divSonikY";
    final String quarterlyGainLossTableSelector = "divSonikQ";

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
    @Test
    @DisplayName("HTML 셀렉터, HTML 요소 를 결합한 선택자 생성기능 테스트 #1")
    public void TEST_HTML_SELECTOR_SPECIFIER_QUERY_1(){
        String requestUrl = newFnGuideUrl(FnGuidePageParam.PageType.FINANCE, testParameters1());

        getDocument(requestUrl).ifPresent(document -> {

            HTMLSelectQuery htmlSelectQuery = new HTMLSelectQuery();

            String query = htmlSelectQuery.grandChildrenAll(testElementSelectorPairs1());

            StringBuilder targetSql = new StringBuilder();

            targetSql
                    .append("div[class=").append("\"").append(tablesDivClassSelector).append("\"").append("]")
                    .append(" ")
                    .append("div[class=").append("\"").append(tableSelector).append("\"").append("]");

            assertThat(query).isEqualTo(targetSql.toString());
        });
    }

    public Optional<Elements> grandChildrenAll(Document document, List<ElementSelectorPair> grandChildren){
        final HTMLSelectQuery divSectionQuery = new HTMLSelectQuery();
        String grandChildrenSelector = divSectionQuery.grandChildrenAll(grandChildren);

        if("".equals(grandChildrenSelector) || grandChildrenSelector == null)
            return Optional.empty();

        return Optional.ofNullable(document.select(grandChildrenSelector));
    }

    public Optional<Elements> atParentFindChild(Elements targetElement, ElementSelectorPair parent, ElementSelectorPair child){
        HTMLSelectQuery tableSelQuery = new HTMLSelectQuery(parent.ofSelector());
        tableSelQuery.next(child, NextSelectorType.CHILD);
        return Optional.ofNullable(targetElement.select(tableSelQuery.selectorString()));
    }

    @Test
    @DisplayName("JSoup 셀렉터 기반 조회 - 연간")
    public void TEST_SEARCHING_JSOUP_BASED_YEARLY_DATA(){
        getDocument(newFnGuideUrl(FnGuidePageParam.PageType.FINANCE, testParameters1())).ifPresent(document -> {
            ElementSelectorPair first = ofElementSelectorPair(ElementType.DIV, SelectorType.CLASS, SpecifierType.FULL, tablesDivClassSelector);
            ElementSelectorPair second = ofElementSelectorPair(ElementType.DIV, SelectorType.CLASS, SpecifierType.FULL, tableSelector);

            Elements divGainLossSection = document
                    .select(first.ofSelector().toString())
                    .select(second.ofSelector().toString());

//            System.out.println(divGainLossSection);

            // 포괄 손익 계산 DIV
            ElementSelectorPair yearlyGainLossDiv = ofElementSelectorPair(ElementType.DIV, SelectorType.ID, SpecifierType.FULL, yearlyGainLossTableSelector);
            // 손익 계산 내에서 table 을 파싱
            ElementSelectorPair yearlyGainLossTable = ofElementSelectorPair(ElementType.TABLE, SelectorType.NONE, SpecifierType.NONE, "table");

            Elements yearlyTableElement = divGainLossSection
                    .select(yearlyGainLossDiv.ofSelector().toString())
                    .select(yearlyGainLossTable.ofSelector().toString());

//            System.out.println(yearlyTableElement);

            // 1) 연도 파싱
            List<GainLossPeriods> yearsList = yearlyTableElement.select("tr").tagName("tr")
                    .stream().limit(1)
                    .map(thtd -> {
                        List<String> data = thtd.select("th").tagName("th").eachText().stream().skip(1).limit(4).collect(Collectors.toList());

                        GainLossPeriods periods = GainLossPeriods.builder()
                                .firstPrev(data.get(PeriodType.FIRST_PREV.getIndexAs()))
                                .secondPrev(data.get(PeriodType.SECOND_PREV.getIndexAs()))
                                .thirdPrev(data.get(PeriodType.THIRD_PREV.getIndexAs()))
                                .fourthPrev(data.get(PeriodType.FOURTH_PREV.getIndexAs()))
                                .build();

                        return periods;
                    })
                    .collect(Collectors.toList());

            System.out.println("years = " + yearsList.get(0));

            // 2) 각 항목 파싱 (매출액, 영업이익, 당기순이익)
            List<GainLossDto> values = yearlyTableElement.select("tr").tagName("tr")
                    .stream().skip(1)
                    .filter(thtd -> {
                        if (thtd.tagName("th").select("div").text().equals("매출액")) return true;
                        if (thtd.tagName("th").select("div").text().equals("영업이익")) return true;
                        if (thtd.tagName("th").select("div").text().equals("당기순이익")) return true;
                        else return false;
                    })
                    .map(thtd -> {
                        String label = thtd.tagName("th").select("div").text();
                        String value = thtd.tagName("td").select(".r").text();
                        List<BigDecimal> valueList = Arrays.asList(value.split(" ")).subList(0, 4)
                                .stream()
                                .map(str -> {
                                    try {
                                        Number parse = NumberFormat.getNumberInstance(Locale.US).parse(str);
                                        BigDecimal parsedValue = new BigDecimal(parse.toString());
                                        return parsedValue;
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                })
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
                        return new GainLossDto(GainLossColumn.krTypeOf(label), valueList);
                    })
                    .collect(Collectors.toList());

            System.out.println("values = " + values);

            Map<GainLossColumn, Integer> columnIndexMap = IntStream
                    .range(0, values.size()).boxed()
                    .collect(Collectors.toMap(i -> values.get(i).getType(), Function.identity()));

            System.out.println("columnIndexMap = " + columnIndexMap);

            System.out.println("===AAA===");
            GainLossDto totalProfit = values.get(columnIndexMap.get(GainLossColumn.TotalProfit));
            BigDecimal firstTotalProfit = totalProfit.getValues().get(PeriodType.FIRST_PREV.getIndexAs());
            BigDecimal secondTotalProfit = totalProfit.getValues().get(PeriodType.SECOND_PREV.getIndexAs());
            BigDecimal thirdTotalProfit = totalProfit.getValues().get(PeriodType.THIRD_PREV.getIndexAs());
            BigDecimal fourthTotalProfit = totalProfit.getValues().get(PeriodType.FOURTH_PREV.getIndexAs());

            GainLossDto opProfit = values.get(columnIndexMap.get(GainLossColumn.OperatingProfit));
            BigDecimal firstOpProfit = opProfit.getValues().get(PeriodType.FIRST_PREV.getIndexAs());
            BigDecimal secondOpProfit = opProfit.getValues().get(PeriodType.SECOND_PREV.getIndexAs());
            BigDecimal thirdOpProfit = opProfit.getValues().get(PeriodType.THIRD_PREV.getIndexAs());
            BigDecimal fourthOpProfit = opProfit.getValues().get(PeriodType.FOURTH_PREV.getIndexAs());

            GainLossDto netIncome = values.get(columnIndexMap.get(GainLossColumn.NetIncome));
            BigDecimal firstNetIncome = netIncome.getValues().get(PeriodType.FIRST_PREV.getIndexAs());
            BigDecimal secondNetIncome = netIncome.getValues().get(PeriodType.SECOND_PREV.getIndexAs());
            BigDecimal thirdNetIncome = netIncome.getValues().get(PeriodType.THIRD_PREV.getIndexAs());
            BigDecimal fourthNetIncome = netIncome.getValues().get(PeriodType.FOURTH_PREV.getIndexAs());

            GainLossData firstGainLoss = GainLossData.builder()
                    .periodValue(yearsList.get(0).getFirstPrev())
                    .periodType(PeriodType.FIRST_PREV)
                    .totalProfit(firstTotalProfit)
                    .operatingProfit(firstOpProfit)
                    .netIncome(firstNetIncome)
                    .build();

            GainLossData secondGainLoss = GainLossData.builder()
                    .periodValue(yearsList.get(0).getSecondPrev())
                    .periodType(PeriodType.SECOND_PREV)
                    .totalProfit(secondTotalProfit)
                    .operatingProfit(secondOpProfit)
                    .netIncome(secondNetIncome)
                    .build();

            GainLossData thirdGainLoss = GainLossData.builder()
                    .periodValue(yearsList.get(0).getThirdPrev())
                    .periodType(PeriodType.THIRD_PREV)
                    .totalProfit(thirdTotalProfit)
                    .operatingProfit(thirdOpProfit)
                    .netIncome(thirdNetIncome)
                    .build();

            GainLossData fourthGainLoss = GainLossData.builder()
                    .periodValue(yearsList.get(0).getFourthPrev())
                    .periodType(PeriodType.FOURTH_PREV)
                    .totalProfit(fourthTotalProfit)
                    .operatingProfit(fourthOpProfit)
                    .netIncome(fourthNetIncome)
                    .build();

            GainLossResult d = GainLossResult.builder()
                    .firstPrev(firstGainLoss)
                    .secondPrev(secondGainLoss)
                    .thirdPrev(thirdGainLoss)
                    .fourthPrev(fourthGainLoss)
                    .build();

            System.out.println("d = " + d);
            System.out.println("===");
        });
    }


    @Test
    @DisplayName("JSoup 셀렉터 기반 조회 - 분기")
    public void TEST_SEARCHING_JSOUP_BASED_QUARTERLY_DATA(){
        getDocument(newFnGuideUrl(FnGuidePageParam.PageType.FINANCE, testParameters1())).ifPresent(document -> {
            ElementSelectorPair first = ofElementSelectorPair(ElementType.DIV, SelectorType.CLASS, SpecifierType.FULL, tablesDivClassSelector);
            ElementSelectorPair second = ofElementSelectorPair(ElementType.DIV, SelectorType.CLASS, SpecifierType.FULL, tableSelector);

            Elements divGainLossSection = document
                    .select(first.ofSelector().toString())
                    .select(second.ofSelector().toString());

//            System.out.println(divGainLossSection);

            // 포괄 손익 계산 DIV
            ElementSelectorPair yearlyGainLossDiv = ofElementSelectorPair(ElementType.DIV, SelectorType.ID, SpecifierType.FULL, quarterlyGainLossTableSelector);
            // 손익 계산 내에서 table 을 파싱
            ElementSelectorPair yearlyGainLossTable = ofElementSelectorPair(ElementType.TABLE, SelectorType.NONE, SpecifierType.NONE, "table");

            Elements yearlyTableElement = divGainLossSection
                    .select(yearlyGainLossDiv.ofSelector().toString())
                    .select(yearlyGainLossTable.ofSelector().toString());

//            System.out.println(yearlyTableElement);

            // 1) 연도 파싱
            List<GainLossPeriods> yearsList = yearlyTableElement.select("tr").tagName("tr")
                    .stream().limit(1)
                    .map(thtd -> {
                        List<String> data = thtd.select("th").tagName("th").eachText().stream().skip(1).limit(4).collect(Collectors.toList());

                        GainLossPeriods periods = GainLossPeriods.builder()
                                .firstPrev(data.get(PeriodType.FIRST_PREV.getIndexAs()))
                                .secondPrev(data.get(PeriodType.SECOND_PREV.getIndexAs()))
                                .thirdPrev(data.get(PeriodType.THIRD_PREV.getIndexAs()))
                                .fourthPrev(data.get(PeriodType.FOURTH_PREV.getIndexAs()))
                                .build();

                        return periods;
                    })
                    .collect(Collectors.toList());

            System.out.println("years = " + yearsList.get(0));

            // 2) 각 항목 파싱 (매출액, 영업이익, 당기순이익)
            List<GainLossDto> values = yearlyTableElement.select("tr").tagName("tr")
                    .stream().skip(1)
                    .filter(thtd -> {
                        if (thtd.tagName("th").select("div").text().equals("매출액")) return true;
                        if (thtd.tagName("th").select("div").text().equals("영업이익")) return true;
                        if (thtd.tagName("th").select("div").text().equals("당기순이익")) return true;
                        else return false;
                    })
                    .map(thtd -> {
                        String label = thtd.tagName("th").select("div").text();
                        String value = thtd.tagName("td").select(".r").text();
                        List<BigDecimal> valueList = Arrays.asList(value.split(" ")).subList(0, 4)
                                .stream()
                                .map(str -> {
                                    try {
                                        Number parse = NumberFormat.getNumberInstance(Locale.US).parse(str);
                                        BigDecimal parsedValue = new BigDecimal(parse.toString());
                                        return parsedValue;
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                })
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
                        return new GainLossDto(GainLossColumn.krTypeOf(label), valueList);
                    })
                    .collect(Collectors.toList());

            System.out.println("values = " + values);

            Map<GainLossColumn, Integer> columnIndexMap = IntStream
                    .range(0, values.size()).boxed()
                    .collect(Collectors.toMap(i -> values.get(i).getType(), Function.identity()));

            System.out.println("columnIndexMap = " + columnIndexMap);

            System.out.println("===AAA===");
            GainLossDto totalProfit = values.get(columnIndexMap.get(GainLossColumn.TotalProfit));
            BigDecimal firstTotalProfit = totalProfit.getValues().get(PeriodType.FIRST_PREV.getIndexAs());
            BigDecimal secondTotalProfit = totalProfit.getValues().get(PeriodType.SECOND_PREV.getIndexAs());
            BigDecimal thirdTotalProfit = totalProfit.getValues().get(PeriodType.THIRD_PREV.getIndexAs());
            BigDecimal fourthTotalProfit = totalProfit.getValues().get(PeriodType.FOURTH_PREV.getIndexAs());

            GainLossDto opProfit = values.get(columnIndexMap.get(GainLossColumn.OperatingProfit));
            BigDecimal firstOpProfit = opProfit.getValues().get(PeriodType.FIRST_PREV.getIndexAs());
            BigDecimal secondOpProfit = opProfit.getValues().get(PeriodType.SECOND_PREV.getIndexAs());
            BigDecimal thirdOpProfit = opProfit.getValues().get(PeriodType.THIRD_PREV.getIndexAs());
            BigDecimal fourthOpProfit = opProfit.getValues().get(PeriodType.FOURTH_PREV.getIndexAs());

            GainLossDto netIncome = values.get(columnIndexMap.get(GainLossColumn.NetIncome));
            BigDecimal firstNetIncome = netIncome.getValues().get(PeriodType.FIRST_PREV.getIndexAs());
            BigDecimal secondNetIncome = netIncome.getValues().get(PeriodType.SECOND_PREV.getIndexAs());
            BigDecimal thirdNetIncome = netIncome.getValues().get(PeriodType.THIRD_PREV.getIndexAs());
            BigDecimal fourthNetIncome = netIncome.getValues().get(PeriodType.FOURTH_PREV.getIndexAs());

            GainLossData firstGainLoss = GainLossData.builder()
                    .periodValue(yearsList.get(0).getFirstPrev())
                    .periodType(PeriodType.FIRST_PREV)
                    .totalProfit(firstTotalProfit)
                    .operatingProfit(firstOpProfit)
                    .netIncome(firstNetIncome)
                    .build();

            GainLossData secondGainLoss = GainLossData.builder()
                    .periodValue(yearsList.get(0).getSecondPrev())
                    .periodType(PeriodType.SECOND_PREV)
                    .totalProfit(secondTotalProfit)
                    .operatingProfit(secondOpProfit)
                    .netIncome(secondNetIncome)
                    .build();

            GainLossData thirdGainLoss = GainLossData.builder()
                    .periodValue(yearsList.get(0).getThirdPrev())
                    .periodType(PeriodType.THIRD_PREV)
                    .totalProfit(thirdTotalProfit)
                    .operatingProfit(thirdOpProfit)
                    .netIncome(thirdNetIncome)
                    .build();

            GainLossData fourthGainLoss = GainLossData.builder()
                    .periodValue(yearsList.get(0).getFourthPrev())
                    .periodType(PeriodType.FOURTH_PREV)
                    .totalProfit(fourthTotalProfit)
                    .operatingProfit(fourthOpProfit)
                    .netIncome(fourthNetIncome)
                    .build();

            GainLossResult d = GainLossResult.builder()
                    .firstPrev(firstGainLoss)
                    .secondPrev(secondGainLoss)
                    .thirdPrev(thirdGainLoss)
                    .fourthPrev(fourthGainLoss)
                    .build();

            System.out.println("d = " + d);
            System.out.println("===");
        });
    }


    @Test
    @DisplayName("각 연도별 매출액, 영업이익, 당기순이익에 해당하는 값에 대한 문자열 데이터 파싱")
    public void TEST_PARSE_TH_TBODY(){
        String requestUrl = newFnGuideUrl(FnGuidePageParam.PageType.FINANCE, testParameters1());

        getDocument(requestUrl).ifPresent(document -> {
            Optional<Elements> divSectionEl = grandChildrenAll(document, testElementSelectorPairs1());

            divSectionEl.ifPresent(divElement -> {
                // 포괄 손익 계산 DIV
                ElementSelectorPair yearlyGainLossDiv = ofElementSelectorPair(ElementType.DIV, SelectorType.ID, SpecifierType.FULL, yearlyGainLossTableSelector);
                // 손익 계산 내에서 table 을 파싱
                ElementSelectorPair yearlyGainLossTable = ofElementSelectorPair(ElementType.TABLE, SelectorType.NONE, SpecifierType.NONE, "table");

                atParentFindChild(divElement, yearlyGainLossDiv, yearlyGainLossTable).ifPresent(yearlyTableElement->{

                    // 1) 연도 파싱
                    List<GainLossPeriods> yearsList = yearlyTableElement.select("tr").tagName("tr")
                            .stream().limit(1)
                            .map(thtd -> {
                                List<String> data = thtd.select("th").tagName("th").eachText().stream().skip(1).limit(4).collect(Collectors.toList());

                                GainLossPeriods periods = GainLossPeriods.builder()
                                        .firstPrev(data.get(PeriodType.FIRST_PREV.getIndexAs()))
                                        .secondPrev(data.get(PeriodType.SECOND_PREV.getIndexAs()))
                                        .thirdPrev(data.get(PeriodType.THIRD_PREV.getIndexAs()))
                                        .fourthPrev(data.get(PeriodType.FOURTH_PREV.getIndexAs()))
                                        .build();

                                return periods;
                            })
                            .collect(Collectors.toList());

                    System.out.println("years = " + yearsList.get(0));

                    // 2) 각 항목 파싱 (매출액, 영업이익, 당기순이익)
                    List<GainLossDto> values = yearlyTableElement.select("tr").tagName("tr")
                            .stream().skip(1)
                            .filter(thtd -> {
                                if (thtd.tagName("th").select("div").text().equals("매출액")) return true;
                                if (thtd.tagName("th").select("div").text().equals("영업이익")) return true;
                                if (thtd.tagName("th").select("div").text().equals("당기순이익")) return true;
                                else return false;
                            })
                            .map(thtd -> {
                                String label = thtd.tagName("th").select("div").text();
                                String value = thtd.tagName("td").select(".r").text();
                                List<BigDecimal> valueList = Arrays.asList(value.split(" ")).subList(0, 4)
                                        .stream()
                                        .map(str -> {
                                            try {
                                                Number parse = NumberFormat.getNumberInstance(Locale.US).parse(str);
                                                BigDecimal parsedValue = new BigDecimal(parse.toString());
                                                return parsedValue;
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            return null;
                                        })
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList());
                                return new GainLossDto(GainLossColumn.krTypeOf(label), valueList);
                            })
                            .collect(Collectors.toList());

                    System.out.println("values = " + values);

                    Map<GainLossColumn, Integer> columnIndexMap = IntStream
                            .range(0, values.size()).boxed()
                            .collect(Collectors.toMap(i -> values.get(i).getType(), Function.identity()));

                    System.out.println("columnIndexMap = " + columnIndexMap);

                    System.out.println("===AAA===");
                    GainLossDto totalProfit = values.get(columnIndexMap.get(GainLossColumn.TotalProfit));
                    BigDecimal firstTotalProfit = totalProfit.getValues().get(PeriodType.FIRST_PREV.getIndexAs());
                    BigDecimal secondTotalProfit = totalProfit.getValues().get(PeriodType.SECOND_PREV.getIndexAs());
                    BigDecimal thirdTotalProfit = totalProfit.getValues().get(PeriodType.THIRD_PREV.getIndexAs());
                    BigDecimal fourthTotalProfit = totalProfit.getValues().get(PeriodType.FOURTH_PREV.getIndexAs());

                    GainLossDto opProfit = values.get(columnIndexMap.get(GainLossColumn.OperatingProfit));
                    BigDecimal firstOpProfit = opProfit.getValues().get(PeriodType.FIRST_PREV.getIndexAs());
                    BigDecimal secondOpProfit = opProfit.getValues().get(PeriodType.SECOND_PREV.getIndexAs());
                    BigDecimal thirdOpProfit = opProfit.getValues().get(PeriodType.THIRD_PREV.getIndexAs());
                    BigDecimal fourthOpProfit = opProfit.getValues().get(PeriodType.FOURTH_PREV.getIndexAs());

                    GainLossDto netIncome = values.get(columnIndexMap.get(GainLossColumn.NetIncome));
                    BigDecimal firstNetIncome = netIncome.getValues().get(PeriodType.FIRST_PREV.getIndexAs());
                    BigDecimal secondNetIncome = netIncome.getValues().get(PeriodType.SECOND_PREV.getIndexAs());
                    BigDecimal thirdNetIncome = netIncome.getValues().get(PeriodType.THIRD_PREV.getIndexAs());
                    BigDecimal fourthNetIncome = netIncome.getValues().get(PeriodType.FOURTH_PREV.getIndexAs());

                    GainLossData firstGainLoss = GainLossData.builder()
                            .periodValue(yearsList.get(0).getFirstPrev())
                            .periodType(PeriodType.FIRST_PREV)
                            .totalProfit(firstTotalProfit)
                            .operatingProfit(firstOpProfit)
                            .netIncome(firstNetIncome)
                            .build();

                    GainLossData secondGainLoss = GainLossData.builder()
                            .periodValue(yearsList.get(0).getSecondPrev())
                            .periodType(PeriodType.SECOND_PREV)
                            .totalProfit(secondTotalProfit)
                            .operatingProfit(secondOpProfit)
                            .netIncome(secondNetIncome)
                            .build();

                    GainLossData thirdGainLoss = GainLossData.builder()
                            .periodValue(yearsList.get(0).getThirdPrev())
                            .periodType(PeriodType.THIRD_PREV)
                            .totalProfit(thirdTotalProfit)
                            .operatingProfit(thirdOpProfit)
                            .netIncome(thirdNetIncome)
                            .build();

                    GainLossData fourthGainLoss = GainLossData.builder()
                            .periodValue(yearsList.get(0).getFourthPrev())
                            .periodType(PeriodType.FOURTH_PREV)
                            .totalProfit(fourthTotalProfit)
                            .operatingProfit(fourthOpProfit)
                            .netIncome(fourthNetIncome)
                            .build();

                    GainLossResult d = GainLossResult.builder()
                            .firstPrev(firstGainLoss)
                            .secondPrev(secondGainLoss)
                            .thirdPrev(thirdGainLoss)
                            .fourthPrev(fourthGainLoss)
                            .build();

                    System.out.println("d = " + d);
                    System.out.println("===");

                });
            });
        });
    }

    public static GainLossColumn fromValuesArray(int i, List<GainLossDto> values){
        return values.get(i).getType();
    }

    @Test
    public void testSomething(){
//        https://stackoverflow.com/questions/38318181/convert-intstream-to-map


        Map<Integer, Integer> aaa = IntStream.range(1, 10).boxed().collect(Collectors.toMap(Function.identity(), integer -> integer * 2));
        System.out.println(aaa);
        System.out.println();

        Map<Integer, Integer> a = IntStream.range(1, 10).boxed().collect(Collectors.toMap(Function.identity(), i -> i * 3));
        System.out.println(a);

        Map<Integer, Integer> collect = IntStream.range(1, 10).boxed().collect(Collectors.toMap(Function.identity(), FinanceCrawlingFnGuideTest::multiplyDouble));
        System.out.println(collect);
    }

    public static Integer multiplyDouble(Integer i){
        return i*2;
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
                    .append("div[id=\"").append(yearlyGainLossTableSelector).append("\"").append("]").append(" > ").append("table");

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

    @Test
    public void TEST(){
        GainLossDto netIncome = GainLossDto.builder()
                .type(GainLossColumn.NetIncome)
                .values(List.of(new BigDecimal("111"), new BigDecimal("222"), new BigDecimal("333")))
                .build();

        GainLossDto totalProfit = GainLossDto.builder()
                .type(GainLossColumn.TotalProfit)
                .values(List.of(new BigDecimal("444"), new BigDecimal("555"), new BigDecimal("666")))
                .build();

        GainLossDto salesProfit = GainLossDto.builder()
                .type(GainLossColumn.OperatingProfit)
                .values(List.of(new BigDecimal("777"), new BigDecimal("888"), new BigDecimal("999")))
                .build();

        List<GainLossDto> values = List.of(netIncome, totalProfit, salesProfit);
        Map<GainLossColumn, Integer> columnIndexMap = IntStream.range(0, values.size())
                .boxed()
                .collect(Collectors.toMap(i -> values.get(i).getType(), Function.identity()));

        System.out.println(columnIndexMap);
    }
}
