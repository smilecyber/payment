package com.cydeo.payment.controller;

import com.cydeo.payment.enums.CardType;
import com.cydeo.payment.exception.InvalidPaymentStrategyException;
import com.cydeo.payment.model.payment.AbstractPaymentResponse;
import com.cydeo.payment.model.payment.AuthRequest;
import com.cydeo.payment.service.PaymentService;
import com.cydeo.payment.service.impl.payment.HybridPaymentServiceImpl;
import com.cydeo.payment.service.impl.payment.TenantPaymentServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ResourceBundle;

@RequestMapping("/payment")
public class PaymentController {
    private final BigDecimal THREEDS_LIMIT = new BigDecimal(500);

    private final ResourceBundle resourceBundle;

    public PaymentController(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }


    // accept payment requests from any clients
    @PostMapping
    public AbstractPaymentResponse makePayment(@RequestBody AuthRequest authRequest) {
        // payment response success result 1 failure result 0
        // If the payment is unsuccessful, the error message and error code field will also be filled.
        AbstractPaymentResponse abstractPaymentResponse = new AbstractPaymentResponse();
        try {
            PaymentService paymentService = decidePaymentPosThatWillBeProcessed(authRequest);
            if (forceThreeds(authRequest)) {
                abstractPaymentResponse = paymentService.auth3D(authRequest);
            } else {
                abstractPaymentResponse = paymentService.auth(authRequest);
            }
        } catch (InvalidPaymentStrategyException e) {
            e.printStackTrace();
        }
        return abstractPaymentResponse;
    }

    //Determines whether payments are forwarded as 3d secure or non secure
    private boolean forceThreeds(AuthRequest authRequest) {
        if (CardType.DEBIT.equals(authRequest.getCardType())) {
            return true;
        } else {
            return authRequest.getAmount().compareTo(THREEDS_LIMIT) > 0;
        }
    }

    //determines the method by which payments will be advanced (hybrid or tenant)
    private PaymentService decidePaymentPosThatWillBeProcessed(AuthRequest authRequest) {
        if (authRequest.isHybridPayment()) {
            return new HybridPaymentServiceImpl();
        } else {
            return new TenantPaymentServiceImpl(resourceBundle);
        }
    }
}
