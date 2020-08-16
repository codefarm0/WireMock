package com.greenlearner.paymoney.controller;

import com.greenlearner.paymoney.dto.FraudCheck;
import com.greenlearner.paymoney.dto.PaymentProcessorResponse;
import com.greenlearner.paymoney.dto.PaymentProcessorResponseRequest;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */
@RestController
public class PayController {

    @PostMapping("/update")
    void updatePayment(@RequestBody String bookingId) {
        System.out.println("payment update for bookingId - " + bookingId);
    }

    @PostMapping("/payments")
    PaymentProcessorResponse makePayment(@RequestBody PaymentProcessorResponseRequest request) {
        PaymentProcessorResponse response;
        if (request.getCardNumber().startsWith("1111")) {
            response = new PaymentProcessorResponse(UUID.randomUUID().toString(),
                    PaymentProcessorResponse.PaymentResponseStatus.SUCCESS);
        } else {
            response = new PaymentProcessorResponse(UUID.randomUUID().toString(),
                    PaymentProcessorResponse.PaymentResponseStatus.FAILED);
        }
        System.out.println("payment status - " + response);
        return response;
    }

//    @GetMapping("/fraudCheck/{cardNumber}")
    public FraudCheck fraudCheck(@PathVariable String cardNumber) {

        FraudCheck fraudCheck;
        if (cardNumber.startsWith("1111")) {
            fraudCheck = new FraudCheck(false);
        } else {
            fraudCheck = new FraudCheck(true);
        }
        System.out.println("Fraud status - " + fraudCheck);
        return fraudCheck;
    }


}
