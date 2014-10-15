package org.fbi.hmfsjm.online.service;

import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjm.enums.RefundQryStatus;
import org.fbi.hmfsjm.helper.ObjectFieldsCopier;
import org.fbi.hmfsjm.repository.MybatisManager;
import org.fbi.hmfsjm.repository.dao.HmfsJmRefundMapper;
import org.fbi.hmfsjm.repository.model.HmfsJmRefund;
import org.fbi.hmfsjm.repository.model.HmfsJmRefundExample;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * �˿����
 */
public class RefundService {
    private static final Logger logger = LoggerFactory.getLogger(RefundService.class);
    MybatisManager manager = new MybatisManager();

    // �����Ų�ѯ�˿
    public HmfsJmRefund qryRefundByNo(String refundNo) {
        SqlSession session = null;

        try {
            session = manager.getSessionFactory().openSession();

            HmfsJmRefundMapper mapper = session.getMapper(HmfsJmRefundMapper.class);
            HmfsJmRefundExample example = new HmfsJmRefundExample();
            example.createCriteria().andBillnoEqualTo(refundNo);
            List<HmfsJmRefund> refunds = mapper.selectByExample(example);
            return (refunds.size() > 0) ? refunds.get(0) : null;
        } finally {
            if (session != null) session.close();
        }
    }

    // ���棬�Ѵ��������
    public boolean saveRefundBill(HmfsJmRefund refund) throws IllegalAccessException {
        if (RefundQryStatus.VALAID.getCode().equals(refund.getBillStsCode())) {
            SqlSession session = null;
            try {
                session = manager.getSessionFactory().openSession();
                HmfsJmRefundMapper mapper = session.getMapper(HmfsJmRefundMapper.class);
                HmfsJmRefundExample example = new HmfsJmRefundExample();
                example.createCriteria().andBillnoEqualTo(refund.getBillno());
                List<HmfsJmRefund> refunds = mapper.selectByExample(example);
                int cnt = 0;
                if (refunds.size() > 0) {
                    // �Ѵ��������
                    HmfsJmRefund origRefund = refunds.get(0);
                    String pkid = origRefund.getPkid();
                    ObjectFieldsCopier.copyFields(refund, origRefund);
                    origRefund.setPkid(pkid);
                    cnt = mapper.updateByPrimaryKey(origRefund);
                    session.commit();
                    return cnt == 1;
                } else {
                    cnt = mapper.insert(refund);
                    session.commit();
                    return cnt == 1;
                }
            } catch (Exception e) {
                session.rollback();
                String errmsg = e.getMessage();
                if (StringUtils.isEmpty(errmsg)) {
                    throw new RuntimeException("֧ȡ������ʧ��");
                } else
                    throw new RuntimeException(errmsg);
            } finally {
                if (session != null) session.close();
            }
        } else {
            throw new RuntimeException("״̬��" + refund.getBillStsCode() + refund.getBillStsTitle());
        }
    }
}
