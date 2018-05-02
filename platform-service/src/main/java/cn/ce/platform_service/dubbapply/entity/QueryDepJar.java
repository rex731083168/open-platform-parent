package cn.ce.platform_service.dubbapply.entity;

import cn.ce.platform_service.util.PageValidateUtil;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年5月2日
*/
public class QueryDepJar {

	private String mainJarId;
	
	private String originalFileName;
	
	private Integer currentPage = 1;
	
	private Integer pageSize = 10;
	
	private Integer startNum = 0; // 分页起始下标

	public String getMainJarId() {
		return mainJarId;
	}

	public void setMainJarId(String mainJarId) {
		this.mainJarId = mainJarId;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage =PageValidateUtil.checkCurrentPage(currentPage);
		buildStartNum();
	}
	
	public void setOrgCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
		buildStartNum();
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = PageValidateUtil.checkPageSize(pageSize);
		buildStartNum();
	}
	
	public void setOrgPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		buildStartNum();
	}

	public Integer getStartNum() {
		return startNum;
	}

	public void setStartNum(Integer startNum) {
		this.startNum = startNum;
	}
	
	public void buildStartNum(){
		if(this.currentPage != null && this.currentPage > 0 
				&& this.pageSize != null && this.pageSize > 0){
			this.startNum = (this.currentPage-1)*this.pageSize;
		}
	}
}

