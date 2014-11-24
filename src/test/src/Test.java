import org.fbi.hmfsjm.gateway.domain.txn.Tia3003;

/**
 * Created by lenovo on 2014-09-25.
 */
public class Test {

    public static void main(String[] args) {
        Tia3003 tia = new Tia3003();
        String tiaStr = tia.toString();
        System.out.println(tiaStr);
        Tia3003 bean = (Tia3003)tia.getTia(tiaStr);
        System.out.println(bean.INFO.TXN_CODE);
    }
}
