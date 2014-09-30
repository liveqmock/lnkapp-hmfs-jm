package org.fbi.hmfsjm.gateway.domain.txn;

import apps.hmfsjm.gateway.domain.base.Toa;
import apps.hmfsjm.gateway.domain.base.xml.ToaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 维修资金-支取拨付单信息查询 3003
 */

@XStreamAlias("HMROOT")
public class Toa3003 extends Toa {
    public ToaHeader INFO = new ToaHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {

        public String DRAW_BILLNO = "";
        public String BILL_STS_CODE = "";
        public String BILL_STS_TITLE = "";
        public String DETAIL_NUM = "0";
        public String RESERVE = "";
        @XStreamImplicit
        public List<Detail> DETAILS = new ArrayList<Detail>();
    }

    @XStreamAlias("DETAIL")
    public static class Detail implements Serializable {
        public String HOUSE_ID = "";
        public String AREA_ACCOUNT = "";
        public String HOUSE_ACCOUNT = "";
        public String HOUSE_LOCATION = "";
        public String HOUSE_AREA = "";
        public String DRAW_MONEY = "";
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa3003.class);
        return (Toa3003) xs.fromXML(xml);
    }
}
