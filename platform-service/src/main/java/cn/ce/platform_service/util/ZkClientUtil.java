package cn.ce.platform_service.util;

import java.net.URLDecoder;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import cn.ce.platform_service.zk.entity.ZKNodeTreeEntity;
import net.sf.json.JSONObject;

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

	/* 递归获取目录 */
	public static JSONObject getChildren(String zkconnectioninfo, String root) throws Exception {
		ZooKeeper zk = new ZooKeeper(zkconnectioninfo, 15000, new ZkClientUtil());
		ZKNodeTreeEntity zkt = new ZKNodeTreeEntity();
		zkt.setNodeName(root);
		List<String> children = zk.getChildren(URLDecoder.decode(root, "UTF-8"), true);
		if (children.size() == 0) {
			zkt.setLeafnode(true);
		}
		for (String child : children) {
			ZKNodeTreeEntity zktn = new ZKNodeTreeEntity();
			zktn.setNodeName(URLDecoder.decode(child, "UTF-8"));
			zkt.getChildrednList().add(zktn);
			if (zk.getChildren(root.endsWith("/") ? root + child : root + "/" + child, true).size() > 0) {
				zktn.setLeafnode(false);
			} else {
				zktn.setLeafnode(true);
			}

		}
		JSONObject json = JSONObject.fromBean(zkt);
		return json;
	}

}
