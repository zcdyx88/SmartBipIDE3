package com.dcits.smartbip.register;

import com.dc.bip.ide.objects.ProtocolInService;

public interface PublishServiceMBean {

    public String pubBuzzService(PublishBuzzServiceDataBean bean);
    public String pubCompositeService(PublishCompositeServiceDataBean bean);
    public String pubBaseService(PublishBaseServiceDataBean bean);
    public String pubParamMapFile(PublishParamMapFileDataBean bean);
    public String pubPrtclService(ProtocolInService bean);
}
