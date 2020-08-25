package com.jobinbasani.config;

import io.quarkus.arc.config.ConfigProperties;
import lombok.Data;

@Data
@ConfigProperties(prefix = "database")
public class TestDatabaseConfig {
    String dockerImage;
    String table;
    String dockerCommand;
    int containerPort;
    long readCapacityUnits;
    long writeCapacityUnits;
}
