package org.fbi.hmfsjm.repository;

import org.apache.ibatis.session.SqlSessionFactory;

import java.io.Serializable;

/**
 *   MybatisManager
 */
public class MybatisManager implements Serializable {

    private SqlSessionFactory sessionFactory;

    public MybatisManager(){
         sessionFactory = MybatisFactory.ORACLE.getInstance();
    }

    public SqlSessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
