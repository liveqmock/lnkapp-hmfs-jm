package org.fbi.hmfsjm.internal;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

/**
 * Created by lenovo on 2014-10-17.
 */
public class InterestTimerTask extends TimerTask {
    public static long PERIOD_DAY = 24 * 60 * 60 * 1000;

    // ��ʱ��Ϣ
    public void run() {

    }


    // ����һ��
    public static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }
}
