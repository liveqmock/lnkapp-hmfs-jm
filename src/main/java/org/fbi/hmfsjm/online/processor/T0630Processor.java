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

// Ʊ������
public class T0630Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {

        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(request.getRequestBody()), "|");
        // ��ʼ���
        String vchStartNo = fieldArray[0];
        // �������
        String vchEndNo = fieldArray[1];

        String branchID = request.getHeader("branchId");
        String tellerID = request.getHeader("tellerId");
        logger.info("[1500630Ʊ������][�����]" + branchID + "[��Ա��]" + tellerID
                + "  [��ʼ���] " + vchStartNo + "[�������]" + vchEndNo);

        try {
            long startNo = Long.parseLong(vchStartNo);
            long endNo = Long.parseLong(vchEndNo);
            if (startNo > endNo) {
                response.setHeader("rtnCode", TxnRtnCode.TXN_FAILED.getCode());
                response.setResponseBody("��ʼ��Ų��ܴ�����ֹ���".getBytes(THIRDPARTY_SERVER_CODING));
            } else {
                new Txn0630Service().process(branchID, tellerID, startNo, endNo);
            }
        } catch (Exception e) {
            logger.error("[1500630][hmfsjmƱ������]ʧ��", e);
            throw new RuntimeException(e);
        }
    }
}
