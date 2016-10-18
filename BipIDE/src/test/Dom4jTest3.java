package test;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dc.bip.ide.gef.model.Param;
import com.dc.bip.ide.gef.model.ParamContainer;
import com.dc.bip.ide.gef.model.TitleModel;
import com.dc.bip.ide.objects.BusiSvcInfo;

public class Dom4jTest3 {

	public static void main(String[] args) {
	    new Dom4jTest3(). setBusiParams("E:\\runtime-EclipseApplication\\edc\\dev\\services\\busi\\S30021000001\\service_S3002100000102.xml",new BusiSvcInfo());
	}

	private boolean checkBody(Element body )
	{
		 List requests =body.elements();
         
         if(null == requests)
         {
         	System.err.println("请求body为空");
         	return false;
         }
         else if ( requests.size() !=1)
         {
         	System.err.println("请求body数大于1");
         	return false;
         } 
         return true;
	}
	private void  setBusiParams(String sdaFile,BusiSvcInfo busi)
	{
		ParamContainer pc = null;
		SAXReader sr = new SAXReader();
		try {
			Document doc = sr.read(new File(sdaFile));
            Element requestBody = doc.getRootElement().element("request").element("Envelope").element("Body");
            if(checkBody(requestBody))
            {
            	pc =  readParamContainer((Element)requestBody.elements().get(0));
            	busi.setInParams(pc);
            }      
            
            Element responseBody = doc.getRootElement().element("response").element("Envelope").element("Body");
            if(checkBody(responseBody))
            {
            	pc =  readParamContainer((Element)responseBody.elements().get(0));
            	busi.setOutParams(pc);
            }            
                   
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	private ParamContainer readParamContainer(Element root)
	{
		ParamContainer pc = new ParamContainer(root.getName());
		pc.addChild(new TitleModel(root.getName()));
		List elements = root.elements();
		for(Object tmpObject : elements)
		{
			Element tmpElement = (Element)tmpObject;
		   if(hasChildren(tmpElement))
		   {
			   pc.addChild(readParamContainer(tmpElement));
		   }
		   else
		   {
			   pc.addChild(new Param(tmpElement.getName()));
		   }		   
		}
		return pc;
	}
	
	private boolean hasChildren(Element  element)
	{
		List elements = element.elements();
		if(null != elements && elements.size()>0)
		{
			return true;
		}
		return false;
	}
}
