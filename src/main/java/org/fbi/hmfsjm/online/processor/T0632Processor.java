package org.fbi.hmfsjm.online.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjm.enums.TxnRtnCode;
import org.fbi.hmfsjm.online.service.Txn0632Service;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// Ʊ��ʹ���������
public class T0632Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {

        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(request.getRequestBody()), "|");
        // ����
        String date8 = fieldArray[0];
        // �ɿ��
        String billNo = fieldArray[1];

        String branchID = request.getHeader("branchId");
        String tellerID = request.getHeader("tellerId");

        logger.info("[1500632Ʊ��ʹ�������ѯ][����]" + date8 + "[����]" + billNo +
                " [����]" + branchID + "[��Ա]" + tellerID);

        try {
            String strVchs = new Txn0632Service().process(date8, billNo);
            if (StringUtils.isEmpty(strVchs)) {
                response.setHeader("rtnCode", TxnRtnCode.TXN_FAILED.getCode());
                response.setResponseBody("û�в�ѯ��Ʊ����Ϣ".getBytes(THIRDPARTY_SERVER_CODING));
            } else {
                response.setResponseBody(strVchs.getBytes(THIRDPARTY_SERVER_CODING));
            }

        } catch (Exception e) {
            logger.error("[1500632][hmfsjmƱ��ʹ�������ѯ]ʧ��", e);
            throw new RuntimeException(e);
        }
    }
}