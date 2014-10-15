package org.fbi.hmfsjm.online.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjm.enums.TxnRtnCode;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10Processor;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;

/**
 * User: :zhangxiaobo
 * response.addHeader("version", request.getHeader("version"));
 * response.addHeader("serialNo", request.getHeader("serialNo"));
 * response.addHeader("txnCode", request.getHeader("txnCode"));
 * response.addHeader("branchId", request.getHeader("branchId"));
 * response.addHeader("tellerId", request.getHeader("tellerId"));
 * response.addHeader("ueserId", request.getHeader("ueserId"));
 * response.addHeader("appId", request.getHeader("appId"));
 * response.addHeader("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
 */
public abstract class AbstractTxnProcessor extends Stdp10Processor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    protected static String THIRDPARTY_SERVER_CODING = "GBK";

    @Override
    public void service(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {
        String txnCode = request.getHeader("txnCode");
        String tellerId = request.getHeader("tellerId");
        if (StringUtils.isEmpty(tellerId)) {
            tellerId = "TELLERID";
        }
        try {
            MDC.put("txnCode", txnCode);
            MDC.put("tellerId", tellerId);
            doRequest(request, response);
        } catch (Exception e) {
            response.setHeader("rtnCode", TxnRtnCode.TXN_FAILED.getCode());
            String errmsg = e.getMessage();
            if (StringUtils.isEmpty(errmsg)) {
                response.setResponseBody(TxnRtnCode.TXN_FAILED.getTitle().getBytes(THIRDPARTY_SERVER_CODING));
            } else
                response.setResponseBody(e.getMessage().getBytes(THIRDPARTY_SERVER_CODING));
            throw new RuntimeException(e);
        } finally {
            MDC.remove("txnCode");
            MDC.remove("tellerId");
        }
    }


    abstract protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException;
}
