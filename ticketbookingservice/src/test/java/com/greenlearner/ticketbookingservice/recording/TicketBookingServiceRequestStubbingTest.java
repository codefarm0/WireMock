package com.greenlearner.ticketbookingservice.recording;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.recording.SnapshotRecordResult;
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
public class TicketBookingServiceRequestStubbingTest {

    private TicketBookingService tbs;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

//        wireMockServer.startRecording("http://localhost:8082");

        PaymentProcessorGateway paymentProcessorGateway = new PaymentProcessorGateway("localhost", wireMockServer.port());

        tbs = new TicketBookingService(paymentProcessorGateway);
    }

    @Test
    void testCase1() {

        //when
        TicketBookingPaymentRequest tbpr = new TicketBookingPaymentRequest("1111",
                200.0,
                new CardDetails("1111-1111-1111-1111", LocalDate.of(2020,8,3)));

        TicketBookingResponse response = tbs.payForBooking(tbpr);

        //then
        assertThat(response).isEqualTo(new TicketBookingResponse("1111","99868a1f-b15a-4906-bb4f-b944152693da", SUCCESS));


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
//        wireMockServer.stopRecording();
        wireMockServer.stop();
    }

}
