package org.fbi.hmfsjm.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lenovo on 2014-10-15.
 */
public class Jodatime {

//    public static long PERIOD_DAY = 24 * 60 * 60 * 1000;
    public static long PERIOD_DAY = 5 * 1000;
    public static void main(String[] args) {
        Timer timer = new Timer();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 29);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime(); //��һ��ִ�ж�ʱ�����ʱ��
        //�����һ��ִ�ж�ʱ�����ʱ�� С�ڵ�ǰ��ʱ��
        //��ʱҪ�� ��һ��ִ�ж�ʱ�����ʱ���һ�죬�Ա���������¸�ʱ���ִ�С��������һ�죬���������ִ�С�
        if (date.before(new Date())) {
            date = addDay(date, 1);
        }
        //����ָ����������ָ����ʱ�俪ʼ�����ظ��Ĺ̶��ӳ�ִ�С�
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(new SimpleDateFormat("yyyyddmm HH:mm:ss").format(new Date()));
            }
        }, date, PERIOD_DAY);
    }

    // ����һ��
    public static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }
}
