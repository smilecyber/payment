package com.cydeo.payment.service;

import com.cydeo.payment.model.payment.AbstractPaymentRequest;
import com.cydeo.payment.model.payment.AuthRequest;
import com.cydeo.payment.model.pos.Pos;

public interface PosSelectionService {
    Pos decidePaymentPos(AuthRequest authRequest);
}
