package cn.ce.platform_service.core;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 *
 * @Title: BathUpdateOptions.java
 * @Description 批量更新用来传递数据的对象
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月12日 time下午2:08:19
 *
 **/
public class BathUpdateOptions {
	private Query query;
	private Update update;
	private boolean upsert = false;
	private boolean multi = false;

	public BathUpdateOptions(Query query, Update update, boolean upsert, boolean multi) {
		// TODO Auto-generated constructor stub
		this.query = query;
		this.update = update;
		this.upsert = upsert;
		this.multi = multi;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	public Update getUpdate() {
		return update;
	}

	public void setUpdate(Update update) {
		this.update = update;
	}

	public boolean isUpsert() {
		return upsert;
	}

	public void setUpsert(boolean upsert) {
		this.upsert = upsert;
	}

	public boolean isMulti() {
		return multi;
	}

	public void setMulti(boolean multi) {
		this.multi = multi;
	}

}
