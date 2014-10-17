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
 * ���ܷ�������
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
                result.STS_CODE = "0002";            // ������
                result.STS_MSG = "���˻�������";
            } else {
                HmfsJmAct dbact = dbActs.get(0);
                if (ActStatus.CANCEL.getCode().equals(dbact.getActStatus())) {
                    result.STS_CODE = "0000";            // ������
                    result.STS_MSG = "�����ɹ�";
                } else {
                    if (dbact.getBalAmt().compareTo(new BigDecimal("0.00")) != 0) {
                        result.STS_CODE = "0001";            // ������
                        result.STS_MSG = "���˻��ʽ�������0";
                    } else {
                        // ����
                        result.STS_CODE = "0000";
                        result.STS_MSG = "�����ɹ�";
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
