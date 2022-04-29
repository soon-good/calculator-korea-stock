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
    private final StringBuilder builder = new StringBuilder();

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

    public ParameterPair pair(ParameterPair pair){
        builder.append(pair.getParameterType().getParamName())
                .append("=")
                .append(pair.getValue());
        return pair;
    }

    public void and(ParameterPair pair){
        builder.append("&");
    }

    public String stripLastAnd(){
        if(builder.lastIndexOf("&") == builder.length() -1)
            return builder.substring(0, builder.length()-1);
        return builder.toString();
    }

    public String buildUrl(){
        builder.append(pageType.getBaseUrl()).append("?");

        parameterPairs.stream()
                .map(this::pair)
                .forEach(this::and);

        // 이 부분은 조금 마음에 안든다. ㅠㅠ
        return stripLastAnd();
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
