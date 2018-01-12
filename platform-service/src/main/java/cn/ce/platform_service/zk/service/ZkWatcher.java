package cn.ce.platform_service.zk.service;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月10日
*/
public class ZkWatcher implements Watcher{

	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	
	@Override
	public void process(WatchedEvent event) {
		System.out.println("Receive watched event : " + event);
		if (KeeperState.SyncConnected == event.getState()) {
			connectedSemaphore.countDown();
		}
	}

}
