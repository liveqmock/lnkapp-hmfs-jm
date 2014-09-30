package org.fbi.hmfsjm.gateway.domain.txn;

import org.fbi.hmfsjm.gateway.domain.base.Toa;
import org.fbi.hmfsjm.gateway.domain.base.xml.ToaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.Serializable;

/**
 * 维修资金-支取确认
 */

@XStreamAlias("HMROOT")
public class Toa3004 extends Toa {

    public ToaHeader INFO = new ToaHeader();
    public Body BODY = new Body();

    public static class Body  implements Serializable {
        /*
          DRAW_BILLNO	支取单号
          BILL_STS_CODE	缴款结果代码
          BILL_STS_TITLE	缴款结果说明
          RESERVE	保留域
         */
            public String DRAW_BILLNO = "";
            public String BILL_STS_CODE = "";
            public String BILL_STS_TITLE = "";
            public String RESERVE = "";
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa3004.class);
        return (Toa3004) xs.fromXML(xml);
    }
}
