/**
  * Copyright 2017 bejson.com 
  */
package cn.ce.platform_service.diyApply.entity.tenantAppPage;
import java.util.List;

/**
 * Auto-generated: 2017-10-19 19:32:56
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Pages {

    private int pageSize;
    private int currentPage;
    private int totalPages;
    private int totalCount;
    private List<List> list;
    private Params params;
    public void setPageSize(int pageSize) {
         this.pageSize = pageSize;
     }
     public int getPageSize() {
         return pageSize;
     }

    public void setCurrentPage(int currentPage) {
         this.currentPage = currentPage;
     }
     public int getCurrentPage() {
         return currentPage;
     }

    public void setTotalPages(int totalPages) {
         this.totalPages = totalPages;
     }
     public int getTotalPages() {
         return totalPages;
     }

    public void setTotalCount(int totalCount) {
         this.totalCount = totalCount;
     }
     public int getTotalCount() {
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