package com.greenlearner.ticketbookingservice.controller;

import com.greenlearner.ticketbookingservice.dto.TicketBookingPaymentRequest;
import com.greenlearner.ticketbookingservice.dto.PaymentUpdateResponse;
import com.greenlearner.ticketbookingservice.dto.TicketBookingResponse;
import com.greenlearner.ticketbookingservice.service.TicketBookingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */

@RestController
public class TicketBookingController {

    private TicketBookingService ticketBookingService;

    public TicketBookingController(TicketBookingService ticketBookingService) {
        this.ticketBookingService = ticketBookingService;
    }

    @PostMapping("/bookTicket")
    TicketBookingResponse payForTicket( @RequestBody TicketBookingPaymentRequest ticketBookingPaymentRequest){
        return ticketBookingService.payForBooking(ticketBookingPaymentRequest);
    }

    @PostMapping("/updatePayment")
    PaymentUpdateResponse updatePaymentDetails(@RequestBody TicketBookingPaymentRequest ticketBookingPaymentRequest){
        return ticketBookingService.updatePaymentDetails(ticketBookingPaymentRequest);
    }

    @PostMapping("/batchPayment")
    List<TicketBookingResponse> batchPayment(List<TicketBookingPaymentRequest> bookingPayment){
        return ticketBookingService.batchPayment(bookingPayment);
    }
}
