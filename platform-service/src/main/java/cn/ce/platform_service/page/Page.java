package cn.ce.platform_service.page;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @ClassName: Page
 * @Description: 分页类型
 * @author dingjia@300.cn 
 *
 * @param <T> 实例
 */
public class Page<T> implements Serializable {  
      
    /** 每页显示条数 */  
    private Integer pageSize = 8;  
  
    /** 当前页 */  
    private Integer currentPage = 1;  
  
    /** 总页数 */  
    private Integer totalPage = 1;  
  
    /** 查询到的总数据量 */  
    private Integer totalNumber = 0;  
  
    /** 数据集 */  
    private List<?> items;  
  
    public Page(int currentPaget, int totalNumbert, int pageSizet) {
        
        this.setCurrentPage(currentPaget);
        this.setTotalNumber(totalNumbert);
        this.setPageSize(pageSizet);
    }

    public Integer getPageSize() {  
  
        return pageSize;  
    }  
 
    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public List<?> getItems() {
        return items;
    }

    public void setItems(List<?> items) {
        this.items = items;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void build(List<?> titems) {  
        this.setItems(titems);  
        int count =  this.getTotalNumber();  
        int divisor = count / this.getPageSize();  
        int remainder = count % this.getPageSize();  
        this.setTotalPage(remainder == 0 ? divisor == 0 ? 1 : divisor : divisor + 1);  
    }
    
}
