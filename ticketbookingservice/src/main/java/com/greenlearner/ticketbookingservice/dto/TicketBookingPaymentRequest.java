package com.greenlearner.ticketbookingservice.dto;
/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */
public class TicketBookingPaymentRequest {
    private  String bookingId;
    private  Double amount;
    private  CardDetails cardDetails;
    private boolean fraudAlert = false;

    public TicketBookingPaymentRequest(String bookingId, Double amount, CardDetails cardDetails) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.cardDetails = cardDetails;
    }

    public TicketBookingPaymentRequest() {
    }

    public String getBookingId() {
        return bookingId;
    }

    public Double getAmount() {
        return amount;
    }

    public CardDetails getCardDetails() {
        return cardDetails;
    }

    public boolean isFraudAlert() {
        return fraudAlert;
    }

    public void setFraudAlert(boolean fraudAlert) {
        this.fraudAlert = fraudAlert;
    }
}