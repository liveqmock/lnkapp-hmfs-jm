package org.fbi.hmfsjm.gateway.domain.txn;

import org.fbi.hmfsjm.gateway.domain.base.Tia;
import org.fbi.hmfsjm.gateway.domain.base.xml.TiaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.fbi.hmfsjm.helper.ProjectConfigManager;

import java.io.Serializable;

/**
 * 维修资金-退款单查询 3001
 */

@XStreamAlias("HMROOT")
public class Tia3001 extends Tia {
    public TiaHeader INFO = new TiaHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        public String BANK_ID = "";
        public String BANKUSER_ID = "";
        public String REFUND_BILLNO = "";
        public String RESERVE = "";
    }

    @Override
    public String toString () {
        this.INFO.TXN_CODE = "3001";
        this.BODY.BANKUSER_ID = ProjectConfigManager.getInstance().getProperty("bank.userid");
        this.BODY.BANK_ID = ProjectConfigManager.getInstance().getProperty("bank.id");
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Tia3001.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }

    @Override
    public Tia getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Tia3001.class);
        return (Tia3001) xs.fromXML(xml);
    }
}
