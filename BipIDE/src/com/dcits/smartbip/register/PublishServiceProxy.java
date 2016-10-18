package com.dcits.smartbip.register;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dc.bip.ide.objects.BusiSvcInfo;
import com.dc.bip.ide.objects.ProtocolInService;
import com.dc.bip.ide.views.objects.ProtocolInNode;
import com.dc.bip.ide.views.objects.ProtocolOutNode;

public class PublishServiceProxy implements PublishServiceMBean {
	protected final static Log log = LogFactory.getLog(PublishServiceProxy.class);

	public final static String PUBLISH_SERVICE = "bip.agent:name=PublishServiceMBean";

	public static String getJMXURL(String ip, int jmxRmtPort, int jmxRegPort) {
		return "service:jmx:rmi://" + ip + ":" + jmxRmtPort + "/jndi/rmi://" + ip + ":" + jmxRegPort + "/jmxrmi";
	}

	private PublishServiceMBean getMBean(String ip, int regPort, int rmtPort) {
		PublishServiceMBean bean = null;
		JMXServiceURL url;
		try {
			String jmxUrl = PublishServiceProxy.getJMXURL(ip, rmtPort, regPort);
			url = new JMXServiceURL(jmxUrl);
			JMXConnector conn = JMXConnectorFactory.connect(url, null);
			MBeanServerConnection mbsc = conn.getMBeanServerConnection();
			ObjectName mbeanName = new ObjectName(PublishServiceProxy.PUBLISH_SERVICE);
			bean = JMX.newMBeanProxy(mbsc, mbeanName, PublishServiceMBean.class);
		} catch (Exception e) {
			log.error("JMX Exception,connector error", e);
		}
		return bean;
	}

	public String pubBuzzService(String ip, int regPort, int rmtPort, BusiSvcInfo busiSvcInfo, String wsdl, String serviceSchema, List<String> metadataSchema) {
		PublishBuzzServiceDataBean bean = new PublishBuzzServiceDataBean(busiSvcInfo, wsdl, serviceSchema, metadataSchema);
		return getMBean(ip, rmtPort, regPort).pubBuzzService(bean);
	}

	public String pubCompositeService(String ip, int regPort, int rmtPort, String serviceId, String serviceName,String serviceDesc, String processContent) {
		PublishCompositeServiceDataBean bean = new PublishCompositeServiceDataBean(serviceId, serviceName, serviceDesc, processContent );
		return getMBean(ip, rmtPort, regPort).pubCompositeService(bean);
	}

	public String pubBaseService(String ip, int regPort, int rmtPort, String name, String code, String id,
			String location, String desc) {
		PublishBaseServiceDataBean bean = new PublishBaseServiceDataBean(name, code, id, location, desc);
		return getMBean(ip, rmtPort, regPort).pubBaseService(bean);
	}
	
	public String pubProtocolService(String ip, int regPort, int rmtPort,ProtocolInService bean) {
		return getMBean(ip, rmtPort, regPort).pubPrtclService(bean);
	}
	


	public String pubParamMapFile(String ip,int regPort,int rmtPort ,List<String> mapFiles,String paramFile) 
{
    	PublishParamMapFileDataBean bean = new PublishParamMapFileDataBean( mapFiles,paramFile);
		return getMBean(ip, rmtPort,regPort).pubParamMapFile(bean);
}
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PublishServiceProxy proxy = new PublishServiceProxy();
		// proxy.pubBuzzService("testServie",
		// "testServiceName","namespace","testserviceDef",
		// "testwsdl", "testServiceSchema", null);
//		proxy.pubCompositeService("testServie", 0, 0, "testServiceName", "namespace", "testserviceDef", "testwsdl",
//				"testServiceSchema", "testmetadataSchema", "", null);
	}

	@Override
	public String pubBuzzService(PublishBuzzServiceDataBean bean) {
		return null;
	}

	@Override
	public String pubCompositeService(PublishCompositeServiceDataBean bean) {
		return null;
	}

	@Override
	public String pubBaseService(PublishBaseServiceDataBean bean) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String pubParamMapFile(PublishParamMapFileDataBean bean) {
		return null;
	}



	@Override
	public String pubPrtclService(ProtocolInService bean) {
		// TODO Auto-generated method stub
		return null;
	}


}
