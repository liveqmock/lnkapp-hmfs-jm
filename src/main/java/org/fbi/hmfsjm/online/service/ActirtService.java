package org.fbi.hmfsjm.online.service;

import org.apache.ibatis.session.SqlSession;
import org.fbi.hmfsjm.repository.MybatisManager;
import org.fbi.hmfsjm.repository.dao.HmfsJmIrtMapper;
import org.fbi.hmfsjm.repository.model.HmfsJmIrt;
import org.fbi.hmfsjm.repository.model.HmfsJmIrtExample;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 利率
 */
public class ActirtService {
    MybatisManager manager = new MybatisManager();

    public int daysBetween(String beginDate, String endDate) {
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime begin = format.parseDateTime(beginDate);
        DateTime end = format.parseDateTime(endDate);
        return Days.daysBetween(begin, end).getDays();
    }

    // 活期日利率
    public BigDecimal qryPerDayCurrentRate() {
        return qryCurrentRate().divide(new BigDecimal("360.0"));
    }

    // 3个月定期日利率
    public BigDecimal qryPerDay3MonthRate() {
        return qry3MonthRate().divide(new BigDecimal("90.0"));
    }

    // 活期年利率
    public BigDecimal qryCurrentRate() {
        return qryRateByCode("AAA");
    }

    // 3个月定期年利率

    public BigDecimal qry3MonthRate() {
        return qryRateByCode("ACA");
    }

    private BigDecimal qryRateByCode(String rateCode) {
        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJmIrtMapper mapper = session.getMapper(HmfsJmIrtMapper.class);
            HmfsJmIrtExample example = new HmfsJmIrtExample();
            example.createCriteria().andIrtcodeEqualTo(rateCode);
            List<HmfsJmIrt> rates = mapper.selectByExample(example);
            return rates.get(0).getIrtvalue();
        } finally {
            if (session != null) session.close();
        }
    }
}
