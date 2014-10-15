package org.fbi.hmfsjm.online.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjm.enums.TxnRtnCode;
import org.fbi.hmfsjm.online.service.Txn1500632Service;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// 票据使用情况概览
public class T1500632Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {

        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(request.getRequestBody()), "|");
        // 日期
        String date8 = fieldArray[0];
        // 缴款单号
        String billNo = fieldArray[1];

        String branchID = request.getHeader("branchId");
        String tellerID = request.getHeader("tellerId");

        logger.info("[1500632票据使用情况查询][日期]" + date8 + "[单号]" + billNo +
                " [网点]" + branchID + "[柜员]" + tellerID);

        try {
            String strVchs = new Txn1500632Service().process(date8, billNo);
            if (StringUtils.isEmpty(strVchs)) {
                response.setHeader("rtnCode", TxnRtnCode.TXN_FAILED.getCode());
                response.setResponseBody("没有查询到票据信息".getBytes(THIRDPARTY_SERVER_CODING));
            } else {
                response.setResponseBody(strVchs.getBytes(THIRDPARTY_SERVER_CODING));
            }

        } catch (Exception e) {
            logger.error("[1500632][hmfsjm票据使用情况查询]失败", e);
            throw new RuntimeException(e);
        }
    }
}
