package com.company.accountingsystem.datainit;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

// Service class for initializing the database using SQL scripts
@Service
public class DatabaseInitializationService {

    private final JdbcTemplate jdbcTemplate;

    @Value("classpath:schema/*.sql")
    private Resource[] schemaScripts;

    @Value("classpath:data/*.sql")
    private Resource[] dataScripts;

    // Constructor injection for JdbcTemplate
    public DatabaseInitializationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Method to initialize the database by executing schema and data scripts
    @PostConstruct
    public void initializeDatabase() {
        executeScripts(schemaScripts, "schema");
        executeScripts(dataScripts, "data");
    }

    // Method to execute SQL scripts
    private void executeScripts(Resource[] scripts, String scriptType) {
        Arrays.stream(scripts)
                .forEach(script -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(script.getInputStream()))) {
                        jdbcTemplate.execute(reader.lines().reduce((line1, line2) -> line1 + "\n" + line2).orElse(""));
                        System.out.println("Executed(" + scriptType + "): " + script.getFilename());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
