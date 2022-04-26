package io.inspect.koreanstockinspector.financial.crawling.tdd;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class FinancialCrawlingFnGuideTest {
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
            Elements select = document.select(tableSelectorBuilder.toString());

            StringBuilder gainLossTableSelectorBuilder = new StringBuilder();
            gainLossTableSelectorBuilder
                    .append("div[id=\"").append(gainLossTableSelector).append("\"").append("]").append(" > ").append("table");

//            int size = select.select("div[id=\"divSonikY\"] > table").size();
//            int size = select.select(gainLossTableSelectorBuilder.toString()).size();

            Elements gainLossTableEl = select.select(gainLossTableSelectorBuilder.toString());

            StringBuilder yearListBuilder = new StringBuilder();
            yearListBuilder.append("th[scope=").append("\"").append("col").append("\"").append("]");
            Elements th = gainLossTableEl.select(yearListBuilder.toString());

            List<String> list = th.eachText();
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
