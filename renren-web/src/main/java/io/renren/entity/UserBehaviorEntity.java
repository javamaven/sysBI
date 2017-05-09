package io.renren.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/4 0004.
 */
public class UserBehaviorEntity implements Serializable {
    private int ID;
    private String userName;
    private Date createTime;
    private String TYPE;
    private String reportType;
    private String EXECSQL;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getEXECSQL() {
        return EXECSQL;
    }

    public void setEXECSQL(String EXECSQL) {
        this.EXECSQL = EXECSQL;
    }
}
