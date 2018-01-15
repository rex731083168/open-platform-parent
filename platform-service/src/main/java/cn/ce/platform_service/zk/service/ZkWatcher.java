package cn.ce.platform_service.zk.service;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月10日
*/
public class ZkWatcher implements Watcher{

	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private static final Logger _LOGGER = LoggerFactory.getLogger(ZkWatcher.class);
	@Override
	public void process(WatchedEvent event) {
		
		_LOGGER.info("Receive watched event : "+ event);
		if (KeeperState.SyncConnected == event.getState()) {
			connectedSemaphore.countDown();
		}
	}

}
