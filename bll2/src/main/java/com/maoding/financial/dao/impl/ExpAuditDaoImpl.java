package com.maoding.financial.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.financial.dao.ExpAuditDao;
import com.maoding.financial.dto.AuditDTO;
import com.maoding.financial.dto.ExpAuditDTO;
import com.maoding.financial.dto.ExpMainDTO;
import com.maoding.financial.dto.ExpMainDataDTO;
import com.maoding.financial.entity.ExpAuditEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpAuditDaoImpl
 * 描    述 : 报销审核DaoImpl
 * 作    者 : LY
 * 日    期 : 2016/7/26-15:48
 */

@Service("expAuditDao")
public class ExpAuditDaoImpl extends GenericDao<ExpAuditEntity> implements ExpAuditDao{

    /**
     * 方法描述：根据报销主表Id把之前最新审核该为"N"
     * 作   者：LY
     * 日   期：2016/7/27 10:25
     * @param mainId(报销主表Id)
     * @return
     *
     */
    public int updateIsNewByMainId(String mainId){
        return this.sqlSession.update("ExpAuditEntityMapper.updateIsNewByMainId", mainId);
    }

    /**
     * 方法描述：根据报销主表Id查询最新审核为"Y"
     * 作   者：LY
     * 日   期：2016/7/27 10:37
     * @param  mainId(报销主表Id)
     * @return
     *
     */
    public List<ExpAuditEntity> selectByMainId(String mainId){
        return this.sqlSession.selectList("ExpAuditEntityMapper.selectByMainId", mainId);
    }

    @Override
    public ExpAuditEntity getLastAuditByMainId(String mainId) {
        List<ExpAuditEntity> list = this.selectByMainId(mainId);
        if(!CollectionUtils.isEmpty(list)){
            return list.get(list.size()-1);
        }
        return null;
    }
    /**
     * 方法描述：根据报销主表Id更新审批状态
     * 作   者：LY
     * 日   期：2016/7/29 11:49
     * @param
     * @return
     *
     */
    public int updateByMainId(ExpAuditEntity auditEntity){
        return this.sqlSession.update("ExpAuditEntityMapper.updateByMainId", auditEntity);
    }

    /**
     * 查询审批人
     * @param id
     * @return
     */
    public ExpAuditDTO selectAuditPersonByMainId(String id){
        return this.sqlSession.selectOne("ExpAuditEntityMapper.selectAuditPersonByMainId", id);
    }

    /**
     * 方法描述：转移审批人
     * 作   者：LY
     * 日   期：2016/8/1 15:29
     * @param
     *
     */
    public int transAuditPer(ExpAuditEntity auditEntity){
        return this.sqlSession.update("ExpAuditEntityMapper.transAuditPer", auditEntity);
    }

    /**
     * 方法描述：根据报销主表id查询审批激励
     * 作   者：LY
     * 日   期：2016/8/2 15:35
     * @param map 报销主表id
     *
     */
    public List<ExpMainDataDTO> selectAuditDetailByMainId(Map<String,Object> map){
        return this.sqlSession.selectList("ExpAuditEntityMapper.selectAuditDetailByMainId", map);
    }

    @Override
    public List<AuditDTO> selectAuditByMainId(Map<String, Object> map) {
        return this.sqlSession.selectList("ExpAuditEntityMapper.selectAuditByMainId", map);
    }


    @Override
    public List<ExpAuditEntity> selectByParam(Map<String, Object> mapParam){
        mapParam.put("isNew","Y");
        return sqlSession.selectList("ExpAuditEntityMapper.selectByParam", mapParam);
    }

    @Override
    public ExpAuditEntity selectLastRecallAudit(String mainId) {
        return sqlSession.selectOne("ExpAuditEntityMapper.selectLastRecallAudit", mainId);
    }


    @Override
    public List<ExpAuditEntity> getMyAudit(Map<String, Object> mapParam) {
        return sqlSession.selectList("ExpAuditEntityMapper.selectByParam", mapParam);
    }
}
