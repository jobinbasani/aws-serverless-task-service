package com.jobinbasani.config;

import io.quarkus.arc.config.ConfigProperties;
import lombok.Data;

@Data
@ConfigProperties(prefix = "database")
public class DatabaseConfig {
    private String table;
}
