package com.cydeo.payment.service;

import com.cydeo.payment.exception.InvalidPaymentStrategyException;
import com.cydeo.payment.model.payment.*;

public interface PaymentService {
    AbstractPaymentResponse auth(AuthRequest authRequest) throws InvalidPaymentStrategyException;

    AbstractPaymentResponse auth3D(AuthRequest auth3DRequest) throws InvalidPaymentStrategyException;

}
