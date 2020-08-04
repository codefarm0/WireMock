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
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.greenlearner.ticketbookingservice.dto.TicketBookingResponse.BookingResponseStatus.SUCCESS;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */
public class TicketBookingServiceRequestNondeteminismStubbingTest {

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
                .withRequestBody(
                        matchingJsonPath("cardNumber")
                )
                .withRequestBody(
                        matchingJsonPath("cardExpiryDate")
                )
                .withRequestBody(
                        matchingJsonPath("amount")
                )
                .willReturn(okJson("{" +
                        "  \"paymentResponseStatus\": \"SUCCESS\"" +
                        "}")));

        stubFor(get(urlPathMatching("/fraudCheck/.*"))
                .willReturn(okJson("{" +
                        "  \"blacklisted\": \"false\"" +
                        "}")));

        final List<TicketBookingPaymentRequest> batch = IntStream.range(0, 10)
                .mapToObj(this::generateBookingPayment)
                .collect(toList());
        // When
        List<TicketBookingResponse> responses = tbs.batchPayment(batch);

        // Then
        assertThat(responses).hasSize(batch.size());
        responses.stream()
                .forEach(bookingResponse ->
                        assertThat(bookingResponse.getBookingResponseStatus()).isEqualTo(SUCCESS)
                );

        //verify
        verify(5,getRequestedFor(urlPathMatching("/fraudCheck/.*")));
        verify(10,postRequestedFor(urlPathEqualTo("/payments")));

    }

    private TicketBookingPaymentRequest generateBookingPayment(int i) {
        final CardDetails creditCard = new CardDetails(
                cardNumbers(),
                LocalDate.of(i, 1, 1)
        );
        TicketBookingPaymentRequest bookingPayment = new TicketBookingPaymentRequest(Integer.toString(i), Double.valueOf(i), creditCard);
        if (i % 2 == 0) {
            bookingPayment.setFraudAlert(true);
        }
        return bookingPayment;
    }

    private String cardNumbers() {
        Random random = new Random();
        return randomNumber(random) +
                "-" +
                randomNumber(random) +
                "-" +
                randomNumber(random) +
                "-" +
                randomNumber(random);
    }

    private String randomNumber(Random random) {
        return IntStream.range(0, 4).mapToObj(i -> Integer.toString(random.nextInt(9) + 1)).reduce((l, r) -> l + r).get();
    }


    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

}
