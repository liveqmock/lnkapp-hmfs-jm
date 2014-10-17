package org.fbi.hmfsjm.repository.model;

import java.math.BigDecimal;

/**
 * Created by lenovo on 2014-10-17.
 */
public class HmTotalAct {
    private String hcnt;
    private String sumArea;
    private BigDecimal sumAmt;

    public String getHcnt() {
        return hcnt;
    }

    public void setHcnt(String hcnt) {
        this.hcnt = hcnt;
    }

    public String getSumArea() {
        return sumArea;
    }

    public void setSumArea(String sumArea) {
        this.sumArea = sumArea;
    }

    public BigDecimal getSumAmt() {
        return sumAmt;
    }

    public void setSumAmt(BigDecimal sumAmt) {
        this.sumAmt = sumAmt;
    }
}
