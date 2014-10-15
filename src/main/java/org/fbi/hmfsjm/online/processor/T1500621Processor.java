package org.fbi.hmfsjm.online.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjm.enums.BillTxnStatus;
import org.fbi.hmfsjm.gateway.domain.txn.Toa3001;
import org.fbi.hmfsjm.gateway.domain.txn.Toa3002;
import org.fbi.hmfsjm.online.service.Txn1500620Service;
import org.fbi.hmfsjm.online.service.Txn1500621Service;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class T1500621Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {

        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(request.getRequestBody()), "|");

        String branchID = request.getHeader("branchId");
        String tellerID = request.getHeader("tellerId");
        // 退款书编号
        String refundNo = fieldArray[0];

        logger.info("[1500621][3002][hmfsjm退款书退款确认][网点号]" + branchID + "[柜员号]" + tellerID
                + "  [退款书编号] " + refundNo);

        String serialNo = request.getHeader("serialNo");
        String txnDate = request.getHeader("txnTime");
        try {
            // 交易发起
            Toa3002 toa = (Toa3002) new Txn1500621Service().process(tellerID, branchID, serialNo, refundNo, txnDate);

            response.setResponseBody(assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING));
        } catch (Exception e) {
            logger.error("[1500621][3002][hmfsjm退款单退款确认]失败", e);
            throw new RuntimeException(e);
        }
    }


    private String assembleStr(Toa3002 toa3002) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(toa3002.BODY.REFUND_BILLNO).append("|")                // 退款书编号
                .append(nullToEmpty(toa3002.BODY.BILL_STS_CODE)).append("|")     // 退款单状态代码
                .append(nullToEmpty(toa3002.BODY.BILL_STS_TITLE)).append("|")    // 退款单状态说明
                .append(nullToEmpty(toa3002.BODY.RESERVE)).append("|");          // 保留域
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

}
