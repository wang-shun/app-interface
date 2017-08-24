package com.maoding.project.service.impl;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.ResponseBean;
import com.maoding.core.common.service.UploadService;
import com.maoding.core.constant.NetFileType;
import com.maoding.core.util.HttpUtils;
import com.maoding.core.util.JsonUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.entity.CompanyEntity;
import com.maoding.project.dao.ProjectSkyDriverDao;
import com.maoding.project.dto.FileDTO;
import com.maoding.project.dto.ProjectSkyDriveDTO;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.project.entity.ProjectSkyDriveEntity;
import com.maoding.project.service.ProjectSkyDriverService;
import com.maoding.task.entity.ProjectTaskEntity;
import com.maoding.v2.project.dto.ProjectSkyDriveListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectSkyDriverServiceImpl
 * 类描述：项目文件磁盘
 * 作    者：MaoSF
 * 日    期：2016/12/18 16:50
 */
@Service("projectSkyDriverService")
public class ProjectSkyDriverServiceImpl extends GenericService<ProjectSkyDriveEntity>  implements ProjectSkyDriverService {

	@Autowired
	private ProjectSkyDriverDao projectSkyDriverDao;

	@Autowired
	private CompanyDao companyDao;


	@Autowired
	private UploadService uploadService;//上传

	@Value("${fastdfs.url}")
	protected String fastdfsUrl;


	@Value("${upload.url}")
	protected String uploadUrl;


	@Value("${company}")
	private String companyUrl;

	@Value("${server.url}")
	protected String serverUrl;

	@Override
	public ResponseBean saveOrUpdateFileMaster(ProjectSkyDriveDTO dto) throws Exception {

		//判断是否存在
		ProjectSkyDriveEntity isExist = this.projectSkyDriverDao.getSkyDriveByPidAndFileName(dto.getPid(),dto.getFileName(),dto.getProjectId());
		if(StringUtil.isNullOrEmpty(dto.getId())){//新增
			//判断是否存在
			if(isExist!=null){
				if(isExist.getType()==0)//如果是文件夹，则返回错误信息
				{
					return  ResponseBean.responseError(dto.getFileName()+"已经存在");
				}else {//如果是文件
					//删除原有的文件
					uploadService.delFileByFdfs(isExist.getFileGroup()+"/"+isExist.getFilePath());
					//修改文件路径及大小
					isExist.setFilePath(dto.getFilePath());
					isExist.setFileGroup(dto.getFileGroup());
					isExist.setUpdateBy(dto.getAccountId());
					isExist.setFileSize(dto.getFileSize());
					this.projectSkyDriverDao.updateById(isExist);
				}
			}else {//如果不存在，直接添加
				dto.setId(StringUtil.buildUUID());
				if(StringUtil.isNullOrEmpty(dto.getPid())){
					dto.setSkyDrivePath(dto.getId());
				}else {
					ProjectSkyDriveEntity parent = projectSkyDriverDao.selectById(dto.getPid());
					if(parent!=null)
					{
						dto.setSkyDrivePath(parent.getSkyDrivePath()+"-"+dto.getId());
					}
				}
				ProjectSkyDriveEntity entity = new ProjectSkyDriveEntity();
				BaseDTO.copyFields(dto,entity);
				entity.setCreateBy(dto.getAccountId());
				entity.setUpdateBy(dto.getAccountId());
				projectSkyDriverDao.insert(entity);

			}
		}else {//修改
			if(isExist!=null&&!isExist.getId().equals(dto.getId())){
				return  ResponseBean.responseError(dto.getFileName()+"已经存在");
			}
			ProjectSkyDriveEntity entity = new ProjectSkyDriveEntity();
			BaseDTO.copyFields(dto,entity);
			entity.setUpdateBy(dto.getAccountId());
			this.projectSkyDriverDao.updateById(entity);
		}
		return ResponseBean.responseSuccess("保存成功");
	}


