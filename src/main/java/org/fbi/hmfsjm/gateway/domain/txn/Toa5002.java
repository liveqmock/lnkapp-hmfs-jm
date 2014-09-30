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
 * 维修资金-结息
 */

@XStreamAlias("HMROOT")
public class Toa5002 extends Toa {

    public ToaHeader INFO = new ToaHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        /*
          STS_CODE	结果代码
          DETAIL_NUM	异常明细数
         */
        public String STS_CODE = "";
        public String DETAIL_NUM = "";
        public String RESERVE = "";
        @XStreamImplicit
        public List<Detail> DETAILS = new ArrayList<Detail>();
    }

    @XStreamAlias("DETAIL")
    public static class Detail implements Serializable {
        public String INTEREST_NO = "";
        public String STATUS = "";
    }


    @Override
    public String toString() {
        this.INFO.TXN_CODE = "5002";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Toa5002.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa5002.class);
        return (Toa5002) xs.fromXML(xml);
    }
}
