package com.greenlearner.ticketbookingservice.timeout;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.greenlearner.ticketbookingservice.dto.CardDetails;
import com.greenlearner.ticketbookingservice.dto.TicketBookingPaymentRequest;
import com.greenlearner.ticketbookingservice.dto.TicketBookingResponse;
import com.greenlearner.ticketbookingservice.gateway.PaymentProcessorGateway;
import com.greenlearner.ticketbookingservice.service.TicketBookingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.greenlearner.ticketbookingservice.dto.TicketBookingResponse.BookingResponseStatus.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
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
        stubFor(post(urlPathEqualTo("/payments"))
                .withRequestBody(equalToJson("{\n" +
                        "\"cardNumber\" : \"1111-1111-1111-1111\",\n" +
                        "\"cardExpiryDate\" : \"2020-08-03\",\n" +
                        "\"amount\" : 200.0\n" +
                        "}"))
                .willReturn(okJson("{\n" +
                        "\"paymentId\" : \"3333\",\n" +
                        "\"paymentResponseStatus\" : \"SUCCESS\"\n" +
                        "}").withFixedDelay(1100)));

        //when
        TicketBookingPaymentRequest tbpr = new TicketBookingPaymentRequest("1111",
                200.0,
                new CardDetails("1111-1111-1111-1111", LocalDate.of(2020,8,3)));

        //then
        assertThrows(ResourceAccessException.class,
                () -> tbs.payForBooking(tbpr));


        verify(postRequestedFor(urlPathEqualTo("/payments")).withRequestBody(
                equalToJson("{\n" +
                        "\"cardNumber\" : \"1111-1111-1111-1111\",\n" +
                        "\"cardExpiryDate\" : \"2020-08-03\",\n" +
                        "\"amount\" : 200.0\n" +
                        "}")
        ));
    }


    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

}
