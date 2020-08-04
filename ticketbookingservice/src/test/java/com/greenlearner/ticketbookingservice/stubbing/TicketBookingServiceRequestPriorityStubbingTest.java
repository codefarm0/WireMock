package com.greenlearner.ticketbookingservice.stubbing;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.greenlearner.ticketbookingservice.dto.CardDetails;
import com.greenlearner.ticketbookingservice.dto.TicketBookingPaymentRequest;
import com.greenlearner.ticketbookingservice.dto.TicketBookingResponse;
import com.greenlearner.ticketbookingservice.gateway.PaymentProcessorGateway;
import com.greenlearner.ticketbookingservice.service.TicketBookingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.greenlearner.ticketbookingservice.dto.TicketBookingResponse.BookingResponseStatus.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */
public class TicketBookingServiceRequestPriorityStubbingTest {

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

        // Given
        stubFor(post(urlPathEqualTo("/payments"))
                .withRequestBody(equalToJson("{" +
                        "  \"cardNumber\": \"1234-1234-1234-1234\"," +
                        "  \"cardExpiryDate\": \"2020-08-01\"," +
                        "  \"amount\": 2000.55" +
                        "}"))
                .willReturn(okJson("{" +
                        "  \"paymentId\": \"2222\"," +
                        "  \"paymentResponseStatus\": \"SUCCESS\"" +
                        "}")));

        stubFor(get(urlPathEqualTo("/fraudCheck/1234-1234-1234-1234"))
                .atPriority(1)
                .willReturn(okJson("{" +
                        "  \"blacklisted\": \"false\"" +
                        "}")));

        stubFor(get(urlPathEqualTo("/fraudCheck/1234-1234-1234-1234"))
                .atPriority(2)
                .willReturn(okJson("{" +
                        "  \"blacklisted\": \"true\"" +
                        "}")));



        // When
        TicketBookingPaymentRequest bookingPayment = new TicketBookingPaymentRequest(
                "1111",
                Double.valueOf(2000.55),
                new CardDetails("1234-1234-1234-1234",
                        LocalDate.of(2020, 8, 1)));
        bookingPayment.setFraudAlert(true);

        final TicketBookingResponse bookingResponse = tbs.payForBooking(bookingPayment);

        // Then
        assertThat(bookingResponse).isEqualTo(new TicketBookingResponse("1111", "2222", SUCCESS));

    }


    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

}
