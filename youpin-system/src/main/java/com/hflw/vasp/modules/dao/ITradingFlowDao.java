package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.TradingFlow;
import com.hflw.vasp.repository.BaseRepository;

import java.io.Serializable;

public interface ITradingFlowDao extends BaseRepository<TradingFlow, Long>, Serializable {

    TradingFlow findByFlowNo(String flowNo);

}
