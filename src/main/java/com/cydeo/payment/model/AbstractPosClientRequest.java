package com.cydeo.payment.model;

import java.math.BigDecimal;

public class AbstractPosClientRequest{
    private final BigDecimal AMOUNT_CONVERSATION_VARIABLE = new BigDecimal(100);

    protected BigDecimal requestedAmount;
    protected String posClientClassName;
    protected String recipientName;
    protected String orderId;

    public AbstractPosClientRequest(BigDecimal requestedAmount, String posClientClassName, String recipientName) {
        this.requestedAmount = requestedAmount;
        this.posClientClassName = posClientClassName;
        this.recipientName = recipientName;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount.multiply(AMOUNT_CONVERSATION_VARIABLE);
    }

    public String getPosClientClassName() {
        return posClientClassName + "PosClient";
    }

    public void setPosClientClassName(String posClientClassName) {
        this.posClientClassName = posClientClassName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
