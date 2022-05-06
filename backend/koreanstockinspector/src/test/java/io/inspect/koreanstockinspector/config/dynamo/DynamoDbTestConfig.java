package io.inspect.koreanstockinspector.config.dynamo;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import lombok.extern.slf4j.Slf4j;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Slf4j
@Profile({"test-docker", "test-in-memory"})
@Configuration
@EnableDynamoDBRepositories(basePackages = "io.inspect.koreanstockinspector.**.domain.dynamo")
public class DynamoDbTestConfig {

    @Profile({"test-docker", "test-in-memory"})
    @Bean
    @Primary
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB dynamoDB){
        return new DynamoDBMapper(dynamoDB, DynamoDBMapperConfig.DEFAULT);
    }

    @Profile({"test-docker"})
    @Bean
    public AmazonDynamoDB amazonDynamoDB(){
        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(
                        "AKIATF2F735OJAS37M6K", "clJ5HpU+4jomg6hc4uT5WtbuE0RsroxmN8wYn8as"
                )
        );

        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                "http://localhost:5555", Regions.AP_NORTHEAST_2.getName()
        );

        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withEndpointConfiguration(endpointConfiguration)
//                .withRegion()
                .build();

        return amazonDynamoDB;
    }

    @Profile("test-in-memory")
    @Bean
    public AmazonDynamoDB inMemoryAmazonDB(){
        return null; // will be implemented
    }
}
