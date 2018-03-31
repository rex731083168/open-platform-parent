package cn.ce.platform_service.gateway.entity;

import cn.ce.platform_service.util.PageValidateUtil;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年3月30日
*/
public class QuerySaasEntity {

	private String saas_id;
	
	private String resource_type;
	
	private String target_url;
	
	private String sandbox_id;
	
	private Integer currentPage = 1;
	
	private Integer pageSize = 10;
	
	private Integer startNum = 0; // 分页起始下标
	
	public String getSaas_id() {
		return saas_id;
	}

	public void setSaas_id(String saas_id) {
		this.saas_id = saas_id;
	}

	public String getResource_type() {
		return resource_type;
	}

	public void setResource_type(String resource_type) {
		this.resource_type = resource_type;
	}

	public String getTarget_url() {
		return target_url;
	}

	public void setTarget_url(String target_url) {
		this.target_url = target_url;
	}

	public String getSandbox_id() {
		return sandbox_id;
	}

	public void setSandbox_id(String sandbox_id) {
		this.sandbox_id = sandbox_id;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage =PageValidateUtil.checkCurrentPage(currentPage);
		buildStartNum();
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = PageValidateUtil.checkPageSize(pageSize);
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

