package test;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Dom4jTest2 {
	public static void main(String[] args) {
		 File xmlf = new File("e:\\esb\\flow\\test.xml"); 
         Document doc;
		try {
			     doc = new SAXReader().read(xmlf);
		         Element e_plan = (Element) doc.selectSingleNode("/persons"); 
		         Element e_id = (Element) e_plan.selectSingleNode("./country"); 
		         Element e_id1 = (Element) e_plan.selectSingleNode("country"); 
		         List<Element> eplist = doc.selectNodes("//serviceId"); 
		         for (Element ep : eplist) { 
		    /*             String path = ep.getPath(); 
		                 Element id = (Element) ep.selectSingleNode("id"); 
		                 System.out.println(id.getText()); */
		        	  System.out.println(ep.getText());
		         } 
		         System.out.println("---------------"); 
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
