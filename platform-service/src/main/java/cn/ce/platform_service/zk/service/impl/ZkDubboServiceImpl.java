package cn.ce.platform_service.zk.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.zk.dao.IDubboConfiguratorDao;
import cn.ce.platform_service.zk.dao.IDubboConsumerDao;
import cn.ce.platform_service.zk.dao.IDubboNodeDao;
import cn.ce.platform_service.zk.dao.IDubboProviderDao;
import cn.ce.platform_service.zk.dao.IDubboRootDao;
import cn.ce.platform_service.zk.dao.IDubboRouterDao;
import cn.ce.platform_service.zk.dao.IZkDao;
import cn.ce.platform_service.zk.entity.DubboConfigurator;
import cn.ce.platform_service.zk.entity.DubboConsumer;
import cn.ce.platform_service.zk.entity.DubboNode;
import cn.ce.platform_service.zk.entity.DubboProvider;
import cn.ce.platform_service.zk.entity.DubboRoot;
import cn.ce.platform_service.zk.entity.DubboRouter;
import cn.ce.platform_service.zk.service.IZkDubboService;
import cn.ce.platform_service.zk.service.ZkDubboParser;
import cn.ce.platform_service.zk.service.ZkWatcher;

/**
 * @Description : 获取zookeeper中dubbo服务的service
 * @Author : makangwei
 * @Date : 2018年1月9日
 */
@Service("zkDubboService")
public class ZkDubboServiceImpl implements IZkDubboService {

	private static final Logger _LOGGER = LoggerFactory.getLogger(ZkDubboServiceImpl.class);
	@Resource
	private IZkDao zkDao;
	@Resource
	private IDubboRootDao dubboRootDao;
	@Resource
	private IDubboProviderDao dubboProviderDao;
	@Resource
	private IDubboConsumerDao dubboConsumerDao;
	@Resource
	private IDubboRouterDao dubboRouterDao;
	@Resource
	private IDubboConfiguratorDao dubboConfiguratorDao;
	@Resource
	private IDubboNodeDao dubboNodeDao;
	
	private static String[] childCategory = { "/providers", "/consumers", "/routers", "/configurators" };

