package test;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class Dom4jTest {

	public static void main(String[] args) {
		SAXReader sr = new SAXReader();
		try {
			Document doc = sr.read(new File("E:\\ESB\\flow\\test2.xml"));
			Element root = doc.getRootElement();
			List serviceIds = new Dom4jTest().getBusiServiceIds(root.element("process"));
			System.out.println(serviceIds.toString());
			/*Element processNode = doc.getRootElement().element("process");
			Element test = (Element)doc.selectSingleNode("/definitions/process");
			
			List services = processNode.selectNodes("//serviceTask");
			
			for(Iterator it = services.iterator();it.hasNext();)
			{
				Element element =(Element)it.next();
				Element params = element.element("extensionElements");
				Element param = params.element("serviceId");
				System.out.println(param.getText());
			}
			
			Element process = root.element("process");
			List serviceTasks = process.elements("serviceTask");
			for(Iterator it = serviceTasks.iterator();it.hasNext();)
			{
				Element serviceTask = (Element)it.next();
				System.out.println(serviceTask.attributeValue("extensionId"));
			}
			*/
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	private List<String>getBusiServiceIds(Element root)
	{
		List<String> list = new ArrayList<String>();
		List serviceTasks = root.elements("serviceTask");
		for(Object tmpObject :serviceTasks)
		{
			
			Element serviceTask = (Element)tmpObject;			
			String extensionId = serviceTask.attribute("extensionId").getText();
			if(null !=extensionId && extensionId.equals("task.BusinessTask")){
				Element extensionElements = (Element)serviceTask.element("extensionElements");				
				if(null !=extensionElements)
				{
					if(null != extensionElements.element("serviceId"))
					{
					String serviceId= serviceTask.element("extensionElements").element("serviceId").getText();
					list.add(serviceId);
				    }
			   }			
		}		
		}
		
		List subProcesses = root.elements("subProcess");
		for(Object tmpObject2: subProcesses)
		{
			Element subProcess = (Element)tmpObject2;
			list.addAll(getBusiServiceIds(subProcess));
		}
		
		List transactions = root.elements("transaction");
		for( Object tmpObject3: transactions)
		{
			Element transaction = (Element)tmpObject3;
			list.addAll(getBusiServiceIds(transaction));
		}		
		return list;
	}
}
