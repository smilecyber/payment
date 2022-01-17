package com.cydeo.payment.posclient.client;

import com.cydeo.payment.model.pos.PosClientRequest;
import com.cydeo.payment.model.pos.PosClientResponse;
import com.cydeo.payment.posclient.AbstractPosClient;

public class BankBPosClient extends AbstractPosClient {

    // return dummy response
    @Override
    public PosClientResponse auth(PosClientRequest posClientRequest) {
        return new PosClientResponse(1, null, posClientRequest.getRequestedAmount());
    }

    // return dummy response
    @Override
    public PosClientResponse auth3D(PosClientRequest posClientRequest) {
        return new PosClientResponse(0, "1345", posClientRequest.getRequestedAmount());
    }
}
