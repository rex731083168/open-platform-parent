/**
  * Copyright 2017 bejson.com 
  */
package cn.ce.platform_service.diyApply.entity.appsEntity;

/**
 * Auto-generated: 2017-10-13 14:32:39
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Params {
	
	private String mobileRunning;
	private String pcRunning;
    private boolean useViewType;
    private String name;
    private String owner;
    public void setUseViewType(boolean useViewType) {
         this.useViewType = useViewType;
     }
     public boolean getUseViewType() {
         return useViewType;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setOwner(String owner) {
         this.owner = owner;
     }
     public String getOwner() {
         return owner;
     }
	public String getMobileRunning() {
		return mobileRunning;
	}
	public void setMobileRunning(String mobileRunning) {
		this.mobileRunning = mobileRunning;
	}
	public String getPcRunning() {
		return pcRunning;
	}
	public void setPcRunning(String pcRunning) {
		this.pcRunning = pcRunning;
	}

}