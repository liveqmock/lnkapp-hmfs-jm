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
 * ����
 */
public class ActirtService {
    MybatisManager manager = new MybatisManager();

    public int daysBetween(String beginDate, String endDate) {
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime begin = format.parseDateTime(beginDate);
        DateTime end = format.parseDateTime(endDate);
        return Days.daysBetween(begin, end).getDays();
    }

    // ����������
    public BigDecimal qryPerDayCurrentRate() {
        return qryCurrentRate().divide(new BigDecimal("360.0"));
    }

    // 3���¶���������
    public BigDecimal qryPerDay3MonthRate() {
        return qry3MonthRate().divide(new BigDecimal("90.0"));
    }

    // ����������
    public BigDecimal qryCurrentRate() {
        return qryRateByCode("AAA");
    }

    // 3���¶���������

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
