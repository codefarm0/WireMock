package com.greenlearner.ticketbookingservice.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static java.util.Arrays.asList;

public class PaymentProcessorRestTemplate extends RestTemplate {

    public PaymentProcessorRestTemplate() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        setMessageConverters(asList(new MappingJackson2HttpMessageConverter(objectMapper)));

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        factory.setConnectTimeout(1000);
        factory.setReadTimeout(1000);

        setRequestFactory(factory);
    }
}
