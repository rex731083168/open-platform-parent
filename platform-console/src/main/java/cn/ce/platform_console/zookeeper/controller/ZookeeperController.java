package cn.ce.platform_console.zookeeper.controller;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年12月15日
*/
public class ZookeeperController implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    
    
    @Override
    public void process(WatchedEvent event) {
        System.out.println("Receive watched event : " + event);
        if (KeeperState.SyncConnected == event.getState()) {
            connectedSemaphore.countDown();
        }
    }

	public static void main(String[] args) throws Exception {
		ZooKeeper zookeeper = new ZooKeeper("10.12.40.234:2181", 
				5000, new ZookeeperController());
		Thread.sleep(1000);
		System.out.println(zookeeper.getState());
		

		//getAllChildren(zookeeper, "/dubbo", "    ");
		byte[] b = zookeeper.getData("/dubbo/cn.ce.yun.zmail.service.YunZmailAppService/providers", true, new Stat());
		if(b != null && b.length > 1){
			System.out.println("data:"+new String(b));
		}
		System.out.println("children:"+zookeeper.getChildren("/dubbo/cn.ce.yun.zmail.service.YunZmailAppService/providers", true));
	}
	
	private static void getAllChildren(ZooKeeper zk, String root,String space) throws Exception{
		List<String> children = zk.getChildren(root, true);
		System.out.println(space+"节点"+root+"（"+children.size()+"）下面有："); 
		space+="    ";
		if(children.size() > 0){
			for (String child : children) {
				System.out.println(space+root);
				getAllChildren(zk, root.endsWith("/") ? root+child : root+"/"+child, space);
			}
		}
	}
}
