package com.dcits.smartbip.register;

import java.io.Serializable;
import java.util.List;

import com.dc.bip.ide.objects.BusiSvcInfo;

public class PublishBuzzServiceDataBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    BusiSvcInfo busiSvcInfo;
    String wsdl;
    String serviceSchema;
    List<String>  metadataSchema;

	public PublishBuzzServiceDataBean(BusiSvcInfo busiSvcInfo, String wsdl, String serviceSchema, List<String> metadataSchema) {
		super();
		this.wsdl = wsdl;
		this.serviceSchema = serviceSchema;
		this.metadataSchema = metadataSchema;
	}

}
