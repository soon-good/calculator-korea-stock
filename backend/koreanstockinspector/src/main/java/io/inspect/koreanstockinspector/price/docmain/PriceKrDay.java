package io.inspect.koreanstockinspector.price.docmain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import io.inspect.koreanstockinspector.common.LocalDateTimeConverter;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@DynamoDBTable(tableName = "PriceKrDay")
public class PriceKrDay {
    @DynamoDBHashKey(attributeName = "Ticker")
    @Getter @Setter
    private String ticker;

    @DynamoDBRangeKey(attributeName = "PriceDate")
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @Getter @Setter
    private LocalDateTime priceDate;

    @Getter @Setter
    @DynamoDBAttribute(attributeName = "open")
    private BigDecimal open;

    @Getter @Setter
    @DynamoDBAttribute(attributeName = "high")
    private BigDecimal high;

    @Getter @Setter
    @DynamoDBAttribute(attributeName = "low")
    private BigDecimal low;

    @Getter @Setter
    @DynamoDBAttribute(attributeName = "close")
    private BigDecimal close;

    @Getter @Setter
    @DynamoDBAttribute(attributeName = "volume")
    private BigDecimal volume;
}
