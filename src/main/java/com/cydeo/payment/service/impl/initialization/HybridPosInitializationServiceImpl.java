package com.cydeo.payment.service.impl.initialization;

import com.cydeo.payment.constant.StaticConstants;
import com.cydeo.payment.enums.ServiceProvider;
import com.cydeo.payment.helper.CollectionHelper;
import com.cydeo.payment.model.pos.Pos;
import com.cydeo.payment.service.PosInitializationService;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.cydeo.payment.constant.StaticConstants.*;

public class HybridPosInitializationServiceImpl implements PosInitializationService {

    @Override
    public List<Pos> initializePosList() {
        if(CollectionHelper.isNullOrEmpty(POS_LIST)){
            checkPosListAndInitializeIfNotExists();
        }
        return POS_LIST;
    }

    private void checkPosListAndInitializeIfNotExists() {
        AtomicReference<Double> initialValue = new AtomicReference<>(1.32);
        double incrementValueForPos = 0.53d;

        EXTERNAL_BANK_CODE.forEach(bankCode -> {
            String posName = "BANK" + bankCode;

            Pos pos = new Pos(posName,
                    EXTERNAL_PARTNER_ID,
                    null,
                    decideSupportedServiceProviderList(posName));

            initialValue.updateAndGet(v -> (v + incrementValueForPos));
            POS_LIST.add(pos);
        });
    }

    private List<ServiceProvider> decideSupportedServiceProviderList(String posName) {
        List<ServiceProvider> serviceProviderList = new ArrayList<>();
        switch (posName) {
            case "BANKD":
                return new ArrayList<>(Arrays.asList(ServiceProvider.values()));
            case "BANKE":
                serviceProviderList.add(ServiceProvider.MASTER_CARD);
                serviceProviderList.add(ServiceProvider.VISA);
                return serviceProviderList;
            case "BANKF":
                serviceProviderList.add(ServiceProvider.AMEX);
                return serviceProviderList;
            default:
                return serviceProviderList;
        }
    }
}
