package cn.ac.iie.hy.areamonitor.server;

import cn.ac.iie.hy.areamonitor.config.Configuration;
import cn.ac.iie.hy.areamonitor.handler.SystemConfigHandler;
import cn.ac.iie.hy.areamonitor.task.GetDataLoop;
import cn.ac.iie.hy.areamonitor.task.ScheduledJobStart;
import cn.ac.iie.hy.areamonitor.task.ThreadPoolManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;


/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑, 永无BUG!
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * @author zhangyu
 */
public class AreaMonitorServer {

	static Server server = null;
	static Logger logger = null;

	static {
		PropertyConfigurator.configure("log4j.properties");
		logger = Logger.getLogger(AreaMonitorServer.class.getName());
	}

	public static void showUsage() {
		System.out.println("Usage:java -jar ");
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		try {
			init();
			startup();
		} catch (Exception ex) {
			logger.error("starting system is failed for " + ex.getMessage(), ex);
		}

		System.exit(0);
	}
	
	private static void startup() throws Exception {
		//start job here
		ThreadPoolManager threadPool = ThreadPoolManager.newInstance();
		threadPool.addExecuteTask(new GetDataLoop());//启动接收数据线程
		threadPool.addExecuteTask(new ScheduledJobStart());
		logger.info("starting system...");
		server.start();
		logger.info("start system successfully");
		server.join();
	}

	private static void init() throws Exception {
		String configurationFileName = "sysconfig.properties";
		logger.info("initializing system...");
		logger.info("getting configuration from configuration file " + configurationFileName);
		Configuration conf = Configuration.getConfiguration(configurationFileName);
		if (conf == null) {
			throw new Exception("reading " + configurationFileName + " is failed.");
		}

		String serverIP = conf.getString("jettyServerIP", "");
		if (serverIP.isEmpty()) {
			throw new Exception("definition jettyServerIP is not found in " + configurationFileName);
		}

		int serverPort = conf.getInt("jettyServerPort", -1);
		if (serverPort == -1) {
			throw new Exception("definition jettyServerPort is not found in " + configurationFileName);
		}

		server = new Server(serverPort);
		ContextHandler configContext = new ContextHandler("/sysconfig");
		SystemConfigHandler sysConfigHandler = SystemConfigHandler.getHandler();
		if (sysConfigHandler == null) {
			throw new Exception("initializing sysConfigHandler failed");
		}
		configContext.setHandler(sysConfigHandler);

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.setHandlers(new Handler[] { configContext });
		server.setHandler(contexts);
		logger.info("intialize system successfully");
		logger.info("config host is http://"+serverIP+":"+serverPort+"/sysconfig");

	}
}
