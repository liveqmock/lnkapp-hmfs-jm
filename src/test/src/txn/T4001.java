package txn;

import org.fbi.hmfsjm.gateway.domain.txn.Toa4001;
import utils.MD5Helper;
import utils.SocketClient;
import utils.StringHelper;

/**
 * 分户信息查询-可用于对账
 */
public class T4001 {
    public static void main(String[] args) {
        String xmlmsg = "<?xml version=\"1.0\" encoding=\"GBK\"?>" +
                "<HMROOT>" +
                "<INFO>" +
                "<TXN_CODE>4001</TXN_CODE>" +
                "<REQ_SN>HMAS000092321</REQ_SN>" +
                "</INFO>" +
                "<BODY>" +
                "<BANK_ID>d2178d4d-8188-4e0d-8b71-e3201c7158ec</BANK_ID>" +
                "<BANKUSER_ID>2763cbf7-c0fc-4fd3-8cdb-91f64056bd1c</BANKUSER_ID>" +
                "<ACCOUNT_NUM>1</ACCOUNT_NUM>" +
                "<HOUSE_ACCOUNT>370282000186000100</HOUSE_ACCOUNT>" +
                "<RESERVE></RESERVE>" +
                "</BODY>" +
                "</HMROOT>";

        String mac = MD5Helper.getMD5String(xmlmsg + "20140711105HMFS");
        String reqmsg = "1.00104HMFS   4001      20140711" + mac + xmlmsg;
        int length = reqmsg.getBytes().length + 8;
        System.out.println("【本地客户端】发送报文总长度：" + length);
        String message = StringHelper.appendStrToLength(String.valueOf(length), " ", 8) + reqmsg;
        System.out.println("发送报文：" + message);

        try {
            String resmsg = new SocketClient().onRequest(message, 17777);
            Toa4001 toa = new Toa4001();
            toa = (Toa4001) toa.toToa(resmsg.substring(64));
            System.out.println(toa.INFO.REQ_SN + " " + toa.BODY.ACCOUNT_NUM);
            System.out.println(resmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
