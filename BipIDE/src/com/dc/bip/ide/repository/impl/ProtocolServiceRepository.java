package com.dc.bip.ide.repository.impl;

import java.util.concurrent.ConcurrentHashMap;

import com.dc.bip.ide.repository.IRepository;
import com.dc.bip.ide.service.ProtocolService;

public class ProtocolServiceRepository implements IRepository<ProtocolService, String> {
	
	private final ConcurrentHashMap<String, ProtocolService> protocolServiceForProjects;
	private static ProtocolServiceRepository instance;
	
	private ProtocolServiceRepository() {
		protocolServiceForProjects = new ConcurrentHashMap<String, ProtocolService>();
	}
	
	public static ProtocolServiceRepository getInstance(){
		if(null == instance){
			instance = new ProtocolServiceRepository();
		}
		return instance;
	}

	@Override
	public ProtocolService get(String id) {
		ProtocolService protocolServiceForProject = protocolServiceForProjects.get(id);
		if(null == protocolServiceForProject){
			ProtocolService protocolService = new ProtocolService(id);
			protocolServiceForProjects.put(id, protocolService);
		}
		return protocolServiceForProjects.get(id);
	}
}
