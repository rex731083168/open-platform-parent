package cn.ce.platform_console.zookeeper.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.cachelocal.CacheManager;
import cn.ce.platform_service.util.ZkClientUtil;
import cn.ce.platform_service.zk.entity.ZKNodeTreeEntity;
import net.sf.json.JSONObject;

@Component
@Service("updateData")
public class UpdateData {
	/** 日志对象 */
	private static Logger _LOGGER = Logger.getLogger(UpdateData.class);

	@Autowired
	private CacheManager cacheManager;
	@Value("#{redis['zookeeper.connection']}")
	private String zkconnectioninfo;
	@Value("#{redis['dubbo.node']}")
	private String datakey;

	/**
	 * 每天夜里1点定时执行任务获取数据
	 */
	// @Scheduled(fixedRate=1000*2)
	@Scheduled(cron = "0 0 1 * * *")
	public void updataZkCache() {
		this.UpdateData(zkconnectioninfo, datakey);
	}

	/**
	 * @param zkconnectioninfo
	 *            zookeeper.connection
	 * @param datakey
	 *            cachkey dubbo.node=dubbo,dubbo-1,dubbo-2 用逗号隔开不能重复
	 */
	public void UpdateData(String zkconnectioninfo, String datakey) {
		// TODO Auto-generated constructor stub
		ZooKeeper zk;
		try {
			zk = new ZooKeeper(zkconnectioninfo, 5000, new ZkClientUtil());

			String[] arry = datakey.split(",");
			for (int i = 0; i < arry.length; i++) {
				ZKNodeTreeEntity zkt = null;
				try {
					zkt = new ZkClientUtil().getAllChildren(zk, arry[i]);
					JSONObject json = JSONObject.fromBean(zkt);
					cacheManager.putCache(arry[i], json);
					_LOGGER.info("Add cach zkTree key=" + arry[i] + "+value=+" + json + "");
				} catch (Exception e) {
					// TODO: handle exception
					_LOGGER.error(e);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			_LOGGER.error("Add cach zkTree failde" + e + "");
		}

	}

}
