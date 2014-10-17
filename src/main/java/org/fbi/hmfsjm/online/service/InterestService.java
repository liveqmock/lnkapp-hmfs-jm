package org.fbi.hmfsjm.online.service;

import org.apache.ibatis.session.SqlSession;
import org.fbi.hmfsjm.enums.ActStatus;
import org.fbi.hmfsjm.repository.MybatisManager;
import org.fbi.hmfsjm.repository.dao.CommonMapper;
import org.fbi.hmfsjm.repository.dao.HmfsJmActMapper;
import org.fbi.hmfsjm.repository.dao.HmfsJmInterestMapper;
import org.fbi.hmfsjm.repository.model.HmfsJmAct;
import org.fbi.hmfsjm.repository.model.HmfsJmActExample;
import org.fbi.hmfsjm.repository.model.HmfsJmInterest;
import org.fbi.hmfsjm.repository.model.HmfsJmInterestExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 利息
 */
public class InterestService {
    private static final Logger logger = LoggerFactory.getLogger(InterestService.class);
    MybatisManager manager = new MybatisManager();

    // 计息流水号
    public String getSerialNo (String houseAct) {
        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            CommonMapper mapper = session.getMapper(CommonMapper.class);
            String serialNo = mapper.qryMaxSerialNo(houseAct);
            return serialNo;
        } finally {
            if (session != null) session.close();
        }
    }

    public void accrualInterest() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String today = sdf.format(new Date());
        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJmActMapper mapper = session.getMapper(HmfsJmActMapper.class);
            HmfsJmActExample example = new HmfsJmActExample();
            example.createCriteria().andIntDateLike(today.substring(4)).andActStatusEqualTo(ActStatus.VALID.getCode());
            List<HmfsJmAct> acts = mapper.selectByExample(example);
            // TODO 
        } finally {
            if (session != null) session.close();
        }
    }

    // 发送计息明细(定时每天发送未发送成功的计息记录)
    public void sendInterestTxns() {

    }

}
