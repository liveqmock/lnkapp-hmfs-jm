package bankclient;

import org.fbi.hmfsjm.gateway.domain.txn.Toa4001;
import utils.MD5Helper;
import utils.SocketClient;
import utils.StringHelper;

/**
 * ֧ȡ��ѯ
 */
public class T0640 {
    public static void main(String[] args) {

        String msg = "12345";

        String reqmsg = "1.012345678912345678900001500640123456789123456789123123456HMFSJM1234567891234512345678912345678912345678912345 " + msg;
        int length = reqmsg.getBytes().length + 6;
        System.out.println("�����ؿͻ��ˡ����ͱ����ܳ��ȣ�" + length);
        String message = StringHelper.appendStrToLength(String.valueOf(length), " ", 6) + reqmsg;
        System.out.println("���ͱ��ģ�" + message);

        try {
            String resmsg = new SocketClient().onRequest6(message, 17777);

            System.out.println(resmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
