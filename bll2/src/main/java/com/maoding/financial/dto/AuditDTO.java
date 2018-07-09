package com.maoding.financial.dto;

import com.maoding.attach.dto.FilePathDTO;

import java.util.Date;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpAuditEntity
 * 描    述 : 审核DTO
 * 作    者 : MaoSF
 * 日    期 : 2016/7/26 15:14
 */
public class AuditDTO extends FilePathDTO {


    private String id;

    private String companyUserId;

    private String accountId;

    private String userName;

    /**
     * 是否最新审核 Y是 N否
     */
    private String isNew;

    /**
     * 报销主单id
     */
    private String mainId;

    /**
     * 审批状态(0:待审核，1:同意，2，退回,3:撤回,4:删除）
     */
    private String approveStatus;

    private String approveStatusName;

    /**
     * 审批日期
     */
    private Date approveDate;

    /**
     * 审核人id
     */
    private String auditPerson;

    /**
     * 审核人姓名
     */
    private String auditPersonName;

    /**
     * 审批意见
     */
    private String auditMessage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew == null ? null : isNew.trim();
    }

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId == null ? null : mainId.trim();
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus == null ? null : approveStatus.trim();
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson == null ? null : auditPerson.trim();
    }

    public String getAuditMessage() {
        return auditMessage;
    }

    public void setAuditMessage(String auditMessage) {
        this.auditMessage = auditMessage == null ? null : auditMessage.trim();
    }

    public String getAuditPersonName() {
        return auditPersonName;
    }

    public void setAuditPersonName(String auditPersonName) {
        this.auditPersonName = auditPersonName;
    }

    public String getApproveStatusName() {
        if ("0".equals(approveStatus)) {
            return "待审批";
        } else if ("1".equals(approveStatus)) {
            if ("Y".equals(isNew)) {
                return "已审批(完成)";
            } else {
                return "已审批";
            }
        } else if ("2".equals(approveStatus)) {
            return "退回";
        } else if ("3".equals(approveStatus)) {
            return "撤回";
        } else if ("4".equals(approveStatus)) {
            return "删除";
        } else if ("5".equals(approveStatus)) {
            return "审批中";
        }
        return "发起申请";
    }

    public void setApproveStatusName(String approveStatusName) {
        this.approveStatusName = approveStatusName;
    }
}