package com.cydeo.payment.service.impl.payment;

import com.cydeo.payment.enums.ServiceProvider;
import com.cydeo.payment.exception.InvalidPaymentStrategyException;
import com.cydeo.payment.model.payment.*;
import com.cydeo.payment.model.pos.Pos;
import com.cydeo.payment.model.pos.PosClientRequest;
import com.cydeo.payment.model.pos.PosClientResponse;
import com.cydeo.payment.posclient.AbstractPosClient;
import com.cydeo.payment.posclient.client.BankAPosClient;
import com.cydeo.payment.posclient.client.BankBPosClient;
import com.cydeo.payment.posclient.client.BankCPosClient;
import com.cydeo.payment.service.PaymentService;
import com.cydeo.payment.service.PosSelectionService;
import com.cydeo.payment.service.impl.initialization.HybridPosInitializationServiceImpl;
import com.cydeo.payment.service.impl.initialization.TenantPosInitializationServiceImpl;
import com.cydeo.payment.service.impl.selection.HybridPosSelectionServiceImpl;
import com.cydeo.payment.service.impl.selection.TenantPosSelectionServiceImpl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static com.cydeo.payment.constant.StaticConstants.HYBRID_MERCHANT_PAYMENT_LIST;

public class HybridPaymentServiceImpl implements PaymentService {

    @Override
    public AbstractPaymentResponse auth(AuthRequest authRequest) {
        AbstractPaymentResponse abstractPaymentResponse = new AbstractPaymentResponse();

        PosSelectionService posSelectionService = decidePaymentPosThatWillBeProcessed(authRequest);

        Pos pos = posSelectionService.decidePaymentPos(authRequest);
        String recipient = "Recipient";

        AbstractPosClient abstractPosClient;

        PosClientRequest posClientRequest = new PosClientRequest(authRequest.getAmount(),
                pos.getName(),
                recipient);

        abstractPosClient = decidePosClient(pos.getName());

        UUID orderId = abstractPosClient.generateOrderId();
        posClientRequest.setOrderId(orderId.toString());

        PosClientResponse posClientResponse = abstractPosClient.auth(posClientRequest);

        abstractPaymentResponse.setResult(posClientResponse.getResult());
        abstractPaymentResponse.setErrorCde(posClientResponse.getErrorCde());

        abstractPaymentResponse.setPaymentCostAmount(
                calculateCommissionAmountForHybridMerchant(authRequest.getServiceProvider()));

        if (abstractPaymentResponse.getResult() == 1){
            initHybridMerchantPayment(authRequest, orderId);
        }

        return abstractPaymentResponse;
    }


    @Override
    public AbstractPaymentResponse auth3D(AuthRequest auth3DRequest){
        AbstractPaymentResponse abstractPaymentResponse = new AbstractPaymentResponse();

        PosSelectionService posSelectionService = decidePaymentPosThatWillBeProcessed(auth3DRequest);

        Pos pos = posSelectionService.decidePaymentPos(auth3DRequest);
        String recipient = "Recipient";

        AbstractPosClient abstractPosClient;

        PosClientRequest posClientRequest = new PosClientRequest(auth3DRequest.getAmount(),
                pos.getName(),
                recipient);

        abstractPosClient = decidePosClient(pos.getName());

        UUID orderId = abstractPosClient.generateOrderId();
        posClientRequest.setOrderId(orderId.toString());

        PosClientResponse posClientResponse = abstractPosClient.auth3D(posClientRequest);

        abstractPaymentResponse.setResult(posClientResponse.getResult());
        abstractPaymentResponse.setErrorCde(posClientResponse.getErrorCde());

        abstractPaymentResponse.setPaymentCostAmount(
                calculateCommissionAmountForHybridMerchant(auth3DRequest.getServiceProvider()));

        if (abstractPaymentResponse.getResult() == 1){
            initHybridMerchantPayment(auth3DRequest, orderId);
        }

        return abstractPaymentResponse;
    }

    // cost calculation showing how much will be charged for successful payments from stores
    public BigDecimal calculateCommissionAmountForHybridMerchant(ServiceProvider serviceProvider) {
        switch (serviceProvider){
            case AMEX:
                return BigDecimal.TEN;
            case VISA:
                return new BigDecimal("0.2");
            case MASTER_CARD:
                return new BigDecimal("0.3");
            default:
                return BigDecimal.ZERO;
        }
    }

    // after succesfull payment, payments need to be collected into payment list
    private void initHybridMerchantPayment(AuthRequest authRequest, UUID orderId){
        Payment payment = new Payment(new Date(), authRequest.getAmount(), orderId, authRequest.getAmount());
        HYBRID_MERCHANT_PAYMENT_LIST.add(payment);
    }

    //determines which bank the payments are directed to
    public AbstractPosClient decidePosClient(String posName){
        switch (posName){
            case "BANKA":
                return new BankAPosClient();
            case "BANKB":
                return new BankBPosClient();
            case "BANKC":
                return new BankCPosClient();
            default:
                return null;
        }
    }

    //It is the part where the pos selection algorithms to be run are determined according to the hybrid or tenant payments.
    private PosSelectionService decidePaymentPosThatWillBeProcessed(AuthRequest authRequest) {
        if (authRequest.isHybridPayment()) {
            return new HybridPosSelectionServiceImpl(new HybridPosInitializationServiceImpl());
        } else {
            return new TenantPosSelectionServiceImpl(new TenantPosInitializationServiceImpl());
        }
    }
}
