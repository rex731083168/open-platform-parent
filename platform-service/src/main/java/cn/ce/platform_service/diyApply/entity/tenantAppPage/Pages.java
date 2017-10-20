/**
  * Copyright 2017 bejson.com 
  */
package cn.ce.platform_service.diyApply.entity.tenantAppPage;
import java.util.List;

/**
 * Auto-generated: 2017-10-20 11:35:58
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Pages {

    private Integer pageSize;
    private Integer currentPage;
    private Integer totalPages;
    private Integer totalCount;
    private List<List> list;
    private Params params;
    public void setPageSize(Integer pageSize) {
         this.pageSize = pageSize;
     }
     public Integer getPageSize() {
         return pageSize;
     }

    public void setCurrentPage(Integer currentPage) {
         this.currentPage = currentPage;
     }
     public Integer getCurrentPage() {
         return currentPage;
     }

    public void setTotalPages(Integer totalPages) {
         this.totalPages = totalPages;
     }
     public Integer getTotalPages() {
         return totalPages;
     }

    public void setTotalCount(Integer totalCount) {
         this.totalCount = totalCount;
     }
     public Integer getTotalCount() {
         return totalCount;
     }

    public void setList(List<List> list) {
         this.list = list;
     }
     public List<List> getList() {
         return list;
     }

    public void setParams(Params params) {
         this.params = params;
     }
     public Params getParams() {
         return params;
     }

}