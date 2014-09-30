package org.fbi.hmfsjm.gateway.domain.txn;

import org.fbi.hmfsjm.gateway.domain.base.Tia;
import org.fbi.hmfsjm.gateway.domain.base.xml.TiaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 维修资金-提交分户结息明细
 */

@XStreamAlias("HMROOT")
public class Tia5002 extends Tia {
    public TiaHeader INFO;
    public Body BODY = new Body();

    public static class Body implements Serializable {
        /*
        BANK_ID	银行ID		非空
        BANKUSER_ID	银行用户ID	非空	房管端系统对银行用户的ID
        DETAIL_NUM	明细数   非空
        RESERVE	保留域	备用字段	可空
         */
        public String BANK_ID = "";
        public String BANKUSER_ID = "";
        public String RESERVE = "";
        public String DETAIL_NUM = "0";
        @XStreamImplicit
        public List<Detail> DETAILS = new ArrayList<Detail>();

    }

    @XStreamAlias("DETAIL")
    public static class Detail implements Serializable {
        public String HOUSE_ACCOUNT = "";
        public String INTEREST_NO = "";
        public String INTEREST_NAME = "";
        public String HOUSE_ID = "";
        public String HOUSE_LOCATION = "";
        public String OWNER = "";
        public String BEFORE_AMT = "";
        public String AFTER_AMT = "";
        public String BEGIN_DATE = "";
        public String END_DATE = "";
        public String CAPITAL = "";
        public String RATE = "";
        public String INTEREST = "";
    }

    @Override
    public Tia getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Tia5002.class);
        return (Tia5002) xs.fromXML(xml);
    }
}
