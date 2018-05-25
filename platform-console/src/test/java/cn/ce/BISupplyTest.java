package cn.ce;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Title: cn.ce
 * @Description: ${todo}(这里用一句话描述这个方法的作用)
 * @create 2018\5\24 0024/makangwei
 * @Copyright:中企动力科技股份有限公司 1999-2018 300.cn
 * All rights Reserved, Designed By www.300.cn
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration({"classpath*:/mongo-config.xml","classpath*:/mybatis-config.xml","classpath*:/spring-session.xml","classpath:/applicationContext.xml"})
@ContextConfiguration({"classpath:/applicationContext.xml"})
public class BISupplyTest {

    @Test
    public void testBI(){
        System.out.println("hello");
    }
}
