package com.monopatin.monopatinservice.Config;
import com.mongodb.MongoClientURI;
import com.mongodb.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/*@Configuration
@EnableMongoRepositories(basePackages = "com.monopatin.monopatinservice.Repository")*/
public class MongoConfig /*extends AbstractMongoClientConfiguration*/ {

   /* @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Override
    protected String getDatabaseName() {
        return "microservicio_monopatin";
    }


    @Override
    public MongoClient mongoClient() {
        MongoClientURI uri = new MongoClientURI(mongoUri);
        return MongoClients.create(String.valueOf(uri));
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    } */
}