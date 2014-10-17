package org.fbi.hmfsjm.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.fbi.hmfsjm.repository.model.HmfsJmInterest;
import org.fbi.hmfsjm.repository.model.HmfsJmInterestExample;

public interface HmfsJmInterestMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_INTEREST
     *
     * @mbggenerated Thu Oct 16 14:52:25 CST 2014
     */
    int countByExample(HmfsJmInterestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_INTEREST
     *
     * @mbggenerated Thu Oct 16 14:52:25 CST 2014
     */
    int deleteByExample(HmfsJmInterestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_INTEREST
     *
     * @mbggenerated Thu Oct 16 14:52:25 CST 2014
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_INTEREST
     *
     * @mbggenerated Thu Oct 16 14:52:25 CST 2014
     */
    int insert(HmfsJmInterest record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_INTEREST
     *
     * @mbggenerated Thu Oct 16 14:52:25 CST 2014
     */
    int insertSelective(HmfsJmInterest record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_INTEREST
     *
     * @mbggenerated Thu Oct 16 14:52:25 CST 2014
     */
    List<HmfsJmInterest> selectByExample(HmfsJmInterestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_INTEREST
     *
     * @mbggenerated Thu Oct 16 14:52:25 CST 2014
     */
    HmfsJmInterest selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_INTEREST
     *
     * @mbggenerated Thu Oct 16 14:52:25 CST 2014
     */
    int updateByExampleSelective(@Param("record") HmfsJmInterest record, @Param("example") HmfsJmInterestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_INTEREST
     *
     * @mbggenerated Thu Oct 16 14:52:25 CST 2014
     */
    int updateByExample(@Param("record") HmfsJmInterest record, @Param("example") HmfsJmInterestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_INTEREST
     *
     * @mbggenerated Thu Oct 16 14:52:25 CST 2014
     */
    int updateByPrimaryKeySelective(HmfsJmInterest record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_INTEREST
     *
     * @mbggenerated Thu Oct 16 14:52:25 CST 2014
     */
    int updateByPrimaryKey(HmfsJmInterest record);
}