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

    private int pageSize;
    private int currentPage;
    private int totalPages;
    private int totalCount;
    private List<AppList> list;
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