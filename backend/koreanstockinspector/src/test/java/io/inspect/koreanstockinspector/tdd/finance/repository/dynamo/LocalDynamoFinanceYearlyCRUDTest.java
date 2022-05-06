package io.inspect.koreanstockinspector.tdd.finance.repository.dynamo;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import io.inspect.koreanstockinspector.finance.domain.FinanceGainLossYearly;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

public class LocalDynamoFinanceYearlyCRUDTest {

    AmazonDynamoDB amazonDynamoDB;
    DynamoDBMapper dynamoDBMapper;

    @BeforeEach
    public void testLocalInit(){
        AwsClientBuilder.EndpointConfiguration endpointConfig = new AwsClientBuilder.EndpointConfiguration(
                "http://localhost:5555",
                "ap-northeast-2"
        );

        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(
                        "type the key",
                        "type the key"
                )
        );

        amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withRegion("ap-northeast-2")
                .withCredentials(credentialsProvider)
                .build();

        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB, DynamoDBMapperConfig.DEFAULT);
    }

    @Test
    @DisplayName("AmazonDynamoDB SDK 가 아닌, spring-data-dynamodb 의 DynamodbMapper 를 이용해 테이블을 생성해본다.")
    public void TEST_CRAETE_TABLE_BY_ORM_MAPPER_DYNAMODBMAPPER(){
        CreateTableRequest request = dynamoDBMapper.generateCreateTableRequest(FinanceGainLossYearly.class)
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

        then(TableUtils.createTableIfNotExists(amazonDynamoDB, request)).isTrue();
    }

}
