package com.cydeo.payment.model.pos;

import com.cydeo.payment.enums.ServiceProvider;

import java.util.List;
import java.util.Map;

public class Pos {
    private String name;
    private Long partnerId;
    private List<ServiceProvider> supportedServiceProviderList;
    private Map<Integer, Double> installmentCommissionMap;

    public Pos(String name, Long partnerId, Map<Integer, Double> installmentCommissionMap, List<ServiceProvider> supportedServiceProviderList) {
        this.name = name;
        this.partnerId = partnerId;
        this.installmentCommissionMap = installmentCommissionMap;
        this.supportedServiceProviderList = supportedServiceProviderList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public Map<Integer, Double> getInstallmentCommissionMap() {
        return installmentCommissionMap;
    }

    public void setInstallmentCommissionMap(Map<Integer, Double> installmentCommissionMap) {
        this.installmentCommissionMap = installmentCommissionMap;
    }

    public List<ServiceProvider> getSupportedServiceProviderList() {
        return supportedServiceProviderList;
    }

    public void setSupportedServiceProviderList(List<ServiceProvider> supportedServiceProviderList) {
        this.supportedServiceProviderList = supportedServiceProviderList;
    }
}
