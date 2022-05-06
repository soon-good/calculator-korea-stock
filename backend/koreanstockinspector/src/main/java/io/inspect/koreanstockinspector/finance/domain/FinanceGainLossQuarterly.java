package io.inspect.koreanstockinspector.finance.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import io.inspect.koreanstockinspector.common.LocalDateTimeConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@DynamoDBTable(tableName = "FinanceGainLossQuarterly")
public class FinanceGainLossQuarterly {
    @Getter @Setter private BigDecimal totalProfit;
    @Getter @Setter private BigDecimal operatingProfit;
    @Getter @Setter private BigDecimal netIncome;

    @Getter @Setter
    @DynamoDBHashKey(attributeName = "Ticker")
    private String ticker;

    @Getter @Setter
    @DynamoDBHashKey(attributeName = "PriceDate")
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    private LocalDateTime priceDate;

    @Builder
    public FinanceGainLossQuarterly(
            String ticker,
            LocalDateTime priceDate,
            BigDecimal totalProfit,
            BigDecimal operatingProfit,
            BigDecimal netIncome
    ){
        this.ticker = ticker;
        this.priceDate = priceDate;
        this.totalProfit = totalProfit;
        this.operatingProfit = operatingProfit;
        this.netIncome = netIncome;
    }
}
