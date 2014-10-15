package org.fbi.hmfsjm.online.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjm.enums.BillTxnStatus;
import org.fbi.hmfsjm.enums.TxnRtnCode;
import org.fbi.hmfsjm.gateway.domain.txn.Toa2001;
import org.fbi.hmfsjm.online.service.Txn1500611Service;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// 缴款确认
public class T1500611Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {

        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(request.getRequestBody()), "|");

        // 缴款书编号
        String billNo = fieldArray[0];
        String branchID = request.getHeader("branchId");
        String tellerID = request.getHeader("tellerId");

        logger.info("[1500611][2001][hmfsjm缴款书缴款确认][网点号]" + branchID + "[柜员号]" + tellerID
                + "  [缴款书编号] " + billNo);

        String serialNo = request.getHeader("serialNo");
        String txnDate = request.getHeader("txnTime").substring(0, 8);
        try {

            Toa2001 toa = (Toa2001) new Txn1500611Service().process(tellerID, branchID, serialNo, billNo, txnDate);
            response.setResponseBody(assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING));
            if (!BillTxnStatus.PAYED_SECCESS.getCode().equals(toa.BODY.BILL_STS_CODE)
                    && !BillTxnStatus.CONFIRMED.getCode().equals(toa.BODY.BILL_STS_CODE)) {
                throw new RuntimeException(StringUtils.isEmpty(toa.BODY.BILL_STS_CODE + toa.BODY.BILL_STS_TITLE) ?
                        (toa.INFO.RET_CODE + toa.INFO.ERR_MSG) : (toa.BODY.BILL_STS_CODE + toa.BODY.BILL_STS_TITLE));
            }
        } catch (Exception e) {
            logger.error("[1500611][2001][hmfsjm缴款单缴款确认]失败", e);
            throw new RuntimeException(e);
        }
    }

    private String assembleStr(Toa2001 toa2001) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(toa2001.BODY.PAY_BILLNO).append("|")                   // 缴款书编号
                .append(nullToEmpty(toa2001.BODY.BILL_STS_CODE)).append("|")     // 缴款单状态代码
                .append(nullToEmpty(toa2001.BODY.BILL_STS_TITLE)).append("|")    // 缴款单状态说明
                .append(nullToEmpty(toa2001.BODY.RESERVE)).append("|");          // 保留域
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

}
