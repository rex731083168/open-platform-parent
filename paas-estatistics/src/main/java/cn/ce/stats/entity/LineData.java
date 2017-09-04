package cn.ce.stats.entity;

/**
 *
 * @Title: LineData.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年9月1日 time下午1:45:06
 *
 **/
public class LineData {

	public String keyAsString;
	public Long doccount;
	public Double intavg;

	public String getKeyAsString() {
		return keyAsString;
	}

	public void setKeyAsString(String keyAsString) {
		this.keyAsString = keyAsString;
	}


	public Long getDoccount() {
		return doccount;
	}

	public void setDoccount(Long doccount) {
		this.doccount = doccount;
	}

	public Double getIntavg() {
		return intavg;
	}

	public void setIntavg(Double intavg) {
		this.intavg = intavg;
	}

	
}
