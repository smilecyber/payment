package com.cydeo.payment.service;

import com.cydeo.payment.model.pos.Pos;

import java.util.List;

public interface PosInitializationService {
    List<Pos> initializePosList();
}
