package com.hflw.vasp.admin.modules.common.service;

import com.hflw.vasp.system.dao.ICommonDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liuyf
 * @Title CommonServiceImpl.java
 * @Package com.hflw.vasp.service.impl
 * @Description TODO
 * @date 2019年10月22日 下午3:01:25
 */
@Service
public class CommonService {

    private Logger logger = LoggerFactory.getLogger(CommonService.class);

    @Autowired
    private ICommonDao commonDao;


}
