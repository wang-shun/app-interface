package com.maoding.core.base.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maoding.core.util.StringUtil;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：BaseDTO
 * 类描述：DTO基类
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:18:22
 */
@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseDTO implements Serializable {

    private String id;

       /**
     * app使用的token标示
     */
    private String token;

    private String appOrgId;

    /**
     * 账号id（当前用户账号id）
     */
    private String accountId;

    /**
     * 移动平台（android ,ios）
     */
    private String platform;

    /**
     * 请求接口版本号
     */
    private Integer interfaceVersion;

    @JsonProperty(value = "IMEI") //大写无效，需要赋值给小写
    private String imei;
    /**
     * 当前公司
     */
    private String currentCompanyId;

    /**
     * 当前人所在组织id
     */
    private String currentCompanyUserId;



    /** 字符串和布尔值互转 */
    public static Boolean toBoolean(String s){
        return (s != null) && "1".equals(s);
    }

    public static String toString(Boolean b){
        return ((b != null) && b) ? "1" : "0";
    }

    /**
     * entity对象和dto对象互相复制属性，包括基类的属性
     */
    public static void copyFields(Object source, Object dest) throws Exception {
    	if (source == null || dest == null)
    		return;
    	
        BeanInfo sourceBeanInfo = Introspector.getBeanInfo(source.getClass(), Object.class);
        PropertyDescriptor[] sourceProperties = sourceBeanInfo.getPropertyDescriptors();
        BeanInfo destBeanInfo = Introspector.getBeanInfo(dest.getClass(), Object.class);
        PropertyDescriptor[] destProperties = destBeanInfo.getPropertyDescriptors();

        for (int i = 0; i < sourceProperties.length; i++) {

            for (int j = 0; j < destProperties.length; j++) {

                if (sourceProperties[i].getName().equals(destProperties[j].getName())) {
                    //调用source的getter方法和dest的setter方法
                    destProperties[j].getWriteMethod().invoke(dest, sourceProperties[i].getReadMethod().invoke(source));
                    break;
                }
            }
        }
    }

    /**
     * 根据entityList复制dtoList
     */
    public static List copyFields(List sourceList, Class destClass) throws Exception {
        List destList = new ArrayList();
        for (Object source : sourceList) {
            Object dest = destClass.newInstance();
            copyFields(source, dest);
            destList.add(dest);
        }
        return destList;
    }

    /**
     * 根据entityList复制dtoList
     */
    public static Object copyFields(Object source, Class destClass) throws Exception {
        Object dest = destClass.newInstance();
        copyFields(source, dest);
        return dest;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppOrgId() {
        if(!StringUtil.isNullOrEmpty(currentCompanyId) && StringUtil.isNullOrEmpty(appOrgId)){
            appOrgId = currentCompanyId;
        }
        return appOrgId;
    }

    public void setAppOrgId(String appOrgId) {
        this.appOrgId = appOrgId;
    }

    public String getCurrentCompanyId() {
        if(!StringUtil.isNullOrEmpty(appOrgId) && StringUtil.isNullOrEmpty(currentCompanyId)){
            currentCompanyId = appOrgId;
        }
        return currentCompanyId;
    }

    public void setCurrentCompanyId(String currentCompanyId) {
        this.currentCompanyId = currentCompanyId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getIMEI() {
        return imei;
    }

    public void setIMEI(String imei) {
        this.imei = imei;
    }

    public Integer getInterfaceVersion() {
        return interfaceVersion;
    }

    public void setInterfaceVersion(Integer interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
    }

    public String getCurrentCompanyUserId() {
        return currentCompanyUserId;
    }

    public void setCurrentCompanyUserId(String currentCompanyUserId) {
        this.currentCompanyUserId = currentCompanyUserId;
    }
}
