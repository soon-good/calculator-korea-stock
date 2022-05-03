package io.inspect.koreanstockinspector.tdd.fnguide.finance.gainloss;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum GainLossColumn {
    TotalProfit("TotalProfit", "매출액"),
    OperatingProfit("OperatingProfit", "영업이익"),
    NetIncome("NetIncome", "당기순이익");

    @Getter private final String colEn;
    @Getter private final String colKr;

    GainLossColumn(String colEn, String colKr){
        this.colEn = colEn;
        this.colKr = colKr;
    }

    static Map<String, GainLossColumn> colKrMap = new HashMap<>();

    static {
        for(GainLossColumn col : GainLossColumn.values()){
            colKrMap.put(col.getColKr(), col);
        }
    }

    public static GainLossColumn krTypeOf(String krLabel){
        return colKrMap.get(krLabel);
    }
}