	/**
	 * 方法描述：文件上传
	 * 作   者：MaoSF
	 * 日   期：2016/12/26 19:37
	 */
	public ResponseBean uploadFile(MultipartFile imgObj,String pid,String projectId,String companyId,String userId) throws Exception {
		Map<String, Object> resultObj = new HashMap<String, Object>();
		resultObj.put("imgObj",imgObj);
		String result = HttpUtils.sendPost(uploadUrl,resultObj,false);
		Map<String, Object> resultMap = JsonUtils.json2map(result);
		String fileName = new String(imgObj.getOriginalFilename().getBytes("ISO-8859-1"), "UTF-8");
		if(resultMap.get("fastdfsPath")!=null){
			String filePath=resultMap.get("fastdfsPath").toString();
			ProjectSkyDriveEntity entity = this.projectSkyDriverDao.getSkyDriveByPidAndFileName(pid,fileName,projectId);
			if(entity!=null){
				String orginalFilePath = entity.getFileGroup()+"/"+entity.getFilePath();
				entity.setFilePath(filePath);
				entity.setUpdateBy(userId);
				this.projectSkyDriverDao.updateById(entity);
				//删除原有的文件
				uploadService.delFileByFdfs(orginalFilePath);
			}else {
				ProjectSkyDriveDTO dto = new ProjectSkyDriveDTO();
				dto.setFilePath(filePath);
				dto.setProjectId(projectId);
				dto.setPid(pid);
				dto.setFileName(fileName);
				dto.setType(1);
				dto.setCompanyId(companyId);
				dto.setFileSize(imgObj.getSize());
				dto.setAccountId(userId);
				this.saveOrUpdateFileMaster(dto);
			}
			FileDTO dto = new FileDTO();
			dto.setFileName(fileName);
			dto.setFilePath(filePath);
			dto.setFastdfsUrl(fastdfsUrl);
			return ResponseBean.responseSuccess("上传成功").addData("file",dto);
		}
		return ResponseBean.responseError("上传失败");
	}

	/**
	 * 方法描述：删除文件或文件夹（单个删除、若文件夹下有其他的文件夹或文件，不可删除）
	 * 作者：MaoSF
	 * 日期：2016/12/18
	 */
	@Override
	public ResponseBean deleteSysDrive(String id,String accountId) throws Exception {
		ProjectSkyDriveEntity entity = this.projectSkyDriverDao.selectById(id);
		if(!accountId.equals(entity.getCreateBy())){
			return ResponseBean.responseError("您无权限删除");
		}
		if(entity!=null){
			if(1==entity.getType()){
				//删除原有的文件
				uploadService.delFileByFdfs(entity.getFileGroup()+"/"+entity.getFilePath());
				this.projectSkyDriverDao.deleteById(id);
			}else {
				//查询是否有子文件
				List<ProjectSkyDriveEntity> list = this.projectSkyDriverDao.getSkyDriveByPid(entity.getId(),entity.getProjectId());
				if(CollectionUtils.isEmpty(list)){
					this.projectSkyDriverDao.deleteById(id);
				}else {
					return 	ResponseBean.responseError("请删除子文件、文件夹再删除");
				}
			}
		}
		return ResponseBean.responseSuccess("删除成功");
	}


	/**
	 * 方法描述：查询文件
	 * 作者：MaoSF
	 * 日期：2016/12/18
	 */
	@Override
	public List<ProjectSkyDriveEntity> getNetFileByParam(Map<String,Object> map){
		return this.projectSkyDriverDao.getNetFileByParam(map);
	}

	/**
	 * 方法描述：查询文件(包含子文件个数)
	 * 作者：MaoSF
	 * 日期：2016/12/18
	 */
	@Override
	public ResponseBean getSkyDrive(Map<String, Object> map) {
		 List<ProjectSkyDriveListDTO> list = this.projectSkyDriverDao.getSkyDrive(map);
		String companyId = (String)map.get("appOrgId");
		String accountId = (String)map.get("accountId");
		for(ProjectSkyDriveListDTO entity:list){
			if(accountId.equals(entity.getCreateBy()) && companyId.equals(entity.getCompanyId())){
				entity.setEditFlag(1);
			}
		}
		Map<String,Object> returnData= new HashMap<>();
		returnData.put("skyDriveList",list);
		returnData.put("uploadFlag",0);
		if(null!=map.get("pid")) {
			ProjectSkyDriveEntity parent = this.selectById(map.get("pid"));
			if (!StringUtil.isNullOrEmpty(parent.getSkyDrivePath())) {
				String[] path = parent.getSkyDrivePath().split("-");
				if (path.length > 1) {
					String rootId = path[0];
					ProjectSkyDriveEntity root = this.selectById(rootId);
					if ("设计成果".equals(root.getFileName())) {
						if (companyId.equals(parent.getCompanyId())) {
							returnData.put("uploadFlag", 1);
						}
					} else {
						returnData.put("uploadFlag", 1);
					}
				}
			}
		}
		returnData.put("fastdfsUrl",fastdfsUrl);
		return ResponseBean.responseSuccess().setData(returnData);
	}


