package io.inspect.koreanstockinspector.testutils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

public class LocalDynamoDbTestConfigUtils {
    public record LocalDynamoDbEnvironment(AmazonDynamoDB amazonDynamoDB, DynamoDBMapper dynamoDBMapper){}

    public static LocalDynamoDbEnvironment newLocalDynamoDbEnvironment(){
        AwsClientBuilder.EndpointConfiguration endpointConfig = new AwsClientBuilder.EndpointConfiguration(
                "http://localhost:5555",
                "local"
        );

        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(
                        "type the key",
                        "type the key"
                )
        );

        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withRegion("ap-northeast-2")
                .withCredentials(credentialsProvider)
                .build();

        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB, DynamoDBMapperConfig.DEFAULT);

        return new LocalDynamoDbEnvironment(amazonDynamoDB, dynamoDBMapper);
    }
}
