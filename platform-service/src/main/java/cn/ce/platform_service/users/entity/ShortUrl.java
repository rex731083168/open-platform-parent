package cn.ce.platform_service.users.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 *
 * @Title: ShortUrl.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月23日 time上午10:26:58
 *
 **/
@Document(collection = "USER_CHCEK_URL")
public class ShortUrl {

	@Id
	private String id;
	/* 长连接 */
	@Field("longUrl")
	private String longUrl;

	/* 短连接 */
	@Field("shortUrt")
	private String shortUrt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}

	public String getShortUrt() {
		return shortUrt;
	}

	public void setShortUrt(String shortUrt) {
		this.shortUrt = shortUrt;
	}

}
