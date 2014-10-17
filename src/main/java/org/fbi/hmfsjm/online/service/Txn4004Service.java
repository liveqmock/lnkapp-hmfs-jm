package org.fbi.hmfsjm.online.service;

import org.apache.ibatis.session.SqlSession;
import org.fbi.hmfsjm.enums.ActStatus;
import org.fbi.hmfsjm.enums.BillBookType;
import org.fbi.hmfsjm.gateway.domain.txn.Toa4004;
import org.fbi.hmfsjm.repository.MybatisManager;
import org.fbi.hmfsjm.repository.dao.HmfsJmActMapper;
import org.fbi.hmfsjm.repository.dao.HmfsJmActTxnMapper;
import org.fbi.hmfsjm.repository.model.HmfsJmAct;
import org.fbi.hmfsjm.repository.model.HmfsJmActExample;
import org.fbi.hmfsjm.repository.model.HmfsJmActTxn;
import org.fbi.hmfsjm.repository.model.HmfsJmActTxnExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 分户记账流水查询
 */
public class Txn4004Service {
    private static final Logger logger = LoggerFactory.getLogger(Txn4004Service.class);
    MybatisManager manager = new MybatisManager();

    //
    public List<Toa4004.Detail> qryActTxns(List<String> actNos) {
        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJmActTxnMapper mapper = session.getMapper(HmfsJmActTxnMapper.class);
            List<Toa4004.Detail> records = new ArrayList<>();
            HmfsJmActTxnExample example = new HmfsJmActTxnExample();
            for (String act : actNos) {
                example.clear();
                example.createCriteria().andHouseAccountEqualTo(act);
                List<HmfsJmActTxn> dbActtxns = mapper.selectByExample(example);
                for (HmfsJmActTxn txn : dbActtxns) {
                    Toa4004.Detail record = new Toa4004.Detail();
                    record.HOUSE_ACCOUNT = act;
                    record.BILL_NO = txn.getBillno();
                    record.BOOK_FLAG = BillBookType.valueOfAlias(txn.getBookType()).typeToQryDetail();
                    record.TXN_MONEY = txn.getTxnAmt().toString();
                    record.TXN_TIME = txn.getOperDate();
                    records.add(record);
                }
            }
            return records;
        } finally {
            if (session != null) session.close();
        }
    }
}
