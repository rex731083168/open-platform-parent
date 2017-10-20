/**
  * Copyright 2017 bejson.com 
  */
package cn.ce.platform_service.diyApply.entity.tenantAppPage;

/**
 * Auto-generated: 2017-10-20 11:35:58
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Params {

    private String appName;
    private boolean mobileRunning;
    private boolean pcRunning;
    private boolean useViewType;
    private Integer tenaId;
    public void setAppName(String appName) {
         this.appName = appName;
     }
     public String getAppName() {
         return appName;
     }

    public void setMobileRunning(boolean mobileRunning) {
         this.mobileRunning = mobileRunning;
     }
     public boolean getMobileRunning() {
         return mobileRunning;
     }

    public void setPcRunning(boolean pcRunning) {
         this.pcRunning = pcRunning;
     }
     public boolean getPcRunning() {
         return pcRunning;
     }

    public void setUseViewType(boolean useViewType) {
         this.useViewType = useViewType;
     }
     public boolean getUseViewType() {
         return useViewType;
     }

    public void setTenaId(Integer tenaId) {
         this.tenaId = tenaId;
     }
     public Integer getTenaId() {
         return tenaId;
     }

}