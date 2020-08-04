package com.greenlearner.ticketbookingservice.gateway;


import com.greenlearner.ticketbookingservice.dto.PaymentProcessorResponse;
import com.greenlearner.ticketbookingservice.dto.PaymentProcessorResponseRequest;
import com.greenlearner.ticketbookingservice.dto.FraudCheckResponse;

import java.time.LocalDate;

public class PaymentProcessorGateway {

    private final PaymentProcessorRestTemplate restTemplate = new PaymentProcessorRestTemplate();
    private final String baseUrl;

    public PaymentProcessorGateway(final String host, final int port) {
        this.baseUrl = "http://" + host + ":" + port;
    }

    public PaymentProcessorResponse makePayment(String creditCardNumber, LocalDate creditCardExpiry, Double amount) {
            final PaymentProcessorResponseRequest request = new PaymentProcessorResponseRequest(creditCardNumber, creditCardExpiry, amount);
        return restTemplate.postForObject(baseUrl + "/payments", request, PaymentProcessorResponse.class);
    }

    public void updatePayment(String bookingId) {
        restTemplate.postForObject(baseUrl + "/update", bookingId, Void.class);
    }

    public FraudCheckResponse fraudCheck(String cardNumber) {
        return restTemplate.getForObject(baseUrl + "/fraudCheck/"+cardNumber, FraudCheckResponse.class);
    }
}