	@Override
	public boolean clearAll() {
		boolean flag1 = dubboProviderDao.clearAll();
		boolean flag2 = dubboConsumerDao.clearAll();
		boolean flag3 = dubboNodeDao.clearAll();
		boolean flag4 = dubboRouterDao.clearAll();
		boolean flag5 = dubboConfiguratorDao.clearAll();
		boolean flag6 = dubboRootDao.clearAll();
		if (flag1 && flag2 && flag3 && flag4 && flag5 && flag6) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Title: updateData @Description: 拉取数据顺序如下： 1、先获取根目录下的二级目录
	 * 2、再获取二级目录下provider/consumer/router/configator下的数据 3、解析数据，保存数据库
	 * 4、遍历 @author: makangwei @date: 2018年1月9日 下午4:57:02 @throws
	 */
	@Override
	public boolean updateData(String zkconnectioninfo, String datakey) {

		ZooKeeper zk = null;
		try {
			// TODO 超时时间写在配置文件
			zk = new ZooKeeper(zkconnectioninfo, 5000, new ZkWatcher());
		} catch (Exception e) {
			_LOGGER.error("_zookeeper create failed, check ip/port if it's avaliable ", e);
			return false;
		}

		String[] zkRoots = datakey.split(",");

		for (String root : zkRoots) {
			
			//将root表存放到数据库
			DubboRoot dRoot = dubboRootDao.save(new DubboRoot(root));
			
			List<String> nodes = null;
			try {
				nodes = zk.getChildren(root, true);
			} catch (KeeperException | InterruptedException e) {
				// TODO 添加重试机制，如果失败可能是网络原因，重试几次，同时 某一个节点失败后记录数据库继续执行下一个节点
				_LOGGER.error("error happens when get zk child:" + root, e);
				continue;
			}
			// TODO 保存所有的node信息到数据库

			for (String nodeStr : nodes) {

				DubboNode dNode = dubboNodeDao.save(new DubboNode(nodeStr, dRoot.getId()));

				String nodePath = root + "/" + nodeStr;

				// 解析所有分支的providers/consumers/routers/configurators
				for (String category : childCategory) {
					try {
						List<String> currentNodeData = zk.getChildren(nodePath + category, true);
						boolean flag = false;
						switch (category) {
						case "/providers":
							flag = saveProviders(currentNodeData, dNode.getId());
							break;
						case "/consumers":
							flag = saveConsumers(currentNodeData, dNode.getId());
							break;
						case "/routers":
							flag = saveRouters(currentNodeData, dNode.getId());
							break;
						case "/configurators":
							flag = saveConfigurators(currentNodeData, dNode.getId());
							break;
						}
					} catch (KeeperException | InterruptedException e) {
						// 如果发生异常继续往下执行，放弃当前节点数据
						_LOGGER.error("error happens when get zk child:" + nodePath + category);
						continue;
					}
				}
			}
		}
		return true;
	}

	private boolean saveProviders(List<String> currentNodeProviders, String nodeId) {

		for (String string : currentNodeProviders) {
			DubboProvider provider = ZkDubboParser.parserProvider(string);
			if (provider != null) {
				provider.setNodeId(nodeId);
				dubboProviderDao.save(provider);
			} else {
				// TODO 记录失败信息到数据库
			}
		}
		return true;
	}

	private boolean saveConsumers(List<String> currentNodeConsumers, String nodeId) {

		for (String string : currentNodeConsumers) {
			DubboConsumer consumer = ZkDubboParser.parserConsumer(string);
			if (consumer != null) {
				consumer.setNodeId(nodeId);
				dubboConsumerDao.save(consumer);
			} else {
				// TODO 记录失败信息到数据库
			}
		}

		return true;
	}

	private boolean saveRouters(List<String> currentNodeRouters, String nodeId) {

		for (String string : currentNodeRouters) {
			DubboRouter router = ZkDubboParser.parserRouter(string);
			if (router != null) {
				router.setNodeId(nodeId);
				dubboRouterDao.save(router);
			} else {
				// TODO 记录失败信息到数据库
			}
		}

		return true;
	}

	private boolean saveConfigurators(List<String> currentNodeConfigurators, String nodeId) {

		for (String string : currentNodeConfigurators) {
			DubboConfigurator configurator = ZkDubboParser.parserConfigurators(string);
			if (configurator != null) {
				configurator.setNodeId(nodeId);
				dubboConfiguratorDao.save(configurator);
			} else {
				// TODO 记录失败信息到数据库
			}
		}
		return true;
	}

//	public static void main(String[] args) throws Exception {
//		ZooKeeper zk = new ZooKeeper("10.12.40.189:2181,10.12.40.189:2182,10.12.40.189:2183", 5000, new ZkClientUtil());
//		/// dubbo-upgrade,/dubbo-debug
//		List<String> list = zk.getChildren("/dubbo-upgrade", true);
//		for (String string : list) {
//			System.out.println(string);
//		}	
//
//	}

	@Override
	public Result<?> findRootPage(Integer currentPage, Integer pageSize) {
		
		Page<DubboRoot> page = dubboRootDao.findPage(currentPage, pageSize);
		Result<Page<DubboRoot>> result = new Result<Page<DubboRoot>>();
		result.setSuccessData(page);
		return result;
	}
	
	@Override
	public Result<?> findNodePage(int currentPage, int pageSize, String rootId) {
		
		Page<DubboNode> page = dubboNodeDao.findPage(currentPage, pageSize, rootId);
		Result<Page<DubboNode>> result = new Result<Page<DubboNode>>();
		result.setSuccessData(page);
		return result;
	}

	@Override
	public Result<?> findNodeById(String id) {
		
		Result<DubboNode> result = new Result<DubboNode>();
		DubboNode node = dubboNodeDao.findById(id);
		if(null != node){
			result.setSuccessData(node);
		}else{
			result.setErrorMessage("当前id不存在", ErrorCodeNo.SYS006);
		}
		return result;
	}

	@Override
	public Result<?> findProviderPage(int currentPage, int pageSize, String nodeId) {
		Page<DubboProvider> page = dubboProviderDao.findPage(currentPage, pageSize, nodeId);
		Result<Page<DubboProvider>> result = new Result<Page<DubboProvider>>();
		result.setSuccessData(page);
		return result;
	}

	@Override
	public Result<?> findProviderById(String id) {
		
		Result<DubboProvider> result = new Result<DubboProvider>();
		DubboProvider provider = dubboProviderDao.findById(id);
		if(null != provider){
			result.setSuccessData(provider);
		}else{
			result.setErrorMessage("当前id不存在", ErrorCodeNo.SYS006);
		}
		return result;
	}

	@Override
	public Result<?> findConsumerPage(int currentPage, int pageSize, String nodeId) {
		Page<DubboConsumer> page = dubboConsumerDao.findPage(currentPage, pageSize, nodeId);
		Result<Page<DubboConsumer>> result = new Result<Page<DubboConsumer>>();
		result.setSuccessData(page);
		return result;
	}

	@Override
	public Result<?> findConsumerById(String id) {
		Result<DubboConsumer> result = new Result<DubboConsumer>();
		DubboConsumer consumer= dubboConsumerDao.findById(id);
		if(null != consumer){
			result.setSuccessData(consumer);
		}else{
			result.setErrorMessage("当前id不存在", ErrorCodeNo.SYS006);
		}
		return result;
	}

	@Override
	public Result<?> findRouterPage(int currentPage, int pageSize, String nodeId) {
		Page<DubboRouter> page = dubboRouterDao.findPage(currentPage, pageSize, nodeId);
		Result<Page<DubboRouter>> result = new Result<Page<DubboRouter>>();
		result.setSuccessData(page);
		return result;
	}

	@Override
	public Result<?> findRouterById(String id) {
		Result<DubboRouter> result = new Result<DubboRouter>();
		DubboRouter router= dubboRouterDao.findById(id);
		if(null != router){
			result.setSuccessData(router);
		}else{
			result.setErrorMessage("当前id不存在", ErrorCodeNo.SYS006);
		}
		return result;
	}

	@Override
	public Result<?> findConfiguratorPage(int currentPage, int pageSize, String nodeId) {
		Page<DubboConfigurator> page = dubboConfiguratorDao.findPage(currentPage, pageSize, nodeId);
		Result<Page<DubboConfigurator>> result = new Result<Page<DubboConfigurator>>();
		result.setSuccessData(page);
		return result;
	}

	@Override
	public Result<?> findConfiguratorById(String id) {
		Result<DubboConfigurator> result = new Result<DubboConfigurator>();
		DubboConfigurator configurator= dubboConfiguratorDao.findById(id);
		if(null != configurator){
			result.setSuccessData(configurator);
		}else{
			result.setErrorMessage("当前id不存在", ErrorCodeNo.SYS006);
		}
		return result;
	}



}
