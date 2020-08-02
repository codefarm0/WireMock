package com.greenlearner.ticketbookingservice.dto;
/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */
public class TicketBookingPaymentRequest {
    private final String bookingId;
    private final Double amount;
    private final CardDetails cardDetails;
    private boolean fraudAlert = false;

    public TicketBookingPaymentRequest(String bookingId, Double amount, CardDetails cardDetails) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.cardDetails = cardDetails;
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