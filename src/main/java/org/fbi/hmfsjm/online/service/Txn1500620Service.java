package org.fbi.hmfsjm.online.service;

import org.fbi.hmfsjm.enums.BillBookType;
import org.fbi.hmfsjm.enums.BillStsFlag;
import org.fbi.hmfsjm.gateway.client.SyncSocketClient;
import org.fbi.hmfsjm.gateway.domain.base.Toa;
import org.fbi.hmfsjm.gateway.domain.txn.Tia3001;
import org.fbi.hmfsjm.gateway.domain.txn.Toa3001;
import org.fbi.hmfsjm.repository.model.HmfsJmRefund;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 1500620 退款单查询交易 业务逻辑  每个缴款单号对应的分户有且只有一个，销户时需将原分户删除，重新开户
 */
public class Txn1500620Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn1500620Service.class);
    private RefundService refundService = new RefundService();

    public Toa process(String tellerID, String branchID, String billNo) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

        Tia3001 tia = new Tia3001();
        tia.BODY.REFUND_BILLNO = billNo;
        tia.INFO.REQ_SN = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());
        logger.info("[3001-退款单信息查询-请求] 流水号：" + tia.INFO.REQ_SN + " 单号：" + tia.BODY.REFUND_BILLNO);

        // 交易发起
        Toa3001 toa = (Toa3001) new SyncSocketClient().onRequest(tia);
        if (toa == null) throw new RuntimeException("网络异常。");

        logger.info("[3001-退款单信息查询-响应] 流水号：" + toa.INFO.REQ_SN +
                " 单号：" + toa.BODY.REFUND_BILLNO +
                " 状态码：" + toa.BODY.BILL_STS_CODE +
                " 状态说明：" + toa.BODY.BILL_STS_TITLE);

        HmfsJmRefund refund = transToa3001ToRefund(tellerID, branchID, toa);
        // 保存
        if (refundService.saveRefundBill(refund)) {
            return toa;
        } else {
            throw new RuntimeException("退款单信息保存失败");
        }
    }

    private HmfsJmRefund transToa3001ToRefund(String tellerID, String branchID, Toa3001 toa3001) {
        HmfsJmRefund refund = new HmfsJmRefund();
        refund.setBillno(toa3001.BODY.REFUND_BILLNO);
        refund.setBillStsCode(toa3001.BODY.BILL_STS_CODE);
        refund.setBillStsTitle(toa3001.BODY.BILL_STS_TITLE);
        refund.setHouseId(toa3001.BODY.HOUSE_ID);
        refund.setHouseLocation(toa3001.BODY.HOUSE_LOCATION);
        refund.setHouseArea(toa3001.BODY.HOUSE_AREA);
        refund.setStandard(toa3001.BODY.STANDARD);
        refund.setPayBank(toa3001.BODY.PAY_BANK);
        refund.setAreaAccount(toa3001.BODY.AREA_ACCOUNT);
        refund.setHouseAccount(toa3001.BODY.HOUSE_ACCOUNT);
        refund.setCardType(toa3001.BODY.CARD_TYPE);
        refund.setCardNo(toa3001.BODY.CARD_NO);
        refund.setOwner(toa3001.BODY.OWNER);
        refund.setTel(toa3001.BODY.TEL);
        refund.setReserve(toa3001.BODY.RESERVE);
        refund.setOperDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        refund.setOperTime(new SimpleDateFormat("HHmmss").format(new Date()));
        refund.setQryTxnCode("3001");
        refund.setBookType(BillBookType.REFUND.getCode());         // 记账类型 00-收款  10-退款 20-支取
        refund.setStsFlag(BillStsFlag.UNBOOK.getCode());            // 单据标志 0-未记账 1-记账未确认 2-记账已确认
        refund.setRelateBillno(toa3001.BODY.PAY_BILL_NO);
        refund.setBankUser(toa3001.BODY.BANK_USER);
        refund.setOperId(tellerID);
        refund.setDeptId(branchID);
        refund.setBankCfmDate(toa3001.BODY.BANK_CFM_DATE);
        refund.setPayMoney(StringUtils.isEmpty(toa3001.BODY.PAY_MONEY) ? null : new BigDecimal(toa3001.BODY.PAY_MONEY));
        refund.setAcceptDate(toa3001.BODY.ACCEPT_DATE);
        refund.setRpType(toa3001.BODY.RP_TYPE);
        refund.setRpMemo(toa3001.BODY.RP_MEMO);
        refund.setTxnAmt(StringUtils.isEmpty(toa3001.BODY.PAY_MONEY) ? null : new BigDecimal(toa3001.BODY.RP_MONEY));
        return refund;
    }
}
