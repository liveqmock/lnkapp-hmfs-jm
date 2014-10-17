package org.fbi.hmfsjm.online.service;

import org.apache.ibatis.session.SqlSession;
import org.fbi.hmfsjm.enums.ActStatus;
import org.fbi.hmfsjm.gateway.domain.txn.Toa4001;
import org.fbi.hmfsjm.gateway.domain.txn.Toa5003;
import org.fbi.hmfsjm.repository.MybatisManager;
import org.fbi.hmfsjm.repository.dao.HmfsJmActMapper;
import org.fbi.hmfsjm.repository.model.HmfsJmAct;
import org.fbi.hmfsjm.repository.model.HmfsJmActExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 房管发起，销户
 */
public class Txn5003Service {
    private static final Logger logger = LoggerFactory.getLogger(Txn5003Service.class);
    MybatisManager manager = new MybatisManager();

    //
    public Toa5003.Body cancelAct(String houseID, String houseAccount) {
        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJmActMapper mapper = session.getMapper(HmfsJmActMapper.class);
            HmfsJmActExample example = new HmfsJmActExample();

            example.createCriteria().andHouseAccountEqualTo(houseAccount).andHouseIdEqualTo(houseID);
            List<HmfsJmAct> dbActs = mapper.selectByExample(example);
            Toa5003.Body result = new Toa5003.Body();
            if (dbActs.isEmpty()) {
                result.STS_CODE = "0002";            // 不存在
                result.STS_MSG = "分账户不存在";
            } else {
                HmfsJmAct dbact = dbActs.get(0);
                if (ActStatus.CANCEL.getCode().equals(dbact.getActStatus())) {
                    result.STS_CODE = "0000";            // 已销户
                    result.STS_MSG = "销户成功";
                } else {
                    if (dbact.getBalAmt().compareTo(new BigDecimal("0.00")) != 0) {
                        result.STS_CODE = "0001";            // 已销户
                        result.STS_MSG = "分账户资金余额大于0";
                    } else {
                        // 销户
                        result.STS_CODE = "0000";
                        result.STS_MSG = "销户成功";
                        dbact.setActStatus(ActStatus.CANCEL.getCode());
                        mapper.updateByPrimaryKey(dbact);
                    }
                }
            }
            return result;
        } finally {
            if (session != null) session.close();
        }
    }

}
