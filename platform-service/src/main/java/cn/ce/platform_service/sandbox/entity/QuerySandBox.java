package cn.ce.platform_service.sandbox.entity;

import cn.ce.platform_service.util.PageValidateUtil;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年3月23日
*/
public class QuerySandBox {

	private String name;
	
	private Integer state;
	
	private String createState;
	
	private Integer currentPage = 1;
	
	private Integer pageSize = 10;
	
	private Integer startNum = 0; // 分页起始下标

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getCreateState() {
		return createState;
	}

	public void setCreateState(String createState) {
		this.createState = createState;
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

