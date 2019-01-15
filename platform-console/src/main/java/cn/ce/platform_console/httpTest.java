package cn.ce.platform_console;

import cn.ce.platform_service.util.http.HttpMethod;
import cn.ce.platform_service.util.http.HttpResult;
import cn.ce.platform_service.util.http.HttpUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: httpTest
 * @Description: TODO
 * @create 2019/1/9 17:28/MKW
 * @update 2019/1/9 17:28/MKW/(说明。)....多次修改添加多个update
 */
@RestController
public class httpTest {

    @RequestMapping(value="/test/http")
    public String testHttp(){
        for (int i = 0; i< 30 ; i++){
            String url = "http://openapi.300.cn/openapi/siteConfigService/getPageInfoByPhysicalName.do?name=blank0";
            String jsonStr = "{}";
            Map<String, String> headers = new HashMap<>();
            headers.put("X-Tyk-Authorization","352d20ee67be67f6340b4c0605b044b7");
            headers.put("api-version","v1");
            headers.put("Saas-Id","100870");
            headers.put("Authorization","Bearer root8ccb46f4d75b47e09aee40a1879b70c8");
            HttpMethod method = HttpMethod.POST;
            boolean outputLogger = true;
            int timeout = 50000;
            HttpResult result = HttpUtil.postJson(url,jsonStr,headers,outputLogger,50000);
            System.out.println(result.getStatus()+":"+result.getBody());
        }
        return "aaa";
    }
}
