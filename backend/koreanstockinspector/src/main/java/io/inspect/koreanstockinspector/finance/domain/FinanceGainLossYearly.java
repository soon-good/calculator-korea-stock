package io.inspect.koreanstockinspector.finance.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import io.inspect.koreanstockinspector.common.LocalDateTimeConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@NoArgsConstructor
@DynamoDBTable(tableName = "FinanceGainLossYearly")
public class FinanceGainLossYearly {

//    @Id
//    @Getter @Setter private FinanceGainLossYearlyId financeGainLossYearlyId;

    @Getter @Setter private BigDecimal totalProfit;
    @Getter @Setter private BigDecimal operatingProfit;
    @Getter @Setter private BigDecimal netIncome;

    @Getter @Setter
    @DynamoDBHashKey(attributeName = "Ticker")
    private String ticker;

    private LocalDateTime priceDate;

    @Builder
    public FinanceGainLossYearly(
            BigDecimal totalProfit,
            BigDecimal operatingProfit,
            BigDecimal netIncome,
            String ticker,
            LocalDateTime priceDate
    ){
        this.totalProfit = totalProfit;
        this.operatingProfit = operatingProfit;
        this.netIncome = netIncome;
        this.ticker = ticker;
        this.priceDate = priceDate;
    }

//    @DynamoDBHashKey(attributeName = "Ticker")
//    private String getTicker(){
//        if(isFinanceGainLossYearlyIdEmpty())
//            return null;
//        else
//            return financeGainLossYearlyId.getTicker();
//    }
//    private void setTicker(String ticker){
//        if(isFinanceGainLossYearlyIdEmpty()){
//            financeGainLossYearlyId = new FinanceGainLossYearlyId();
//        }
//        financeGainLossYearlyId.setTicker(ticker);
//    }

    @DynamoDBRangeKey(attributeName = "PriceDate")
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    public LocalDateTime getPriceDate(){
//        if(isFinanceGainLossYearlyIdEmpty())
//            return null;
//        else
//            return financeGainLossYearlyId.getPriceDate();
        return this.priceDate;
    }

    public void setPriceDate(LocalDateTime priceDate){
//        if(isFinanceGainLossYearlyIdEmpty()){
//            financeGainLossYearlyId = new FinanceGainLossYearlyId();
//        }
//        financeGainLossYearlyId.setPriceDate(priceDate);
        this.priceDate = priceDate;
    }

    public boolean isFinanceGainLossYearlyIdEmpty(){
//        return Optional.ofNullable(financeGainLossYearlyId).isEmpty();
        return true;
    }
}
