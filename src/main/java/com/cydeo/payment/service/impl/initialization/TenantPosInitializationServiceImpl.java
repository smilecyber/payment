package com.cydeo.payment.service.impl.initialization;

import com.cydeo.payment.enums.ServiceProvider;
import com.cydeo.payment.helper.CollectionHelper;
import com.cydeo.payment.model.pos.Pos;
import com.cydeo.payment.service.PosInitializationService;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.cydeo.payment.constant.StaticConstants.*;

public class TenantPosInitializationServiceImpl implements PosInitializationService {

    @Override
    public List<Pos> initializePosList() {
        if (CollectionHelper.isNullOrEmpty(POS_LIST)) {
            checkPosListAndInitializeIfNotExists();
        }
        return POS_LIST;
    }

    private void checkPosListAndInitializeIfNotExists() {
        double incrementValueForPos = 0.53d , initialValue = 1.32;

        for (Character bankCode : BANK_CODE){
            String posName = "BANK" + bankCode;
            Pos pos = new Pos(posName,
                    CYDEO_PARTNER_ID,
                    initializePosCommissionRateMap(initialValue),
                    decideSupportedServiceProviderList(posName));
            initialValue += incrementValueForPos;
            POS_LIST.add(pos);
        }
    }

    private Map<Integer, Double> initializePosCommissionRateMap(double initialValue) {

        double incrementValue = 0.3d;
        Map<Integer, Double> commissionMap = new HashMap<>();

        for (Integer installment : AVAILABLE_INSTALLMENT) {
            commissionMap.put(installment, initialValue);
            initialValue += incrementValue;
        }

        return commissionMap;
    }

    private List<ServiceProvider> decideSupportedServiceProviderList(String posName) {
        List<ServiceProvider> serviceProviderList = new ArrayList<>();
        switch (posName) {
            case "BANKA":
                return new ArrayList<>(Arrays.asList(ServiceProvider.values()));
            case "BANKB":
                serviceProviderList.add(ServiceProvider.MASTER_CARD);
                serviceProviderList.add(ServiceProvider.VISA);
                return serviceProviderList;
            case "BANKC":
                serviceProviderList.add(ServiceProvider.AMEX);
                return serviceProviderList;
            default:
                return serviceProviderList;
        }
    }
}
