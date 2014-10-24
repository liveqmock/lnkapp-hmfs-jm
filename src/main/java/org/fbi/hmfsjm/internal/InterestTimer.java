package org.fbi.hmfsjm.internal;

import org.fbi.hmfsjm.helper.ProjectConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class InterestTimer implements Runnable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public static long PERIOD_DAY = 24 * 60 * 60 * 1000;

    @Override
    public void run() {


        Timer timer = new Timer();
        int hour = Integer.parseInt(ProjectConfigManager.getInstance().getProperty("interest.time.hour"));
        int minute = Integer.parseInt(ProjectConfigManager.getInstance().getProperty("interest.time.minute"));
        logger.info("HMFSJM 定时计息任务开启...时间： " + hour + ":" + minute);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        // 第一次执行定时任务的时间
        // 如果第一次执行定时任务的时间 小于当前的时间
        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
        if (date.before(new Date())) {
            date = addDay(date, 1);
        }
        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
        timer.schedule(new InterestTimerTask(), date, PERIOD_DAY);
    }


    // 增加一天
    public static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

}
