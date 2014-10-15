package org.fbi.hmfsjm.online.service;

import org.fbi.hmfsjm.enums.VoucherStatus;
import org.fbi.hmfsjm.helper.StringHelper;
import org.fbi.hmfsjm.repository.MybatisManager;
import org.fbi.hmfsjm.repository.dao.CommonMapper;
import org.fbi.hmfsjm.repository.model.VoucherBill;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 1500632 票据使用情况查询
 */
public class Txn1500632Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn1500632Service.class);
    MybatisManager manager = new MybatisManager();

    public String process(String date8, String billNo) {

        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            CommonMapper mapper = session.getMapper(CommonMapper.class);
            String useCnt = mapper.qryVchCnt(date8, VoucherStatus.USED.getCode());
            String delCnt = mapper.qryVchCnt(date8, VoucherStatus.CANCEL.getCode());
            if (StringUtils.isEmpty(billNo)) {
                List<VoucherBill> vchList = mapper.qryVoucherBills(date8);
                String vchs = transVchsToStr(vchList);
                if (StringUtils.isEmpty(vchs)) {
                    return vchs;
                } else
                    return useCnt + "|" + delCnt + "|" + vchs;
            } else {
                List<VoucherBill> vchList = mapper.qryVoucher(billNo);
                if (vchList == null || vchList.isEmpty()) {
                    return null;
                } else {
                    return useCnt + "|" + delCnt + "|" + transVchsToStr(vchList);
                }
            }
        } finally {
            if (session != null) session.close();
        }
    }

    private String transVchsToStr(List<VoucherBill> vchList) {
        if (vchList == null || vchList.isEmpty()) {
            return null;
        } else {
            StringBuilder vchBuilder = new StringBuilder();
            vchBuilder.append(vchList.size());
            logger.info("查询到票据笔数：" + vchList.size());
            vchBuilder.append("|");
            for (VoucherBill vb : vchList) {
                // 单号，金额， 票据号， 状态
                if (StringUtils.isEmpty(vb.getBillno())) {
                    vchBuilder.append(StringHelper.rightPad4ChineseToByteLength("", 24, " "));
                } else {
                    vchBuilder.append(StringHelper.rightPad4ChineseToByteLength(vb.getBillno(), 24, " "));
                }
                if (StringUtils.isEmpty(vb.getTxnamt())) {
                    vchBuilder.append(StringHelper.rightPad4ChineseToByteLength("", 24, " "));
                } else {
                    vchBuilder.append(StringHelper.rightPad4ChineseToByteLength(vb.getTxnamt(), 24, " "));
                }
                if (StringUtils.isEmpty(vb.getVchnum())) {
                    vchBuilder.append("无");
                } else {
                    vchBuilder.append(StringHelper.rightPad4ChineseToByteLength(vb.getVchnum(), 20, ""));
                }
                if (!StringUtils.isEmpty(vb.getVchsts())) {
                    vchBuilder.append(VoucherStatus.valueOfAlias(vb.getVchsts()).getTitle());
                } else {
                    vchBuilder.append("");
                }
                vchBuilder.append(",");
            }
            return vchBuilder.toString();
        }
    }
}
