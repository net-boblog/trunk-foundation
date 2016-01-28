package com.tumbleweed.platform.trunk.base.model;

/**
 * Created by mylover on 1/25/16.
 */
public abstract class DBRecord {
    private int totalCount;
    private String startDate;
    private String endDate;
    private double sumFee;

    public DBRecord() {
    }

    public double getSumFee() {
        return this.sumFee;
    }

    public void setSumFee(double sumFee) {
        this.sumFee = sumFee;
    }

    public abstract String getRecord();

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
