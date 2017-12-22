package cn.ce.platform_service.util;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import cn.ce.platform_service.zk.entity.ZKNodeTreeEntity;

public class ZkClientUtil implements Watcher {

	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

	@Override
	public void process(WatchedEvent event) {
		System.out.println("Receive watched event : " + event);
		if (KeeperState.SyncConnected == event.getState()) {
			connectedSemaphore.countDown();
		}
	}

	/* 递归获取目录 */
	public static ZKNodeTreeEntity getAllChildren(ZooKeeper zk, String root) throws Exception {
		ZKNodeTreeEntity zkt = new ZKNodeTreeEntity();
		zkt.setNodeName(root);
		List<String> children = zk.getChildren(root, true);
		for (String child : children) {
			ZKNodeTreeEntity zktn = getAllChildren(zk, root.endsWith("/") ? root + child : root + "/" + child);
			zkt.getChildrednList().add(zktn);
		}
		return zkt;
	}

}
