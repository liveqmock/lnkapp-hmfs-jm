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
 * 维修资金-银行发起分户信息查询-用于对账
 */

@XStreamAlias("HMROOT")
public class Toa4001 extends Toa {

    public ToaHeader INFO = new ToaHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        /*
          ACCOUNT_NUM	账户笔数
         */
        public String ACCOUNT_NUM = "0";
        public String RESERVE = "";
        @XStreamImplicit
        public List<Account> ACCOUNTS = new ArrayList<Account>();
    }

    @XStreamAlias("ACCOUNT")
    public static class Account implements Serializable {
        public String HOUSE_ACCOUNT = "";
        public String STATUS = "";
        public String HOUSE_ID = "";
        public String HOUSE_LOCATION = "";
        public String HOUSE_AREA = "";
        public String STANDARD = "";
        public String PAY_MONEY = "";
        public String BALAMT = "";
        public String ACCEPT_DATE = "";
    }


    @Override
    public String toString() {
        this.INFO.TXN_CODE = "4001";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Toa4001.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa4001.class);
        return (Toa4001) xs.fromXML(xml);
    }
}
