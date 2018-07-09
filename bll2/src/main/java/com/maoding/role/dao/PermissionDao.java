package com.maoding.role.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.role.dto.PermissionDTO;
import com.maoding.role.entity.PermissionEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：PermissionService
 * 类描述：权限表（dao）
 * 作    者：wrb
 * 日    期：2016年11月2日-上午11:38:47
 */
public interface PermissionDao extends BaseDao<PermissionEntity> {

    /**
     * 方法描述：获取当前角色下下所有的权限
     * 作者：MaoSF
     * 日期：2016/11/2
     * @param:roleId，companyId
     */
    List<PermissionEntity> getDefaultPermission();

    /**
     * 方法描述：角色-权限 只查询角色对应的权限-
     * 作者：MaoSF
     * 日期：2016/11/2
     * @param:roleId，companyId
     */
    List<PermissionDTO> getPermissionByRole2(Map<String, Object> map);

    /**
     * 方法描述：角色-权限 只userId的权限
     * 作者：MaoSF
     * 日期：2016/11/2
     * @param:roleId，companyId
     */
    List<PermissionDTO> getPermissionByRoleAndUserForApp(Map<String, Object> map);

    /**
     * 方法描述：获取当前角色下下所有的权限Code
     * 作者：MaoSF
     * 日期：2016/11/2
     * @param:userId，companyId
     */
    String getPermissionCodeByUserId(Map<String, Object> map);

    /**
     *  查询某个用户是否具有某些权限
     */
    int getCompanyUserIsHasPermission(Map<String,Object> map);

}