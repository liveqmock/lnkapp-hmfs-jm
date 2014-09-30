package org.fbi.hmfsjm.repository.dao;

import org.fbi.hmfsjm.repository.model.HmfsJmAct;
import org.fbi.hmfsjm.repository.model.HmfsJmActExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface HmfsJmActMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int countByExample(HmfsJmActExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int deleteByExample(HmfsJmActExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int insert(HmfsJmAct record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int insertSelective(HmfsJmAct record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    List<HmfsJmAct> selectByExample(HmfsJmActExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    HmfsJmAct selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int updateByExampleSelective(@Param("record") HmfsJmAct record, @Param("example") HmfsJmActExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int updateByExample(@Param("record") HmfsJmAct record, @Param("example") HmfsJmActExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int updateByPrimaryKeySelective(HmfsJmAct record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int updateByPrimaryKey(HmfsJmAct record);
}