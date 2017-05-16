package io.renren.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import static io.renren.utils.ShiroUtils.getUserId;

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
    private String  userID;
    private String statDate;
    private String seeTimes;
    private String exportTimes;
    private String editTimes;
    private String deleteTimes;


    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }

    public String getSeeTimes() {
        return seeTimes;
    }

    public void setSeeTimes(String seeTimes) {
        this.seeTimes = seeTimes;
    }

    public String getExportTimes() {
        return exportTimes;
    }

    public void setExportTimes(String exportTimes) {
        this.exportTimes = exportTimes;
    }

    public String getEditTimes() {
        return editTimes;
    }

    public void setEditTimes(String editTimes) {
        this.editTimes = editTimes;
    }

    public String getDeleteTimes() {
        return deleteTimes;
    }

    public void setDeleteTimes(String deleteTimes) {
        this.deleteTimes = deleteTimes;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

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
