package com.greenlearner.ticketbookingservice.dto;

import java.time.LocalDate;

public class PaymentProcessorResponseRequest {
    private final String cardNumber;
    private final LocalDate cardExpiryDate;
    private final Double amount;

    public PaymentProcessorResponseRequest(String cardNumber, LocalDate cardExpiryDate, Double amount) {

        this.cardNumber = cardNumber;
        this.cardExpiryDate = cardExpiryDate;
        this.amount = amount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public LocalDate getCardExpiryDate() {
        return cardExpiryDate;
    }

    public Double getAmount() {
        return amount;
    }
}
