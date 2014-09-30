package org.fbi.hmfsjm.gateway.domain.txn;

import org.fbi.hmfsjm.gateway.domain.base.Tia;
import org.fbi.hmfsjm.gateway.domain.base.xml.TiaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.Serializable;

/**
 * 维修资金-记账汇总信息查询 4003 房管局发起
 */

@XStreamAlias("HMROOT")
public class Tia4003 extends Tia {
    public TiaHeader INFO = new TiaHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        /*
        BANK_ID	银行ID		非空
        RESERVE	保留域	备用字段	可空
         */
        public String BANK_ID = "";
        public String RESERVE = "";
    }

    @Override
    public Tia getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Tia4003.class);
        return (Tia4003) xs.fromXML(xml);
    }
}
