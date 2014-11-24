package org.fbi.hmfsjm.online.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjm.enums.TxnRtnCode;
import org.fbi.hmfsjm.enums.VoucherStatus;
import org.fbi.hmfsjm.online.service.Txn0631Service;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// 即墨房屋维修资金票据使用和作废
public class T0631Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {

        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(request.getRequestBody()), "|");
        // 票据编号
        String vchNo = fieldArray[1];
        // 票据状态
        String vchSts = fieldArray[2];
        // 缴款单编号
        String billNo = fieldArray[0];

        String branchID = request.getHeader("branchId");
        String tellerID = request.getHeader("tellerId");
        if (VoucherStatus.USED.getCode().equals(vchSts)) {
            if (StringUtils.isEmpty(billNo)) {
                response.setHeader("rtnCode", TxnRtnCode.TXN_FAILED.getCode());
                response.setResponseBody("使用票据时必须输入缴款单号".getBytes(THIRDPARTY_SERVER_CODING));
            }
        }

        logger.info("[1500631票据使用与作废][网点号]" + branchID + "[柜员号]" + tellerID
                + "  [票据编号] " + vchNo + "[票据状态]" + vchSts + "[缴款单编号]" + billNo);

        try {
            new Txn0631Service().process(branchID, tellerID, vchNo, billNo, vchSts);

        } catch (Exception e) {
            logger.error("[1500631][hmfsjm票据使用与作废]失败", e);
            throw new RuntimeException(e);
        }
    }
}
