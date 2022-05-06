package io.inspect.koreanstockinspector.tdd.finance.repository.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import io.inspect.koreanstockinspector.finance.domain.FinanceGainLossQuarterly;
import io.inspect.koreanstockinspector.testutils.DockerLocalDynamoDbTestConfigUtils;
import io.inspect.koreanstockinspector.testutils.DockerLocalDynamoDbTestConfigUtils.LocalDynamoDbEnvironment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

public class LocalDynamoFinanceQuarterlyCRUDTest {
    AmazonDynamoDB amazonDynamoDB;
    DynamoDBMapper dynamoDBMapper;

    @BeforeEach
    public void testLocalInit(){
        LocalDynamoDbEnvironment localDynamoDbEnvironment = DockerLocalDynamoDbTestConfigUtils.newLocalDynamoDbEnvironment();
        amazonDynamoDB = localDynamoDbEnvironment.amazonDynamoDB();
        dynamoDBMapper = localDynamoDbEnvironment.dynamoDBMapper();
    }

    // AmazonDynamoDB SDK 를 사용하지 않고 DynamodbMapper 를 이용해 테이블을 생성
    @Order(1)
    @Test
    @DisplayName("FinanceGainLossQuarterly 테이블 생성 >> spring-data-dynamodb 의 DynamodbMapper 를 이용해 테이블을 생성")
    public void TEST_CRAETE_TABLE_BY_ORM_MAPPER_DYNAMODBMAPPER(){
        CreateTableRequest request = dynamoDBMapper.generateCreateTableRequest(FinanceGainLossQuarterly.class)
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

        then(TableUtils.createTableIfNotExists(amazonDynamoDB, request)).isTrue();
    }


    // AmazonDynamoDB SDK 를 사용하지 않고 DynamodbMapper 를 이용해 테이블을 삭제
    @Order(2)
    @Test
    @DisplayName("FinanceGainLossYearly 테이블 삭제 >> spring-data-dynamodb 의 DynamodbMapper 를 이용해 테이블을 삭제")
    public void TEST_DELETE_TABLE_BY_ORM_MAPPER_DYNAMOMAPPER(){
        DeleteTableRequest deleteTableRequest = dynamoDBMapper.generateDeleteTableRequest(FinanceGainLossQuarterly.class);
        then(TableUtils.deleteTableIfExists(amazonDynamoDB, deleteTableRequest)).isTrue();
    }

}
