package com.maoding.projectprocess.controller;

import com.maoding.core.bean.ResponseBean;
import com.maoding.project.dto.ProjectProcessDTO;
import com.maoding.project.service.ProjectProcessService;
import com.maoding.system.controller.BaseWSController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ProjectProcessController
 * 描    述 : 流程管理Controller
 * 作    者 : ChenZhuJie
 * 日    期 : 2016/8/8-11:04
 */

@Controller
@RequestMapping("projectProcess")
public class ProjectProcessController extends BaseWSController {

    @Autowired
    private ProjectProcessService projectProcessService;


    /**
     * 方法描述：创建更新流程（设定设计人）
     * 作者：MaoSF
     * 日期：2017/1/4
     */
    @RequestMapping("/saveTaskProcess")
    @ResponseBody
    public ResponseBean saveTaskProcess(@RequestBody ProjectProcessDTO dto ) throws Exception {
        return projectProcessService.saveOrUpdateProcess(dto);
    }


}
