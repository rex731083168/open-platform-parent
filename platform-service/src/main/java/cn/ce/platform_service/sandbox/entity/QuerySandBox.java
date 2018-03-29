package cn.ce.platform_service.sandbox.entity;

import cn.ce.platform_service.util.PageValidateUtil;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年3月23日
*/
public class QuerySandBox {

	private String name;
	
	private String boxName;
	
	private int state;
	
	private boolean isDelete;
	
	private Integer currentPage = 1;
	
	private Integer pageSize = 10;
	
	private Integer startNum = 0; // 分页起始下标

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBoxName() {
		return boxName;
	}

	public void setBoxName(String boxName) {
		this.boxName = boxName;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
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

