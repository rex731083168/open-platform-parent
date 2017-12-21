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

	public static ZKNodeTreeEntity getAllChildren(ZooKeeper zk, String root) throws Exception {

		ZKNodeTreeEntity zkt = new ZKNodeTreeEntity();
		zkt.setNodeName(root);
		List<String> children = zk.getChildren(root, true);
		System.out.println("节点" + root + "（" + children.size() + "）下面有：");
		for (String child : children) {
			ZKNodeTreeEntity zktn = getAllChildren(zk, root.endsWith("/") ? root + child : root + "/" + child);
			zkt.getChildrednList().add(zktn);
		}
		return zkt;
	}

	public static void main(String[] args) {
		try {
			String rootPath = "/dubbo";
			ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 5000, new ZkClientUtil());
			ZKNodeTreeEntity zkt = new ZkClientUtil().getAllChildren(zk, rootPath);
			System.out.println(new ZkClientUtil().getAllChildren(zk, rootPath));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
