package com.cydeo.payment.service.impl.selection;

import com.cydeo.payment.model.payment.AuthRequest;
import com.cydeo.payment.model.pos.Pos;
import com.cydeo.payment.service.PosInitializationService;
import com.cydeo.payment.service.PosSelectionService;

import static com.cydeo.payment.constant.StaticConstants.POS_LIST;

public class HybridPosSelectionServiceImpl implements PosSelectionService {

    private final PosInitializationService posInitializationService;

    public HybridPosSelectionServiceImpl(PosInitializationService posInitializationService) {
        this.posInitializationService = posInitializationService;
    }

    @Override
    public Pos decidePaymentPos(AuthRequest authRequest) {
        posInitializationService.initializePosList();

        return POS_LIST.stream().filter(pos -> pos.getPartnerId().equals(authRequest.getPartnerId())).
                filter(pos -> authRequest.getPosName().equals(pos.getName())).findFirst().orElse(POS_LIST.get(0));
    }
}
