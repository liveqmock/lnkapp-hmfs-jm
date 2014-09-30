package org.fbi.hmfsjm.gateway.domain.txn;

import org.fbi.hmfsjm.gateway.domain.base.Toa;
import org.fbi.hmfsjm.gateway.domain.base.xml.ToaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.Serializable;

/**
 * 维修资金-退款单查询 3001
 */

@XStreamAlias("HMROOT")
public class Toa3001 extends Toa {
    public ToaHeader INFO = new ToaHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        /*
        REFUND_BILLNO 退款单号
        BILL_STS_CODE	退款单状态代码
        BILL_STS_TITLE	单据状态说明
        PAY_BILL_NO	缴存通知单号
        PAY_BANK	收款银行
        BANK_USER	银行收款人
        BANK_CFM_DATE	银行收款日期
        PAY_MONEY	银行收款金额
        HOUSE_ID	房屋编号
        HOUSE_LOCATION	房屋坐落
        HOUSE_AREA	建筑面积
        STANDARD	缴存标准
        AREA_ACCOUNT	专户账号
        HOUSE_ACCOUNT	分户账号
        CARD_TYPE	证件类型
        CARD_NO	证件号码
        OWNER	业主姓名
        TEL	联系电话
        ACCEPT_DATE	缴存受理日期
        RESERVE	保留域
         RP_TYPE	退款类别
        RP_MEMO	退款内容
        RP_MONEY	退款金额
         */
        public String REFUND_BILLNO = "";
        public String BILL_STS_CODE = "";
        public String BILL_STS_TITLE = "";
        public String RP_TYPE = "";
        public String RP_MEMO = "";
        public String RP_MONEY = "";
        public String PAY_BILL_NO = "";
        public String PAY_BANK = "";
        public String BANK_USER = "";
        public String BANK_CFM_DATE = "";
        public String PAY_MONEY = "";
        public String HOUSE_ID = "";
        public String HOUSE_LOCATION = "";
        public String HOUSE_AREA = "";
        public String STANDARD = "";
        public String AREA_ACCOUNT = "";
        public String HOUSE_ACCOUNT = "";
        public String CARD_TYPE = "";
        public String CARD_NO = "";
        public String OWNER = "";
        public String TEL = "";
        public String ACCEPT_DATE = "";
        public String RESERVE = "";
    }

    @Override
    public String toString() {
        this.INFO.TXN_CODE = "3001";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Toa3001.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa3001.class);
        return (Toa3001) xs.fromXML(xml);
    }
}
