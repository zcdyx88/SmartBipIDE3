package com.dcits.smartbip.register;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.dc.bip.ide.objects.PramsInfo;
import com.dc.bip.ide.objects.ProtocolInService;


public class PublishService implements PublishServiceMBean {
    private Log log = LogFactory.getLog(getClass());

    @Override
    public String pubBuzzService(PublishBuzzServiceDataBean bean) {
        log.info("****************** publish buzz service **************");
        log.info("binding[" + bean.busiSvcInfo.getBindingName() + "]");
        log.info("operation["+bean.busiSvcInfo.getOperationName()+"]");
        log.info("reversal["+bean.busiSvcInfo.getOppositeBusi().getId()+"]");
        log.info("wsdl[" + bean.wsdl + "]");
        log.info("serviceSchema[" + bean.serviceSchema + "]");
        log.info("metadataSchema[" + bean.metadataSchema + "]");
        log.info("****************** publish buzz service **************");
        return "OK";
    }

    @Override
    public String pubCompositeService(PublishCompositeServiceDataBean bean) {
        log.info("****************** publish composite service **************");
        log.info("serviceId[" + bean.serviceId + "]");
        log.info("serviceName[" + bean.serviceName + "]");
        log.info("serviceDesc[" + bean.serviceDesc + "]");

        log.info("****************** publish composite service **************");
        return "OK";
    }

	@Override
	public String pubBaseService(PublishBaseServiceDataBean bean) {
        log.info("****************** publish base service **************");
        log.info("serviceId[" + bean.id + "]");
        log.info("serviceName[" + bean.name + "]");
        log.info("serviceDesc[" + bean.desc + "]");
        log.info("code[" + bean.code + "]");
        log.info("****************** publish base service **************");
        return "OK";
	}

	@Override
	public String pubParamMapFile(PublishParamMapFileDataBean bean) {
        log.info("****************** pub ParamMapFile **************");
        log.info("serviceId[" +bean.mapFiles.toString() + "]");
        log.info("serviceId[" +bean.paramFile + "]");
        log.info("****************** pub ParamMapFile **************");
        return "OK";
	}

	@Override
	public String pubPrtclService(ProtocolInService bean) {
		log.info("****************** pub ProtocolService **************");
		log.info("ProtocolID[" + bean.getProtocolName() + "]");
		log.info("ProtocolType[" + bean.getProtocolType() + "]");
		log.info("Type[" + bean.getType() + "]");
		log.info("****************** pub ProtocolService **************");
		
		
		
		return "OK";
	}



	

}
