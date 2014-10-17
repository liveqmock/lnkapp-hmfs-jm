package org.fbi.hmfsjm.online.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjm.gateway.domain.txn.Toa3004;
import org.fbi.hmfsjm.online.service.Txn1500641Service;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// 支取确认
public class T1500641Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {

        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(request.getRequestBody()), "|");

        String branchID = request.getHeader("branchId");
        String tellerID = request.getHeader("tellerId");
        // 支取编号
        String drawNo = fieldArray[0];

        logger.info("[1500641][3004][hmfsjm支取确认][网点号]" + branchID + "[柜员号]" + tellerID
                + "  [支取编号] " + drawNo);

        String serialNo = request.getHeader("serialNo");
        String txnDate = request.getHeader("txnTime");
        try {
            // 交易发起
            Toa3004 toa = (Toa3004) new Txn1500641Service().process(tellerID, branchID, serialNo, drawNo, txnDate);
            response.setResponseBody(assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING));
        } catch (Exception e) {
            logger.error("[1500641][3004][hmfsjm支取确认]失败", e);
            throw new RuntimeException(e);
        }
    }

    private String assembleStr(Toa3004 toa3004) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(toa3004.BODY.DRAW_BILLNO).append("|")                  // 支取编号
                .append(nullToEmpty(toa3004.BODY.BILL_STS_CODE)).append("|")     // 状态代码
                .append(nullToEmpty(toa3004.BODY.BILL_STS_TITLE)).append("|")    // 状态说明
                .append(nullToEmpty(toa3004.BODY.RESERVE)).append("|");          // 保留域
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

}
