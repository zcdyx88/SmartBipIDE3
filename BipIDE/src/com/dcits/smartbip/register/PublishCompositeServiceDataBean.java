package com.dcits.smartbip.register;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by vincentfxz on 16/5/7.
 */
public class PublishCompositeServiceDataBean implements Serializable {
    private static final long serialVersionUID = 1L;
    String serviceId;
    String serviceName;
    String serviceDesc;
    String processContent;

    public PublishCompositeServiceDataBean(String serviceId, String serviceName, String serviceDesc, String processContent) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceDesc = serviceDesc;
        this.processContent = processContent;
    }


}
