package com.greenlearner.ticketbookingservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.time.LocalDate;

public class CardDetails {
    private  String number;
    private  LocalDate expiry;
    //more details goes here..

    public CardDetails(String number, LocalDate expiry) {
        this.number = number;
        this.expiry = expiry;
    }

    public CardDetails() {
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getExpiry() {
        return expiry;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setExpiry(LocalDate expiry) {
        this.expiry = expiry;
    }
}
