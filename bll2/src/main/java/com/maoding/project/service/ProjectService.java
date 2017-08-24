package com.maoding.project.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.bean.ResponseBean;
import com.maoding.org.dto.ProjectUserDTO;
import com.maoding.project.dto.ProjectDTO;
import com.maoding.project.dto.ProjectDesignContentDTO;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.v2.project.dto.ProjectBaseDataDTO;
import com.maoding.v2.project.dto.V2ProjectTableDTO;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectService
 * 类描述：Service
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午5:28:54
 */
public interface ProjectService extends BaseService<ProjectEntity>{

    /**
     * 方法描述：v2项目列表
     * 作者：chenzhujie
     * 日期：2016/12/24
     */
    List<V2ProjectTableDTO> getV2ProjectList(Map<String, Object> param)throws Exception;

    /**
     * 方法描述：经营列表总数据（用于分页）
     * 作者：MaoSF
     * 日期：2016/7/29
     * @param:
     * @return:
     */
    int getProjectListByConditionCount(Map<String, Object> param) throws Exception;

    /**
     * 方法描述：删除项目（逻辑删除）
     * 作者：MaoSF
     * 日期：2016/8/4
     * @param:
     * @return:
     */
    AjaxMessage deleteProjectById(String id) throws Exception;

    /**
     * 方法描述：根据id查询项目信息
     * 作者：MaoSF
     * 日期：2016/7/28
     * @param:
     * @return:
     */
    ProjectDTO getProjectById(String id, String companyId, String userId) throws Exception;

    /**
     * 方法描述：根据id查询项目信息
     * 作者：MaoSF
     * 日期：2016/7/28
     * @param:
     * @return:
     */
    Map<String, Object> getProjectDetail(String id, String companyId, String userId) throws Exception;

    /**
     * 方法描述：根据companyId查询所有有效项目(我要报销 选择项目下拉框 )
     * 作   者：LY
     * 日   期：2016/8/8 14:38
     */
    List<ProjectDTO> getProjectListByCompanyId(Map<String, Object> param);


    /*********************************新的接口****************************************/


    /**
     * 方法描述：获取立项项目的基础数据（甲方和项目经营人）v2.0
     * 作者：MaoSF
     * 日期：2016/12/07
     * @param companyId
     * @param:
     * @return:
     */
    ProjectBaseDataDTO getAddProjectOfBaseData(String companyId) throws Exception;

    /**
     * 方法描述：查询项目相关数据
     * 作者：Chenzhujie
     * 日期：2016/12/13
     * @param:dto
     * @return:
     */
    Map<String, Object> getProjectsAbouts(Map<String, Object> param)throws Exception;


    /**
     * 方法描述：查询项目参与人员
     * 作者：chenzhujie
     * 日期：2016/12/24
     */
   // List<Map<String,Object>> getProjectParticipants(Map<String, Object> param) throws  Exception;

    /**
     * //立项人:1 经营负责人:2 项目负责人:3  任务负责人 4 流程人:5  去掉 exitList
     * 项目所有参与人员
     * @param
     * @return
     * @throws Exception
     */
    List<ProjectUserDTO> getProjectParticipantsList(String projectId) throws  Exception;


    /**
     * 方法描述：新增或修改项目
     * 作        者：TangY
     * 日        期：2016年7月20日-下午4:47:25
     * @param dto
     * @return
     */
    ResponseBean saveOrUpdateProjectNew(ProjectDTO dto) throws Exception;

    /**
     * 项目参与组织
     */
    List<Map<String,Object>> getProjectTaskCompany(String projectId)throws Exception;


    /**
     * 方法描述：（新建项目，任务签发给其他组织）发送消息给相关组织成员
     * 作者：MaoSF
     * 日期：2017/2/20
     * @param:id(项目id)
     * @return:
     */
    ResponseBean sendMsgToRelationCompanyUser(List<String> companyList);

    /**
     * 方法描述：获取项目菜单权限
     * 作者：MaoSF
     * 日期：2017/3/27
     * @param:
     * @return:
     */
    ResponseBean projectNavigationRoleInterface(Map<String, Object> map) throws Exception;


    /**
     * 方法描述：设计阶段添加与修改
     * 作者：MaoSF
     * 日期：2017/4/20
     * @param:
     * @return:
     */
    ResponseBean saveOrUpdateProjectDesign(ProjectDesignContentDTO designContentDTO) throws Exception;
}