package cn.ce.platform_service.page;

/**
 * 
 * @ClassName: PageContext
 * @Description: 分页上下文
 * @author dingjia@300.cn
 *
 */
public class PageContext {

    /** 当前页 */
	public static ThreadLocal<Integer> cuurentPage = new ThreadLocal<Integer>();
	/** 页大小  */
	public static ThreadLocal<Integer> pageSize = new ThreadLocal<Integer>();
	/** 默认当前页编号 */
	public static final int defualtCuurentPage = 1; 
	/** 默认页大小 */
	public static final int defualtPageSize = 8; 
	
	public static Integer getCuurentPage() {
		if (cuurentPage.get() == null) {
			return defualtCuurentPage;
		}
		return cuurentPage.get();
	}
	
	public static Integer getPageSize() {
		if (pageSize.get() == null) {
			return defualtPageSize;
		}
		return pageSize.get();
	}
	
	public static void setCuurentPage(Integer value) {
		if (value == null) {
			cuurentPage.set(defualtCuurentPage);
			return;
		}
		cuurentPage.set(value);
	}
	
	public static void setPageSize(Integer value) {
		if (value == null) {
			pageSize.set(defualtPageSize);
			return;
		}
		pageSize.set(value);
	}

	public static void init(Integer cuurentPage2, Integer pageSize2) {
		setCuurentPage(cuurentPage2);
		setPageSize(pageSize2);
	}
}
