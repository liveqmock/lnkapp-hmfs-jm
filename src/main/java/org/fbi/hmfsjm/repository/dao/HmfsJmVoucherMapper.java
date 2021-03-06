package org.fbi.hmfsjm.repository.dao;

import org.fbi.hmfsjm.repository.model.HmfsJmVoucher;
import org.fbi.hmfsjm.repository.model.HmfsJmVoucherExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HmfsJmVoucherMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int countByExample(HmfsJmVoucherExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int deleteByExample(HmfsJmVoucherExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int insert(HmfsJmVoucher record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int insertSelective(HmfsJmVoucher record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    List<HmfsJmVoucher> selectByExample(HmfsJmVoucherExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    HmfsJmVoucher selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int updateByExampleSelective(@Param("record") HmfsJmVoucher record, @Param("example") HmfsJmVoucherExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int updateByExample(@Param("record") HmfsJmVoucher record, @Param("example") HmfsJmVoucherExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int updateByPrimaryKeySelective(HmfsJmVoucher record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int updateByPrimaryKey(HmfsJmVoucher record);
}