package org.fbi.hmfsjm.online.service;

import org.fbi.hmfsjm.enums.BillBookType;
import org.fbi.hmfsjm.enums.BillStsFlag;
import org.fbi.hmfsjm.gateway.client.SyncSocketClient;
import org.fbi.hmfsjm.gateway.domain.base.Toa;
import org.fbi.hmfsjm.gateway.domain.txn.Tia1001;
import org.fbi.hmfsjm.gateway.domain.txn.Toa1001;
import org.fbi.hmfsjm.repository.model.HmfsJmBill;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 1500610 缴款单查询交易 业务逻辑
 */
public class Txn1500610Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn1500610Service.class);
    private BillService billService = new BillService();

    public Toa process(String tellerID, String branchID, String billNo) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

        Tia1001 tia = new Tia1001();
        tia.BODY.PAY_BILLNO = billNo;
        tia.INFO.REQ_SN = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());
        logger.info("[1001-缴款单信息查询-请求] 流水号：" + tia.INFO.REQ_SN + " 单号：" + tia.BODY.PAY_BILLNO);
        // 交易发起
        Toa1001 toa = (Toa1001) new SyncSocketClient().onRequest(tia);

        if (toa == null) throw new RuntimeException("网络异常。");

        logger.info("[1001-缴款单信息查询-响应] 流水号：" + toa.INFO.REQ_SN +
                " 单号：" + toa.BODY.PAY_BILLNO +
                " 状态码：" + toa.BODY.BILL_STS_CODE +
                " 状态说明：" + toa.BODY.BILL_STS_TITLE);

        HmfsJmBill bill = transToa1001ToBill(tellerID, branchID, toa);
        // 保存
        if (billService.saveDepositBill(bill)) {
            return toa;
        } else {
            // 交易失败
            throw new RuntimeException("缴款单信息保存失败");
        }
    }

    private HmfsJmBill transToa1001ToBill(String operid, String branchID, Toa1001 toa1001) {
        HmfsJmBill bill = new HmfsJmBill();
        bill.setBillno(toa1001.BODY.PAY_BILLNO);
        bill.setBillStsCode(toa1001.BODY.BILL_STS_CODE);
        bill.setBillStsTitle(toa1001.BODY.BILL_STS_TITLE);
        bill.setHouseId(toa1001.BODY.HOUSE_ID);
        bill.setHouseLocation(toa1001.BODY.HOUSE_LOCATION);
        bill.setHouseArea(toa1001.BODY.HOUSE_AREA);
        bill.setStandard(toa1001.BODY.STANDARD);
        bill.setTxnAmt(StringUtils.isEmpty(toa1001.BODY.TXN_AMT) ? null : new BigDecimal(toa1001.BODY.TXN_AMT.trim()));
        bill.setPayBank(toa1001.BODY.PAY_BANK);
        bill.setAreaAccount(toa1001.BODY.AREA_ACCOUNT);
        bill.setHouseAccount(toa1001.BODY.HOUSE_ACCOUNT);
        bill.setCardType(toa1001.BODY.CARD_TYPE);
        bill.setCardNo(toa1001.BODY.CARD_NO);
        bill.setOwner(toa1001.BODY.OWNER);
        bill.setTel(toa1001.BODY.TEL);
        bill.setReserve(toa1001.BODY.RESERVE);
        bill.setOperDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        bill.setOperTime(new SimpleDateFormat("HHmmss").format(new Date()));
        bill.setQryTxnCode("1001");
        bill.setOperId(operid);
        bill.setDeptId(branchID);
        bill.setBookType(BillBookType.DEPOSIT.getCode());         // 记账类型 00-收款  10-退款 20-支取 99-开户
        bill.setStsFlag(BillStsFlag.UNBOOK.getCode());            // 单据标志 0-未记账 1-记账未确认 2-记账已确认
        return bill;
    }


}
