package org.fbi.hmfsjm.online.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjm.gateway.domain.txn.Toa1001;
import org.fbi.hmfsjm.online.service.Txn0610Service;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// 缴款查询
public class T0610Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {

        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(request.getRequestBody()), "|");
        // 缴款书编号
        String billNo = fieldArray[0];
        String branchID = request.getHeader("branchId");
        String tellerID = request.getHeader("tellerId");

        logger.info("[1500610缴款书信息查询][网点号]" + branchID + "[柜员号]" + tellerID + "  [缴款书编号] " + billNo);
        try {
            Toa1001 toa = (Toa1001) new Txn0610Service().process(tellerID, branchID, billNo);
            response.setResponseBody(assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING));
        } catch (Exception e) {
            logger.error("[1500610][1001][hmfsjm缴款单查询]失败", e);
            throw new RuntimeException(e);
        }
    }

    private String assembleStr(Toa1001 toa1001) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(toa1001.BODY.PAY_BILLNO).append("|")                   // 缴款书编号
                .append(nullToEmpty(toa1001.BODY.BILL_STS_CODE)).append("|")     // 缴款单状态代码
                .append(nullToEmpty(toa1001.BODY.BILL_STS_TITLE)).append("|")    // 缴款单状态说明
                .append(nullToEmpty(toa1001.BODY.HOUSE_ID)).append("|")          // 房屋编号
                .append(nullToEmpty(toa1001.BODY.HOUSE_LOCATION)).append("|")    // 房屋坐落
                .append(nullToEmpty(toa1001.BODY.HOUSE_AREA)).append("|")        // 建筑面积
                .append(nullToEmpty(toa1001.BODY.STANDARD)).append("|")          // 缴存标准
                .append(nullToEmpty(toa1001.BODY.TXN_AMT)).append("|")           // 金额
                .append(nullToEmpty(toa1001.BODY.PAY_BANK)).append("|")          // 缴款银行
                .append(nullToEmpty(toa1001.BODY.AREA_ACCOUNT)).append("|")      // 专户账号
                .append(nullToEmpty(toa1001.BODY.HOUSE_ACCOUNT)).append("|")     // 分户账号
                .append(nullToEmpty(toa1001.BODY.CARD_TYPE)).append("|")         // 证件类型
                .append(nullToEmpty(toa1001.BODY.CARD_NO)).append("|")           // 证件号码
                .append(nullToEmpty(toa1001.BODY.OWNER)).append("|")             // 业主姓名
                .append(nullToEmpty(toa1001.BODY.TEL)).append("|")               // 联系电话
                .append(nullToEmpty(toa1001.BODY.RESERVE)).append("|");          // 保留域
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
