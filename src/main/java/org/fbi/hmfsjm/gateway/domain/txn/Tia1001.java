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
 * 维修资金-缴款单查询 1001
 */

@XStreamAlias("HMROOT")
public class Tia1001 extends Tia {
    public TiaHeader INFO = new TiaHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        /*
        BANK_ID	银行ID		非空
        BANKUSER_ID	银行用户ID	非空	房管端系统对银行用户的ID
        PAY_BILLNO	缴存通知单号   非空
        RESERVE	保留域	备用字段	可空
         */
        public String BANK_ID = "";
        public String BANKUSER_ID = "";
        public String PAY_BILLNO = "";
        public String RESERVE = "";
    }

    @Override
    public String toString () {
        this.INFO.TXN_CODE = "1001";
        this.BODY.BANKUSER_ID = ProjectConfigManager.getInstance().getProperty("bank.userid");
        this.BODY.BANK_ID = ProjectConfigManager.getInstance().getProperty("bank.id");
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Tia1001.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }

    @Override
    public Tia getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Tia1001.class);
        return (Tia1001) xs.fromXML(xml);
    }
}
