/**
  * Copyright 2017 bejson.com 
  */
package cn.ce.platform_service.diyApply.entity.appsEntity;

import java.util.List;

/**
 * Auto-generated: 2017-10-13 14:32:39
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Data {

	private Integer pageSize;
	private Integer currentPage;
	private Integer totalPages;
	private Integer totalCount;
	private List<AppList> list;
	private Params params;

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public void setList(List<AppList> list) {
		this.list = list;
	}

	public List<AppList> getList() {
		return list;
	}

	public void setParams(Params params) {
		this.params = params;
	}

	public Params getParams() {
		return params;
	}

}