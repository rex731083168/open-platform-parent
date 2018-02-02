package cn.ce.platform_service.zk.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.util.RandomUtil;
import cn.ce.platform_service.zk.dao.IDubboConfiguratorDao1;
import cn.ce.platform_service.zk.dao.IDubboConsumerDao1;
import cn.ce.platform_service.zk.dao.IDubboNodeDao1;
import cn.ce.platform_service.zk.dao.IDubboProviderDao1;
import cn.ce.platform_service.zk.dao.IDubboRootDao1;
import cn.ce.platform_service.zk.dao.IDubboRouterDao1;
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
@Transactional
public class ZkDubboServiceImpl implements IZkDubboService {

	private static final Logger _LOGGER = LoggerFactory.getLogger(ZkDubboServiceImpl.class);
//	@Resource
//	private IZkDao zkDao;
	/** mongo dao */
//	@Resource
//	private IDubboRootDao dubboRootDao;
//	@Resource
//	private IDubboProviderDao dubboProviderDao;
//	@Resource
//	private IDubboConsumerDao dubboConsumerDao;
//	@Resource
//	private IDubboRouterDao dubboRouterDao;
//	@Resource
//	private IDubboConfiguratorDao dubboConfiguratorDao;
//	@Resource
//	private IDubboNodeDao dubboNodeDao;
	
	/** mysql dao */
	@Resource
	private IDubboRootDao1 dubboRootDao1;
	@Resource
	private IDubboProviderDao1 dubboProviderDao1;
	@Resource
	private IDubboConsumerDao1 dubboConsumerDao1;
	@Resource
	private IDubboRouterDao1 dubboRouterDao1;
	@Resource
	private IDubboConfiguratorDao1 dubboConfiguratorDao1;
	@Resource
	private IDubboNodeDao1 dubboNodeDao1;
	
	private static String[] childCategory = { "/providers", "/consumers", "/routers", "/configurators" };

