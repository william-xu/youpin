package com.hflw.vasp.eshop.modules.trading.service;

import com.hflw.vasp.modules.dao.ITradingFlowDao;
import com.hflw.vasp.modules.entity.TradingFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 交易服务
 */
@Service
public class TradingService {

    @Autowired
    private ITradingFlowDao tradingFlowDao;

    public void save(TradingFlow tradingFlow) {
        tradingFlowDao.save(tradingFlow);
    }

    public TradingFlow findByFlowNo(String flowNo) {
        return tradingFlowDao.findByFlowNo(flowNo);
    }

}
