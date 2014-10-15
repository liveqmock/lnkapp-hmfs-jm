package org.fbi.hmfsjm.online.service;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.fbi.hmfsjm.enums.BillStsFlag;
import org.fbi.hmfsjm.enums.DrawQryStatus;
import org.fbi.hmfsjm.enums.RefundQryStatus;
import org.fbi.hmfsjm.helper.ObjectFieldsCopier;
import org.fbi.hmfsjm.repository.MybatisManager;
import org.fbi.hmfsjm.repository.dao.HmfsJmDrawMapper;
import org.fbi.hmfsjm.repository.model.HmfsJmDraw;
import org.fbi.hmfsjm.repository.model.HmfsJmDrawExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 支取单处理
 */
public class DrawService {
    private static final Logger logger = LoggerFactory.getLogger(DrawService.class);
    MybatisManager manager = new MybatisManager();

    // 按单号查询支取单
    public HmfsJmDraw qryDrawByNo(String drawNo) {
        SqlSession session = null;

        try {
            session = manager.getSessionFactory().openSession();
            HmfsJmDrawMapper mapper = session.getMapper(HmfsJmDrawMapper.class);
            HmfsJmDrawExample example = new HmfsJmDrawExample();
            example.createCriteria().andBillnoEqualTo(drawNo);
            List<HmfsJmDraw> draws = mapper.selectByExample(example);
            return (draws.size() > 0) ? draws.get(0) : null;
        } finally {
            if (session != null) session.close();
        }
    }

    // 保存缴款单，已存在则更新
    public void saveDrawBills(List<HmfsJmDraw> draws, String billNo) throws IllegalAccessException {
        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJmDrawMapper mapper = session.getMapper(HmfsJmDrawMapper.class);
            HmfsJmDrawExample example = new HmfsJmDrawExample();
            example.createCriteria().andBillnoEqualTo(billNo);
            mapper.deleteByExample(example);
            for (HmfsJmDraw record : draws) {
                mapper.insert(record);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
            String errmsg = e.getMessage();
            if (StringUtils.isEmpty(errmsg)) {
                throw new RuntimeException("支取单保存失败");
            } else
                throw new RuntimeException(errmsg);
        } finally {
            if (session != null) session.close();
        }
    }
}
