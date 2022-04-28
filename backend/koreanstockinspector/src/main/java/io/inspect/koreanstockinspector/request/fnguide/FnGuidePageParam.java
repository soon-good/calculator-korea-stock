package io.inspect.koreanstockinspector.request.fnguide;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

// test 패키지에서 시작하면 좋지만, 롬복을 사용하는게 편해져서 소스레벨에서 시작했다.
// 상수코드의 경우는 소스레벨에서 시작해도 되는 것 같지만,
// 테스트 레벨에서 lombok을 허용하는 방식으로 바꿔서 해도 나쁘지 않나 싶다.
// 왜냐하면 소스코드가 주기적으로 바뀌기 때문.
// 완성된 코드만 소스레벨로 옮기겠다. 하는 마인드로 다시한번 바꿔볼까? 생각중
public class FnGuidePageParam {
    @Getter private final PageType pageType;
    @Getter private final List<ParameterPair> parameterPairs;

    @Builder
    public FnGuidePageParam(
        PageType pageType,
        List<ParameterPair> parameterPairs
    ){
        this.pageType = pageType;
        this.parameterPairs = parameterPairs;
    }

    public String newRequestUrl(){
        StringBuilder builder = new StringBuilder();
        builder.append(pageType.getBaseUrl()).append("?");
        // STUB

//        parameterPairs.forEach(parameterPair -> {builder.append(parameterPair.)})
//
//                .append(param.pageNoName).append("=").append(param.pageNoValue)
//                .append("&")
//                .append(param.companyNoName).append("=").append(param.companyNoValue)
//                .toString();
        return null;
    }

    public enum PageType{
        MAIN("MAIN", "Snapshot", "https://comp.fnguide.com/SVO2/ASP/SVD_Main.asp"){
            @Override
            public String requestUrl(FnGuidePageParam param) {
                return null;
            }
        },
        COMPANY_OVERVIEW("COMPANY_OVERVIEW", "기업개요", "https://comp.fnguide.com/SVO2/ASP/SVD_Corp.asp"){
            @Override
            public String requestUrl(FnGuidePageParam param) {
                return null;
            }
        },
        FINANCE("FINANCE", "재무제표", "http://comp.fnguide.com/SVO2/ASP/SVD_Finance.asp"){
            @Override
            public String requestUrl(FnGuidePageParam param) {
                return null;
            }
        };

        @Getter private final String menuEn;
        @Getter private final String menuKr;
        @Getter private final String baseUrl;

        PageType(String menuEn, String menuKr, String baseUrl){
            this.menuEn = menuEn;
            this.menuKr = menuKr;
            this.baseUrl = baseUrl;
        }

        public abstract String requestUrl(FnGuidePageParam param);
    }
}
