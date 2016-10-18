package com.dc.bip.ide.editors.util.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * Copyright：2016-DCITS  
 * Project name：BipIDE     
 *  
 * Class decription：  
 * Class name：com.dc.bip.ide.editors.util.server.HttpServerServlet       
 * Author：Sure-xujian  
 * Create date：2016年7月11日 上午9:43:48
 */
public class HttpServerServlet extends HttpServlet {

    private static final Log log = LogFactory.getLog(HttpServerServlet.class);
    private ExecutorService threadpool = Executors.newFixedThreadPool(50);
    private int DEFAULT_TIME_OUT = 30000;
    private String respMsg;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		StringBuilder stringBuilder = new StringBuilder();
        InputStream input = req.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(input,"utf-8"));
        int length = 1024*8;
        char[] buf = new char[length];
        while((br.read(buf, 0, length)!=-1)){
        	stringBuilder.append(new String(buf));
        	buf = null;
        	buf = new char[length];
        }
        respMsg = stringBuilder.toString();
        OutputStream out = null;
        try{
        	out = resp.getOutputStream();
        	out.write(respMsg.getBytes());
        	out.flush();
        }catch(Exception e){
        	log.error(e,e);
        }finally{
        	if(br!=null){
        		br.close();
        	}
        	if(input!=null){
        		br.close();
        	}
        	if(out!=null){
        		out.close();
        	}
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
