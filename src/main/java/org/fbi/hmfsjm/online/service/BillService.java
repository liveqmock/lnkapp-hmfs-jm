package org.fbi.hmfsjm.online.service;

import org.fbi.hmfsjm.enums.BillQryStatus;
import org.fbi.hmfsjm.helper.ObjectFieldsCopier;
import org.fbi.hmfsjm.repository.MybatisManager;
import org.fbi.hmfsjm.repository.dao.HmfsJmBillMapper;
import org.fbi.hmfsjm.repository.model.HmfsJmBill;
import org.fbi.hmfsjm.repository.model.HmfsJmBillExample;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * �ɿ����
 */
public class BillService {
    private static final Logger logger = LoggerFactory.getLogger(BillService.class);
    MybatisManager manager = new MybatisManager();

    // �����Ų�ѯ�ɿ
    public HmfsJmBill qryBillByNo(String billNo) {
        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJmBillMapper mapper = session.getMapper(HmfsJmBillMapper.class);
            HmfsJmBillExample example = new HmfsJmBillExample();
            example.createCriteria().andBillnoEqualTo(billNo);
            List<HmfsJmBill> bills = mapper.selectByExample(example);
            return (bills.size() > 0) ? bills.get(0) : null;
        } finally {
            if (session != null) session.close();
        }
    }

    // ����ɿ���Ѵ��������
    public boolean saveDepositBill(HmfsJmBill bill) throws IllegalAccessException {
        if (BillQryStatus.VALAID.getCode().equals(bill.getBillStsCode()))  {
            SqlSession session = null;
            try {
                session = manager.getSessionFactory().openSession();
                HmfsJmBillMapper mapper = session.getMapper(HmfsJmBillMapper.class);
                HmfsJmBillExample example = new HmfsJmBillExample();
                example.createCriteria().andBillnoEqualTo(bill.getBillno());
                List<HmfsJmBill> bills = mapper.selectByExample(example);
                int cnt = 0;
                if (bills.size() > 0) {
                    // �Ѵ��������
                    HmfsJmBill origBill = bills.get(0);
                    String pkid = origBill.getPkid();
                    ObjectFieldsCopier.copyFields(bill, origBill);
                    origBill.setPkid(pkid);
                    cnt = mapper.updateByPrimaryKey(origBill);
                    session.commit();
                    return cnt == 1;
                } else {
                    cnt = mapper.insert(bill);
                    session.commit();
                    return cnt == 1;
                }
            } finally {
                if (session != null) session.close();
            }
        } else {
            throw new RuntimeException("״̬��" + bill.getBillStsCode() + bill.getBillStsTitle());
        }
    }
}
