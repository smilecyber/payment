package com.cydeo.payment.model.payment;

import com.cydeo.payment.enums.Localization;

import java.math.BigDecimal;
import java.util.Arrays;

public class AbstractPaymentRequest {
    private static final Localization DEFAULT_LOCALE = Localization.ENG;

    protected BigDecimal amount;
    protected Localization locale;
    protected String conversationId;

    public AbstractPaymentRequest(BigDecimal amount, Localization locale, String conversationId) {
        this.amount = amount;
        this.locale = locale;
        this.conversationId = conversationId;
    }

    public Localization getLocale() {
        return Arrays.stream(Localization.values()).findAny().orElse(DEFAULT_LOCALE);
    }

    public void setLocale(Localization locale) {
        this.locale = locale;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
