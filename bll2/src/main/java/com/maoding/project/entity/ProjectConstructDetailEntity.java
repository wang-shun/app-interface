package com.maoding.project.entity;

import com.maoding.core.base.entity.BaseEntity;

import java.util.Date;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectConstructDetailEntity
 * 类描述：建设单位项目联系人实体
 * 作    者：LY
 * 日    期：2016年7月19日-下午4:04:50
 */
public class ProjectConstructDetailEntity extends BaseEntity {

    /**
     * 建设单位id
     */
    private String constructId;

    /**
     * 联系人姓名
     */
    private String name;

    /**
     * 联系人电话
     */
    private String phone;

    /**
     * 联系人邮箱
     */
    private String email;

    /**
     * 排序
     */
    private String seq;

    /**
     * 项目Id
     */
    private String projectId;

    /**
     * 职位
     */
    private String position;

    public String getConstructId() {
        return constructId;
    }

    public void setConstructId(String constructId) {
        this.constructId = constructId == null ? null : constructId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq == null ? null : seq.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }
}