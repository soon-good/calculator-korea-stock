package io.inspect.koreanstockinspector.tdd.fnguide.main;

import io.inspect.koreanstockinspector.request.fnguide.FnGuidePageParam;
import io.inspect.koreanstockinspector.request.fnguide.ParameterPair;
import io.inspect.koreanstockinspector.request.fnguide.ParameterType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class MainPageTest {
    Logger logger = LoggerFactory.getLogger(MainPageTest.class);

    private final String MAIN_PAGE_URL = "https://comp.fnguide.com/SVO2/ASP/SVD_Main.asp?pGB=1&gicode=A005930";

    @Test
    @DisplayName("현재 주가 크롤링")
    public void TEST_SELECT_CURRENT_PRICE(){
        ParameterPair pGb = new ParameterPair(ParameterType.pGb, "1");
        ParameterPair gicode = new ParameterPair(ParameterType.gicode, "A005930");

        FnGuidePageParam fnGuideParam = FnGuidePageParam.builder()
                .pageType(FnGuidePageParam.PageType.MAIN)
                .parameterPairs(List.of(pGb, gicode))
                .build();

        getDocument(fnGuideParam.buildUrl())
                .ifPresent(document -> {
                    try {
                        Elements elements = document.selectXpath("//*[@id=\"svdMainChartTxt11\"]");
                        Number parse = NumberFormat.getNumberInstance(Locale.US).parse(elements.text());
                        BigDecimal parsedValue = new BigDecimal(parse.toString());
                        System.out.println(parsedValue);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Test
    @DisplayName("발행주식수 크롤링")
    public void TEST_SELECT_STOCK_CNT(){
        //*[@id="svdMainGrid1"]/table/tbody/tr[7]/td[1]
        ParameterPair pGb = new ParameterPair(ParameterType.pGb, "1");
        ParameterPair gicode = new ParameterPair(ParameterType.gicode, "A005930");

        FnGuidePageParam fnGuideParam = FnGuidePageParam.builder()
                .pageType(FnGuidePageParam.PageType.MAIN)
                .parameterPairs(List.of(pGb, gicode))
                .build();

        getDocument(fnGuideParam.buildUrl())
                .ifPresent(document -> {
                    try {
                        Elements elements = document.selectXpath("//*[@id=\"svdMainGrid1\"]/table/tbody/tr[7]/td[1]");
                        System.out.println(elements.text());
                        Number parse = NumberFormat.getNumberInstance(Locale.US).parse(elements.text().split("/")[0].trim());
                        BigDecimal parsedValue = new BigDecimal(parse.toString());
                        System.out.println(parsedValue);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                });

    }

    public Optional<Document> getDocument(String url){
        try {
            return Optional.ofNullable(Jsoup.connect(url).get());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
