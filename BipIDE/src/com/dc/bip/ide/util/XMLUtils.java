package com.dc.bip.ide.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XMLUtils {
	public static void saveDocument(Document document, File xmlFile) {
		Writer osWrite = null;
		XMLWriter writer = null;
		try{
			osWrite = new OutputStreamWriter(new FileOutputStream(xmlFile));// 创建输出流
			OutputFormat format = OutputFormat.createPrettyPrint(); // 获取输出的指定格式
			format.setEncoding("UTF-8");// 设置编码 ，确保解析的xml为UTF-8格式
			writer = new XMLWriter(osWrite, format);// XMLWriter
																// 指定输出文件以及格式
			writer.write(document);// 把document写入xmlFile指定的文件(可以为被解析的文件或者新创建的文件)
			writer.flush();
		}catch(Exception e){
			
		}finally{
			if(writer != null){
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(osWrite != null){
				try {
					osWrite.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	public static Document getDocFromFile(File file) {
		Document doc = null;
		if (null != file) {
			SAXReader reader = new SAXReader();
			BufferedInputStream bufferedInputStream = null;
			try {
				bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
				doc = reader.read(bufferedInputStream);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != bufferedInputStream) {
					try {
						bufferedInputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return doc;
	}
}
