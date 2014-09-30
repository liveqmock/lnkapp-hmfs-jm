package org.fbi.hmfsjm.gateway.domain.txn;

import org.fbi.hmfsjm.gateway.domain.base.Tia;
import org.fbi.hmfsjm.gateway.domain.base.xml.TiaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.Serializable;

/**
 * 维修资金-分户销户 5003
 */

@XStreamAlias("HMROOT")
public class Tia5003 extends Tia {
    public TiaHeader INFO = new TiaHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {

        public String BANK_ID = "";
        public String HOUSE_ID = "";
        public String HOUSE_ACCOUNT = "";
        public String RESERVE = "";
    }

    @Override
    public Tia getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Tia5003.class);
        return (Tia5003) xs.fromXML(xml);
    }

    @Override
    public String toString() {
        this.INFO.TXN_CODE = "5003";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Tia5003.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }
}
