package com.greenlearner.ticketbookingservice.error;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.Fault;
import com.greenlearner.ticketbookingservice.dto.CardDetails;
import com.greenlearner.ticketbookingservice.dto.TicketBookingPaymentRequest;
import com.greenlearner.ticketbookingservice.gateway.PaymentProcessorGateway;
import com.greenlearner.ticketbookingservice.service.TicketBookingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */
public class TicketBookingServiceRequestStubbingTest {

    private TicketBookingService tbs;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer();
        configureFor("localhost", 8080);
        wireMockServer.start();

        PaymentProcessorGateway paymentProcessorGateway = new PaymentProcessorGateway("localhost", wireMockServer.port());

        tbs = new TicketBookingService(paymentProcessorGateway);
    }

    @Test
    void testCase1() {
        //given
        stubFor(post(urlPathEqualTo("/payments")).willReturn(serverError()));

        //when
        TicketBookingPaymentRequest tbpr = new TicketBookingPaymentRequest("1111",
                200.0,
                new CardDetails("1111-1111-1111-1111", LocalDate.of(2020, 8, 3)));

        //then
        assertThrows(HttpServerErrorException.InternalServerError.class,
                () -> tbs.payForBooking(tbpr));
    }

    @Test
    void testCase2() {
        //given
        stubFor(post(urlPathEqualTo("/payments")).willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK)));

        //when
        TicketBookingPaymentRequest tbpr = new TicketBookingPaymentRequest("1111",
                200.0,
                new CardDetails("1111-1111-1111-1111", LocalDate.of(2020, 8, 3)));

        //then
        assertThrows(ResourceAccessException.class,
                () -> tbs.payForBooking(tbpr));
    }

    @Test
    void testCase3() {
        //given
        stubFor(post(urlPathEqualTo("/payments")).willReturn(aResponse().withFault(Fault.RANDOM_DATA_THEN_CLOSE)));

        //when
        TicketBookingPaymentRequest tbpr = new TicketBookingPaymentRequest("1111",
                200.0,
                new CardDetails("1111-1111-1111-1111", LocalDate.of(2020, 8, 3)));

        //then
        assertThrows(ResourceAccessException.class,
                () -> tbs.payForBooking(tbpr));
    }
    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

}
