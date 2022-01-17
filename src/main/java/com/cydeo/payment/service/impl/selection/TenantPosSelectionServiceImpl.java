package com.cydeo.payment.service.impl.selection;

import com.cydeo.payment.constant.StaticConstants;
import com.cydeo.payment.model.payment.AuthRequest;
import com.cydeo.payment.model.pos.Pos;
import com.cydeo.payment.service.PosInitializationService;
import com.cydeo.payment.service.PosSelectionService;

import java.util.List;
import java.util.stream.Collectors;

public class TenantPosSelectionServiceImpl implements PosSelectionService {

    private final PosInitializationService posInitializationService;

    public TenantPosSelectionServiceImpl(PosInitializationService posInitializationService) {
        this.posInitializationService = posInitializationService;
    }

    @Override
    public Pos decidePaymentPos(AuthRequest authRequest) {

        posInitializationService.initializePosList();

        List<Pos> avaliablePosList = StaticConstants.POS_LIST.stream().filter(pos -> pos.getPartnerId().equals(StaticConstants.CYDEO_PARTNER_ID))
        .filter(pos -> pos.getSupportedServiceProviderList().contains(authRequest.getServiceProvider())).collect(Collectors.toList());

        return decideSelectedPos(avaliablePosList, authRequest);
    }

    //add comment
    public Pos decideSelectedPos(List<Pos> posList, AuthRequest authRequest){
        Pos selectedPos = posList.get(0);
        double firstIndexCommission = selectedPos.getInstallmentCommissionMap().get(authRequest.getInstallment()) ;
        for (Pos pos : posList){
            if (!pos.getName().equals(selectedPos.getName())){
                double posCommissionAmount = pos.getInstallmentCommissionMap().get(authRequest.getInstallment());
                if (posCommissionAmount < firstIndexCommission){
                    selectedPos = pos;
                }
            }
        }
        return selectedPos;
    }
}
