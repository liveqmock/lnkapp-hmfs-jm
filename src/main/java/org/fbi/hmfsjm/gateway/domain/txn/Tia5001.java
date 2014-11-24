package org.fbi.hmfsjm.gateway.domain.txn;

import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.fbi.hmfsjm.gateway.domain.base.Tia;
import org.fbi.hmfsjm.gateway.domain.base.xml.TiaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.fbi.hmfsjm.helper.ProjectConfigManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 维修资金-分户信息查询
 */

@XStreamAlias("HMROOT")
public class Tia5001 extends Tia {
    public TiaHeader INFO;
    public Body BODY = new Body();

    public static class Body implements Serializable {
        /*
        BANK_ID	银行ID		非空
        BANKUSER_ID	银行用户ID	非空	房管端系统对银行用户的ID
        ACCOUNT_NUM 账户数   非空
        RESERVE	保留域	备用字段	可空
         */
        public String BANK_ID = "";
        public String BANKUSER_ID = "";
        public String RESERVE = "";
        public String ACCOUNT_NUM = "0";

        @XStreamImplicit(itemFieldName = "HOUSE_ACCOUNT")
        public List<String> ACCOUNTS = new ArrayList<String>();
    }

    @Override
    public String toString() {
        this.INFO.TXN_CODE = "5001";
        this.BODY.BANKUSER_ID = ProjectConfigManager.getInstance().getProperty("bank.userid");
        this.BODY.BANK_ID = ProjectConfigManager.getInstance().getProperty("bank.id");
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(this.getClass());
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }

    @Override
    public Tia getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Tia5001.class);
        return (Tia5001) xs.fromXML(xml);
    }

}
