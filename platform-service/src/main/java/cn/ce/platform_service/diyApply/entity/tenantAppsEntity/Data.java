/**
  * Copyright 2017 bejson.com 
  */
package cn.ce.platform_service.diyApply.entity.tenantAppsEntity;
import java.util.List;

/**
 * Auto-generated: 2017-10-13 11:28:45
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Data {

    private List<AppList> appList;
    private Tenant tenant;
    public void setAppList(List<AppList> appList) {
         this.appList = appList;
     }
     public List<AppList> getAppList() {
         return appList;
     }

    public void setTenant(Tenant tenant) {
         this.tenant = tenant;
     }
     public Tenant getTenant() {
         return tenant;
     }

}