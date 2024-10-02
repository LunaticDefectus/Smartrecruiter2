package com.defectus.smartrecruiter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Add custom message converters
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new MappingJackson2HttpMessageConverter());  // Default JSON converter

        // Add custom converter for "application/x-ndjson"
        MappingJackson2HttpMessageConverter ndjsonConverter = new MappingJackson2HttpMessageConverter();
        ndjsonConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.parseMediaType("application/x-ndjson")));
        messageConverters.add(ndjsonConverter);

        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }
}