package com.company.accountingsystem.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.media.Schema;

import java.util.Map;
import java.util.TreeMap;

import static com.company.accountingsystem.swagger.Description.DESCRIPTION_TEXT;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Accounting System API",
                version = "1.0.0",
                description = DESCRIPTION_TEXT
        )
)
public class ApiDocumentationConfig {
    // Sorting schemas alphabetically in UI
    @Bean
    public OpenApiCustomizer sortSchemasAlphabetically() {
        return openApi -> {
            Map<String, Schema> schemas = openApi.getComponents().getSchemas();
            openApi.getComponents().setSchemas(new TreeMap<>(schemas));
        };
    }
}
