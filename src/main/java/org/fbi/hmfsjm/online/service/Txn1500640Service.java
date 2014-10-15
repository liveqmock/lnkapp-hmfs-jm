package org.fbi.hmfsjm.online.service;

import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjm.enums.BillBookType;
import org.fbi.hmfsjm.enums.BillStsFlag;
import org.fbi.hmfsjm.enums.DrawQryStatus;
import org.fbi.hmfsjm.gateway.client.SyncSocketClient;
import org.fbi.hmfsjm.gateway.domain.base.Toa;
import org.fbi.hmfsjm.gateway.domain.txn.Tia3003;
import org.fbi.hmfsjm.gateway.domain.txn.Toa3003;
import org.fbi.hmfsjm.repository.model.HmfsJmDraw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 1500640 支取查询
 */
public class Txn1500640Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn1500640Service.class);
    private DrawService drawService = new DrawService();

    public Toa process(String tellerID, String branchID, String billNo, String txnDate) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

        Tia3003 tia = new Tia3003();
        tia.BODY.DRAW_BILLNO = billNo;
        tia.INFO.REQ_SN = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());
        logger.info("[3001-退款单信息查询-请求] 流水号：" + tia.INFO.REQ_SN + " 单号：" + tia.BODY.DRAW_BILLNO);

        // 交易发起
        Toa3003 toa = (Toa3003) new SyncSocketClient().onRequest(tia);
        if (toa == null) throw new RuntimeException("网络异常。");

        logger.info("[3003-支取单信息查询-响应] 流水号：" + toa.INFO.REQ_SN +
                " 单号：" + toa.BODY.DRAW_BILLNO +
                " 笔数：" + toa.BODY.DETAIL_NUM +
                " 状态码：" + toa.BODY.BILL_STS_CODE +
                " 状态说明：" + toa.BODY.BILL_STS_TITLE);

        List<HmfsJmDraw> drawList = transToa3003ToDraws(tellerID, branchID, toa, txnDate);
        // 保存
        drawService.saveDrawBills(drawList, billNo);
        return toa;
    }

    private List<HmfsJmDraw> transToa3003ToDraws(String tellerID, String branchID, Toa3003 toa3003, String txnDate) {

        if (!DrawQryStatus.VALAID.getCode().equals(toa3003.BODY.BILL_STS_CODE)) {
            throw new RuntimeException("状态：" + toa3003.BODY.BILL_STS_CODE + toa3003.BODY.BILL_STS_TITLE);
        }
        List<HmfsJmDraw> draws = new ArrayList<>();
        for (Toa3003.Detail detail : toa3003.BODY.DETAILS) {
            HmfsJmDraw draw = new HmfsJmDraw();
            draw.setBillno(toa3003.BODY.DRAW_BILLNO);
            draw.setBillStsCode(toa3003.BODY.BILL_STS_CODE);
            draw.setBillStsTitle(toa3003.BODY.BILL_STS_TITLE);
            draw.setHouseId(detail.HOUSE_ID);
            draw.setHouseArea(detail.HOUSE_AREA);
            draw.setAreaAccount(detail.AREA_ACCOUNT);
            draw.setHouseAccount(detail.HOUSE_ACCOUNT);
            draw.setHouseLocation(detail.HOUSE_LOCATION);
            draw.setReserve(toa3003.BODY.RESERVE);
            draw.setOperDate(txnDate.substring(0, 8));
            draw.setOperTime(txnDate.substring(8));
            draw.setQryTxnCode("3003");
            draw.setBookType(BillBookType.DRAW.getCode());            // 记账类型 00-收款  10-退款 20-支取
            draw.setStsFlag(BillStsFlag.UNBOOK.getCode());            // 单据标志 0-未记账 1-记账未确认 2-记账已确认
            draw.setOperId(tellerID);
            draw.setDeptId(branchID);
            draw.setTxnAmt(StringUtils.isEmpty(detail.DRAW_MONEY) ? null : new BigDecimal(detail.DRAW_MONEY));
            draws.add(draw);
        }
        return draws;
    }
}