	@Override
	public void createProjectFile(ProjectEntity projectEntity){
		List<String> fileList= new ArrayList<String>();
		fileList.add("设计依据");
		fileList.add("设计成果");
		int seq0 = 1;
		for(String fileName:fileList){
			ProjectSkyDriveEntity projectSkyDriveEntity=new ProjectSkyDriveEntity();
			projectSkyDriveEntity.setId(StringUtil.buildUUID());
			projectSkyDriveEntity.setCompanyId(projectEntity.getCompanyId());
			projectSkyDriveEntity.setProjectId(projectEntity.getId());
			projectSkyDriveEntity.setSkyDrivePath(projectSkyDriveEntity.getId());
			projectSkyDriveEntity.setIsCustomize(1);
			projectSkyDriveEntity.setType(0);
			projectSkyDriveEntity.setFileName(fileName);
			projectSkyDriveEntity.setParam4(seq0++);
			projectSkyDriverDao.insert(projectSkyDriveEntity);
			List<String> fileList2= new ArrayList<String>();
			if("设计成果".equals(fileName)){
			//	fileList2.add("项目总归档");
//				CompanyEntity companyEntity = this.companyDao.selectById(projectEntity.getCompanyId());
//				if(companyEntity!=null){
//					fileList2.add(companyEntity.getCompanyName());//立项方公司公司名
//				}
			}
			else{
				fileList2.add("基础资料");
				fileList2.add("政府批文");
				fileList2.add("设计任务书");
				fileList2.add("往来函件");
				fileList2.add("会议纪要");
			}
			int seq = 1;
			for(String fileName2:fileList2){
				ProjectSkyDriveEntity projectSkyDrivey=new ProjectSkyDriveEntity();
				projectSkyDrivey.setId(StringUtil.buildUUID());
				projectSkyDrivey.setCompanyId(projectEntity.getCompanyId());
				projectSkyDrivey.setProjectId(projectEntity.getId());
				projectSkyDrivey.setPid(projectSkyDriveEntity.getId());
				projectSkyDrivey.setSkyDrivePath(projectSkyDriveEntity.getId()+"-"+projectSkyDrivey.getId());
				projectSkyDrivey.setIsCustomize(1);
				projectSkyDrivey.setType(0);
				projectSkyDrivey.setFileName(fileName2);
				projectSkyDrivey.setParam4(seq++);
				projectSkyDriverDao.insert(projectSkyDrivey);
			}
		}
	}


	/**
	 * 方法描述：签发的时候，给该公司创建默认的文件（设计成果中的文件）
	 * 作者：MaoSF
	 * 日期：2017/4/12
	 */
	@Override
	public void createFileMasterForIssueTask(String projectId, String companyId) {
		List<ProjectSkyDriveEntity> list = this.projectSkyDriverDao.getProjectSkyByCompanyId(projectId,companyId);
		if (CollectionUtils.isEmpty(list)){
			ProjectSkyDriveEntity parent = this.projectSkyDriverDao.getSkyDriveByPidAndFileName(null,"设计成果",projectId);
			CompanyEntity companyEntity = this.companyDao.selectById(companyId);
			if(parent!=null && companyEntity!=null){
				ProjectSkyDriveEntity projectSkyDrivey = new ProjectSkyDriveEntity();
				projectSkyDrivey.setId(StringUtil.buildUUID());
				projectSkyDrivey.setCompanyId(companyId);
				projectSkyDrivey.setProjectId(projectId);
				projectSkyDrivey.setPid(parent.getId());
				projectSkyDrivey.setSkyDrivePath(parent.getId() + "-" + projectSkyDrivey.getId());
				projectSkyDrivey.setIsCustomize(1);
				projectSkyDrivey.setType(0);
				projectSkyDrivey.setFileName(companyEntity.getCompanyName());
				projectSkyDriverDao.insert(projectSkyDrivey);
			}
		}
	}

	/**
	 * 方法描述：经营签发的任务名称默认到文档库中（设计成果中的文件）
	 * 作者：MaoSF
	 * 日期：2017/4/12
	 */
	@Override
	public void createFileMasterForTask(ProjectTaskEntity taskEntity) {
		ProjectSkyDriveEntity parent = null;
		String projectId = taskEntity.getProjectId();
		if(StringUtil.isNullOrEmpty(taskEntity.getTaskPid())){
			parent = this.projectSkyDriverDao.getSkyDriveByPidAndFileName(null,"设计成果",projectId);
		}else {
			parent = this.projectSkyDriverDao.getSkyDriveByTaskId(taskEntity.getTaskPid());
		}
		if(parent!=null){
			ProjectSkyDriveEntity projectSkyDrivey = new ProjectSkyDriveEntity();
			projectSkyDrivey.setId(StringUtil.buildUUID());
			projectSkyDrivey.setCompanyId(taskEntity.getCompanyId());
			projectSkyDrivey.setProjectId(projectId);
			projectSkyDrivey.setPid(parent.getId());
			projectSkyDrivey.setSkyDrivePath(parent.getSkyDrivePath() + "-" + projectSkyDrivey.getId());
			projectSkyDrivey.setIsCustomize(1);
			projectSkyDrivey.setType(0);
			projectSkyDrivey.setFileName(taskEntity.getTaskName());
			projectSkyDrivey.setTaskId(taskEntity.getId());
			projectSkyDrivey.setParam4(taskEntity.getSeq());
			//判断是否存在
			ProjectSkyDriveEntity isExist = this.projectSkyDriverDao.getSkyDriveByPidAndFileName(projectSkyDrivey.getPid(), projectSkyDrivey.getFileName(), projectSkyDrivey.getProjectId());
			if(isExist==null){
				this.projectSkyDriverDao.insert(projectSkyDrivey);
			}
		}
	}


