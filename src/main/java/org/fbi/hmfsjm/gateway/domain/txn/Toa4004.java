package org.fbi.hmfsjm.gateway.domain.txn;

import org.fbi.hmfsjm.gateway.domain.base.Toa;
import org.fbi.hmfsjm.gateway.domain.base.xml.ToaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 维修资金-分户记账流水查询-用于对账
 */

@XStreamAlias("HMROOT")
public class Toa4004 extends Toa {

    public ToaHeader INFO = new ToaHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        /*
          DETAIL_NUM	笔数
         */
        public String DETAIL_NUM = "0";
        public String RESERVE = "";
        @XStreamImplicit
        public List<Detail> DETAILS = new ArrayList<Detail>();
    }

    @XStreamAlias("DETAIL")
    public static class Detail implements Serializable {
        public String HOUSE_ACCOUNT = "";
        public String BILL_NO = "";
        public String BOOK_FLAG = "";
        public String TXN_MONEY = "";
        public String TXN_TIME = "";
    }


    @Override
    public String toString() {
        this.INFO.TXN_CODE = "4004";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Toa4004.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa4004.class);
        return (Toa4004) xs.fromXML(xml);
    }
}
