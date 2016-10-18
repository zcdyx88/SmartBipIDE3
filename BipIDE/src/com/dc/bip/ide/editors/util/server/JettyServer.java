package com.dc.bip.ide.editors.util.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;


/**
 * Implements an HTTP transport service using Jetty.
 * Http 接入容器,同样也用于WebService接入
 */
  
/**     
 * Copyright：2016-DCITS  
 * Project name：SmartBIP     
 *  
 * Class decription：  
 * Class name：com.dcits.smartbip.protocol.http.server.JettyServer       
 * Author：Sure-xujian  
 * Create date：2016年7月9日 下午4:20:53          
 */  
  
public class JettyServer{
    private static final Log log = LogFactory.getLog(JettyServer.class);
    private static final String PORT = "port";
    private static final String CONTEXT_PATH = "contextPath";
    private static final String THREAD_POOL_MIN = "minThreads";
    private static final String THREAD_POOL_MAX = "maxThreads";
    private static final String ACCEPT_QUEUE_SIZE = "acceptQueueSize";
    private static final String ACCEPTER_SIZE = "acceptSize";
    private Server server;
    private String id;
    private volatile Map<String,String> configMap;;
    private volatile boolean isStart = false;
    private static final Map<String,String> defaultConfigMap = new HashMap<String,String>();

    static {
    	defaultConfigMap.put(PORT, "8080");//setConfig(PORT, "8080");
    	defaultConfigMap.put(CONTEXT_PATH, "/ws");//setConfig(CONTEXT_PATH, "/ws");
    	defaultConfigMap.put(THREAD_POOL_MIN, "50");//setConfig(THREAD_POOL_MIN, "50");
    	defaultConfigMap.put(THREAD_POOL_MAX, "200");//setConfig(THREAD_POOL_MAX, "200");
    	defaultConfigMap.put(ACCEPT_QUEUE_SIZE, "50");//setConfig(ACCEPT_QUEUE_SIZE, "50");
    	defaultConfigMap.put(ACCEPTER_SIZE, "2");//setConfig(ACCEPTER_SIZE, "2");
    }

    public JettyServer(Map<String,String> configMap) {
    	setConfigMap(configMap);
    }

    public void start() {
        isStart = true;
        if (log.isInfoEnabled()) {
            log.info("开始启动协议[" + this.getId() + "]");
        }
        synchronized (JettyServer.class) {
            configMap = configMap == null ? defaultConfigMap : configMap;
            try {
                String port = (String)configMap.get(PORT);//Config(PORT);
                String contextPath = (String)configMap.get(CONTEXT_PATH);//configuration.getConfig(CONTEXT_PATH);
                String minThreads = (String)configMap.get(THREAD_POOL_MIN);//configuration.getConfig(THREAD_POOL_MIN);
                String maxThreads = (String)configMap.get(THREAD_POOL_MAX);//configuration.getConfig(THREAD_POOL_MAX);
                String acceptQueueSize = (String)configMap.get(ACCEPT_QUEUE_SIZE);//configuration.getConfig(ACCEPT_QUEUE_SIZE);
                String accepterSize = (String)configMap.get(ACCEPTER_SIZE);//configuration.getConfig(ACCEPTER_SIZE);
                if (log.isInfoEnabled()) {
                    log.info("协议参数:\n端口[" + port + "]\n路径[" + contextPath + "]\n最小处理线程[" + minThreads
                            + "]\n最大处理线程[" + maxThreads + "]\naccept队列深度[" + acceptQueueSize + "]\nacceptor的个数["
                            + accepterSize + "]");
                }
                if (StringUtils.isNumeric(port) && StringUtils.isNumeric(minThreads)
                        && StringUtils.isNumeric(maxThreads) && StringUtils.isNumeric(acceptQueueSize)) {
                    int portInt = Integer.parseInt(port);
                    int minThreadsInt = Integer.parseInt(minThreads);
                    int maxThreadsInt = Integer.parseInt(maxThreads);
                    int acceptQueueSizeInt = Integer.parseInt(acceptQueueSize);
                    int accepterSizeInt = Integer.parseInt(accepterSize);
                    server = new Server();
                    ServletHandler handler = new ServletHandler();
                    //Jetty的线程池设置,通过Jetty接入,不需要在做线程池分派,直接使用jetty接入的线程池
                    QueuedThreadPool threadPool = new QueuedThreadPool();
                    threadPool.setMinThreads(minThreadsInt);
                    threadPool.setMaxThreads(maxThreadsInt);
                    server.setThreadPool(threadPool);
                    SelectChannelConnector connector = new SelectChannelConnector();
                    connector.setPort(portInt);
                    //每个请求被accept前允许等待的连接数
                    connector.setAcceptQueueSize(acceptQueueSizeInt);
                    //同事监听read事件的线程数
                    connector.setAcceptors(accepterSizeInt);
                    //连接最大空闲时间，默认是200000，-1表示一直连接
                    connector.setMaxIdleTime(3000);
                    server.addConnector(connector);
                    server.setHandler(handler);
       
                    handler.addServletWithMapping(HttpServerServlet.class, contextPath);
                    
                    server.start();
                    //server.join();
                }
            } catch (Exception e) {
                isStart = false;
                log.error("协议[" + getId() + "]启动失败!", e);
            }
        }
        if (log.isInfoEnabled()) {
            log.info("协议[" + this.getId() + "]启动完成");
        }
    }

    /**
     * Stop all the started servers.
     */
    public void stop() {
        if (null != server) {
            isStart = false;
            try {
                server.stop();
                System.out.println("该应用服务器已经被停止");
            } catch (Exception e) {
                log.error(e, e);
            }
        }
    }

    
    public String getId() {
        return id;
    }

    
    public void setId(String id) {
        this.id = id;
    }

    
    public void reload() {

    }


    public static void main(String[] args) {
        JettyServer server = new JettyServer(null);
    }

	public Map<String, String> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<String, String> configMap) {
		this.configMap = configMap;
	}
}