	/**
	 * 方法描述：根据项目id，父级id，文件名查询
	 * 作者：MaoSF
	 * 日期：2016/12/18
	 */
	@Override
	public ProjectSkyDriveEntity getSkyDriveByTaskId(String taskId) {
		return this.projectSkyDriverDao.getSkyDriveByTaskId(taskId);
	}

	/**
	 * 方法描述：
	 * 作者：MaoSF
	 * 日期：2017/4/21
	 */
	@Override
	public ResponseBean updateSkyDriveStatus(String taskId, String accountId) throws Exception {
		ProjectSkyDriveEntity driveEntity = this.getSkyDriveByTaskId(taskId);
		if(driveEntity!=null){
			Map<String ,Object> map = new HashMap<>();
			map.put("accountId",accountId);
			map.put("skyDrivePath",driveEntity.getId());
			this.projectSkyDriverDao.updateSkyDriveStatus(map);
		}
		return ResponseBean.responseSuccess("操作成功");
	}

	/**
	 * 方法描述：获取组织logo地址
	 * 作者：MaoSF
	 * 日期：2017/6/2
	 */
	@Override
	public String getCompanyLogo(String companyId) throws Exception {
		Map<String,Object> map = new HashMap<>();
		map.put("companyId",companyId);
		map.put("type", NetFileType.COMPANY_LOGO_ATTACH);
		return this.getFileUrl(map,true);
	}

	/**
	 * 方法描述：获取组织logo地址（不包含轮播图）
	 * 作者：MaoSF
	 * 日期：2017/6/2
	 */
	@Override
	public String getCompanyFileByType(String companyId, Integer type,boolean isHasFastdfsUrl) throws Exception {
		Map<String,Object> map = new HashMap<>();
		map.put("companyId",companyId);
		map.put("type",type);
		map.put("status","0");
		return this.getFileUrl(map,isHasFastdfsUrl);
	}

	/**
	 * 方法描述：生成公司二维码
	 * 作者：MaoSF
	 * 日期：2016/11/25
	 */
	@Override
	public String createCompanyQrcode(String companyId) throws Exception {
		String url = this.serverUrl+"iAdmin/sys/shareInvitation/"+companyId;
		String result = this.uploadService.createQrcode(url,this.companyUrl);
		//上传成功后，数据保存到数据库
		if(null!=result&&!"".equals(result)){
			ProjectSkyDriveEntity projectSkyDrive = new ProjectSkyDriveEntity();
			projectSkyDrive.setId(StringUtil.buildUUID());
			projectSkyDrive.setFileName("");
			projectSkyDrive.setType(7);
			projectSkyDrive.setFileGroup(result.substring(0,6));
			projectSkyDrive.setFilePath(result.substring(7));
			projectSkyDrive.setCompanyId(companyId);
			projectSkyDrive.setIsCustomize(0);
			projectSkyDrive.setStatus("0");
			projectSkyDrive.setCreateDate(new Date());
			projectSkyDriverDao.insert(projectSkyDrive);

			return result;
		}

		return null;
	}
	/**
	 * 方法描述：获取组织logo地址(完整的url地址)
	 * 作者：MaoSF
	 * 日期：2017/6/2
	 */
	@Override
	public ProjectSkyDriveEntity getProjectContractAttach(String projectId) throws Exception {
		Map<String,Object> map = new HashMap<>();
		map.put("projectId",projectId);
		map.put("type",NetFileType.PROJECT_CONTRACT_ATTACH);
		List<ProjectSkyDriveEntity> attachEntityList = this.projectSkyDriverDao.getNetFileByParam(map);
		if(!CollectionUtils.isEmpty(attachEntityList)){
			return attachEntityList.get(0);
		}
		return null;
	}


	/**
	 * 方法描述：获取单个文件的路径
	 * 作者：MaoSF
	 * 日期：2017/6/2
	 */
	private String getFileUrl(Map<String,Object> map,boolean isHasFastdfsUrl) {
		List<ProjectSkyDriveEntity> attachEntityList = this.projectSkyDriverDao.getNetFileByParam(map);
		if(!CollectionUtils.isEmpty(attachEntityList)){
			ProjectSkyDriveEntity logoAttach = attachEntityList.get(0);
			String filePath = logoAttach.getFileGroup()+"/"+logoAttach.getFilePath();
			if(isHasFastdfsUrl){
				filePath = this.fastdfsUrl+filePath;
			}
			return filePath;
		}
		return null;
	}
}