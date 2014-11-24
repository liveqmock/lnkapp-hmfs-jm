package org.fbi.hmfsjm.online.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjm.enums.TxnRtnCode;
import org.fbi.hmfsjm.online.service.Txn0630Service;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// 票据领用
public class T0630Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {

        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(request.getRequestBody()), "|");
        // 起始编号
        String vchStartNo = fieldArray[0];
        // 结束编号
        String vchEndNo = fieldArray[1];

        String branchID = request.getHeader("branchId");
        String tellerID = request.getHeader("tellerId");
        logger.info("[1500630票据领用][网点号]" + branchID + "[柜员号]" + tellerID
                + "  [起始编号] " + vchStartNo + "[结束编号]" + vchEndNo);

        try {
            long startNo = Long.parseLong(vchStartNo);
            long endNo = Long.parseLong(vchEndNo);
            if (startNo > endNo) {
                response.setHeader("rtnCode", TxnRtnCode.TXN_FAILED.getCode());
                response.setResponseBody("起始编号不能大于终止编号".getBytes(THIRDPARTY_SERVER_CODING));
            } else {
                new Txn0630Service().process(branchID, tellerID, startNo, endNo);
            }
        } catch (Exception e) {
            logger.error("[1500630][hmfsjm票据领用]失败", e);
            throw new RuntimeException(e);
        }
    }
}
