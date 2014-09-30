package org.fbi.hmfsjm.gateway.domain.txn;

import org.fbi.hmfsjm.gateway.domain.base.Toa;
import org.fbi.hmfsjm.gateway.domain.base.xml.ToaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.Serializable;

/**
 * 维修资金-退款确认
 */

@XStreamAlias("HMROOT")
public class Toa3002 extends Toa {

    public ToaHeader INFO = new ToaHeader();
    public Body BODY = new Body();

    public static class Body  implements Serializable {
        /*
          REFUND_BILLNO	 缴存通知单号
          BILL_STS_CODE	 缴款结果代码
          BILL_STS_TITLE 缴款结果说明
          RESERVE	     保留域
         */
        public String REFUND_BILLNO = "";
        public String BILL_STS_CODE = "";
        public String BILL_STS_TITLE = "";
        public String RESERVE = "";
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa3002.class);
        return (Toa3002) xs.fromXML(xml);
    }
}
