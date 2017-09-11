package cn.ce.oauth.entity;

public class AuthorizeClientEntity {

	 /** 最大配额*/
    private int quota_max;
    
    /** 最大配额 重置周期 */
    private int quota_renewal_rate;
    
    /** 频次  */
    private int rate;
    
    /** 访问频次计数周期  */
    private int per;
    
    private int allowance;
    
    private Integer expires;
    
    private int quota_renews;
    
    private int quota_remaining;
    
    

	public int getAllowance() {
		return allowance;
	}

	public void setAllowance(int allowance) {
		this.allowance = allowance;
	}

	public Integer getExpires() {
		return expires;
	}

	public void setExpires(Integer expires) {
		this.expires = expires;
	}

	public int getQuota_renews() {
		return quota_renews;
	}

	public void setQuota_renews(int quota_renews) {
		this.quota_renews = quota_renews;
	}

	public int getQuota_remaining() {
		return quota_remaining;
	}

	public void setQuota_remaining(int quota_remaining) {
		this.quota_remaining = quota_remaining;
	}

	public int getQuota_max() {
		return quota_max;
	}

	public void setQuota_max(int quota_max) {
		this.quota_max = quota_max;
	}

	public int getQuota_renewal_rate() {
		return quota_renewal_rate;
	}

	public void setQuota_renewal_rate(int quota_renewal_rate) {
		this.quota_renewal_rate = quota_renewal_rate;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getPer() {
		return per;
	}

	public void setPer(int per) {
		this.per = per;
	}
    

    
	
}
