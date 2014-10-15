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
 * ά���ʽ�-֧ȡȷ��
 */

@XStreamAlias("HMROOT")
public class Tia3004 extends Tia {
    public TiaHeader INFO = new TiaHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        /*
         BANK_ID	    ����ID		�ǿ�
         BANKUSER_ID	�����û�ID	�ǿ�	���ܶ�ϵͳ�������û���ID
         DRAW_BILLNO	֧ȡ����   �ǿ�
         RESERVE	    ������	�����ֶ�	�ɿ�
          */
        public String BANK_ID = "";
        public String BANKUSER_ID = "";
        public String DRAW_BILLNO = "";
        public String RESERVE = "";
    }

    @Override
    public String toString () {
        this.INFO.TXN_CODE = "3004";
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
        xs.processAnnotations(Tia3004.class);
        return (Tia3004) xs.fromXML(xml);
    }
}