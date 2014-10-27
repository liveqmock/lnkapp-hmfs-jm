package org.fbi.hmfsjm.internal;

import org.fbi.hmfsjm.online.service.InterestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

/**
 * Created by lenovo on 2014-10-17.
 */
public class InterestTimerTask extends TimerTask {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    // 定时计息
    public void run() {
         try {
             InterestService interestService = new InterestService();
             // 计息
             interestService.accrualInterest();
             // 发送计息明细
             interestService.sendInterestTxns();
         } catch (Exception e) {
             logger.error("业务异常", e);
             return;
         }

    }
}