	@Override
	public boolean clearAll() {
		dubboProviderDao1.clearAll();
		dubboConsumerDao1.clearAll();
		dubboNodeDao1.clearAll();
		dubboRouterDao1.clearAll();
		dubboConfiguratorDao1.clearAll();
		dubboRootDao1.clearAll();
		
		return true;
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
			DubboRoot dRoot = new DubboRoot(root,RandomUtil.random32UUID());
			dubboRootDao1.save(dRoot);
			
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

				DubboNode dNode = new DubboNode(nodeStr, dRoot.getId(),RandomUtil.random32UUID());
				dubboNodeDao1.save(dNode);

				String nodePath = root + "/" + nodeStr;

				// 解析所有分支的providers/consumers/routers/configurators
				for (String category : childCategory) {
					List<String> currentNodeData  = null;
					try {
						currentNodeData = zk.getChildren(nodePath + category, true);
					} catch (KeeperException | InterruptedException e) {
						// 如果发生异常继续往下执行，放弃当前节点数据
						_LOGGER.error("no node found:" + nodePath + category,e.getMessage());
						continue;
					}
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
				provider.setId(RandomUtil.random32UUID());
				dubboProviderDao1.save(provider);
				_LOGGER.debug("save Provider:"+provider);
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
				consumer.setId(RandomUtil.random32UUID());
				dubboConsumerDao1.save(consumer);
				_LOGGER.debug("save Consumer:"+consumer);
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
				router.setId(RandomUtil.random32UUID());
				dubboRouterDao1.save(router);
				_LOGGER.debug("save Router:"+router);
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
				configurator.setId(RandomUtil.random32UUID());
				dubboConfiguratorDao1.save(configurator);
				_LOGGER.debug("save Configurator:"+configurator);
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
		
		int pageNum = dubboRootDao1.findTotalPage();
		List<DubboRoot> rootList = dubboRootDao1.findPage((currentPage-1)*pageSize, pageSize);
		Page<DubboRoot> page = new Page<DubboRoot>(currentPage,pageNum,pageSize,rootList);
		Result<Page<DubboRoot>> result = new Result<Page<DubboRoot>>();
		result.setSuccessData(page);
		return result;
	}
	
	@Override
	public Result<?> findNodePage(int currentPage, int pageSize, String rootId) {
		
		//Page<DubboNode> page = dubboNodeDao.findPage(currentPage, pageSize, rootId);
		int pageNum = dubboNodeDao1.findTotalPage(rootId);
		List<DubboNode> rootList = dubboNodeDao1.findPage((currentPage-1)*pageSize, pageSize,rootId);
		Page<DubboNode> page = new Page<DubboNode>(currentPage,pageNum,pageSize,rootList);
		Result<Page<DubboNode>> result = new Result<Page<DubboNode>>();
		result.setSuccessData(page);
		return result;
	}

	@Override
	public Result<?> findNodeById(String id) {
		
		Result<DubboNode> result = new Result<DubboNode>();
		DubboNode node = dubboNodeDao1.findById(id);
		if(null != node){
			result.setSuccessData(node);
		}else{
			result.setErrorMessage("当前id不存在", ErrorCodeNo.SYS006);
		}
		return result;
	}

	@Override
	public Result<?> findProviderPage(int currentPage, int pageSize, String nodeId) {
//		Page<DubboProvider> page = dubboProviderDao.findPage(currentPage, pageSize, nodeId);
		int pageNum = dubboProviderDao1.findTotalPage(nodeId);
		List<DubboProvider> rootList = dubboProviderDao1.findPage((currentPage-1)*pageSize, pageSize,nodeId);
		Page<DubboProvider> page = new Page<DubboProvider>(currentPage,pageNum,pageSize,rootList);
		Result<Page<DubboProvider>> result = new Result<Page<DubboProvider>>();
		result.setSuccessData(page);
		return result;
	}

	@Override
	public Result<?> findProviderById(String id) {
		
		Result<DubboProvider> result = new Result<DubboProvider>();
		DubboProvider provider = dubboProviderDao1.findById(id);
		if(null != provider){
			result.setSuccessData(provider);
		}else{
			result.setErrorMessage("当前id不存在", ErrorCodeNo.SYS006);
		}
		return result;
	}

	@Override
	public Result<?> findConsumerPage(int currentPage, int pageSize, String nodeId) {
//		Page<DubboConsumer> page = dubboConsumerDao.findPage(currentPage, pageSize, nodeId);
		int pageNum = dubboConsumerDao1.findTotalPage(nodeId);
		List<DubboConsumer> rootList = dubboConsumerDao1.findPage((currentPage-1)*pageSize, pageSize,nodeId);
		Page<DubboConsumer> page = new Page<DubboConsumer>(currentPage,pageNum,pageSize,rootList);
		Result<Page<DubboConsumer>> result = new Result<Page<DubboConsumer>>();
		result.setSuccessData(page);
		return result;
	}

	@Override
	public Result<?> findConsumerById(String id) {
		Result<DubboConsumer> result = new Result<DubboConsumer>();
		DubboConsumer consumer= dubboConsumerDao1.findById(id);
		if(null != consumer){
			result.setSuccessData(consumer);
		}else{
			result.setErrorMessage("当前id不存在", ErrorCodeNo.SYS006);
		}
		return result;
	}

	@Override
	public Result<?> findRouterPage(int currentPage, int pageSize, String nodeId) {
//		Page<DubboRouter> page = dubboRouterDao.findPage(currentPage, pageSize, nodeId);
		int pageNum = dubboRouterDao1.findTotalPage(nodeId);
		List<DubboRouter> rootList = dubboRouterDao1.findPage((currentPage-1)*pageSize, pageSize,nodeId);
		Page<DubboRouter> page = new Page<DubboRouter>(currentPage,pageNum,pageSize,rootList);
		Result<Page<DubboRouter>> result = new Result<Page<DubboRouter>>();
		result.setSuccessData(page);
		return result;
	}

	@Override
	public Result<?> findRouterById(String id) {
		Result<DubboRouter> result = new Result<DubboRouter>();
		DubboRouter router= dubboRouterDao1.findById(id);
		if(null != router){
			result.setSuccessData(router);
		}else{
			result.setErrorMessage("当前id不存在", ErrorCodeNo.SYS006);
		}
		return result;
	}

	@Override
	public Result<?> findConfiguratorPage(int currentPage, int pageSize, String nodeId) {
//		Page<DubboConfigurator> page = dubboConfiguratorDao.findPage(currentPage, pageSize, nodeId);
		int pageNum = dubboConfiguratorDao1.findTotalPage(nodeId);
		List<DubboConfigurator> rootList = dubboConfiguratorDao1.findPage((currentPage-1)*pageSize, pageSize,nodeId);
		Page<DubboConfigurator> page = new Page<DubboConfigurator>(currentPage,pageNum,pageSize,rootList);
		Result<Page<DubboConfigurator>> result = new Result<Page<DubboConfigurator>>();
		result.setSuccessData(page);
		return result;
	}

	@Override
	public Result<?> findConfiguratorById(String id) {
		Result<DubboConfigurator> result = new Result<DubboConfigurator>();
		DubboConfigurator configurator= dubboConfiguratorDao1.findById(id);
		if(null != configurator){
			result.setSuccessData(configurator);
		}else{
			result.setErrorMessage("当前id不存在", ErrorCodeNo.SYS006);
		}
		return result;
	}

}
