package com.dc.bip.ide.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.dc.bip.ide.common.ConfigLoadAble;
import com.dc.bip.ide.objects.ProtocolInService;
import com.dc.bip.ide.util.FileUtils;
import com.dc.bip.ide.util.ObjectUtils;
import com.dc.bip.ide.util.ProtocolUtils;
import com.dc.bip.ide.views.objects.ProtocolInNode;
import com.dc.bip.ide.views.objects.ProtocolOutNode;

public class ProtocolService implements ConfigLoadAble {

	private static final String IN_PROTOCOL_POSTFIX = "protolin";
	private static final String OUT_PROTOCOL_POSTFIX = "protolout";

	private static ProtocolService protocolService;
	private final ConcurrentHashMap<String, ProtocolInService> inProtocolMap;
	private final ConcurrentHashMap<String, ProtocolInService> outProtocolMap;
	private final String projectName;

	public ProtocolService(String projectName) {
		inProtocolMap = new ConcurrentHashMap<String, ProtocolInService>();
		outProtocolMap = new ConcurrentHashMap<String, ProtocolInService>();
		this.projectName = projectName;
		load();
	}

	@Override
	public void load() {
		String protocoldir = new StringBuilder("/").append(projectName).append("/dev/services/protocol").toString();
		List<File> protocolFiles = FileUtils.getFilesFromWorkSpace(protocoldir);
		for (File protocolFile : protocolFiles) {
			if (protocolFile.getAbsolutePath().endsWith(IN_PROTOCOL_POSTFIX)) {
				ProtocolInNode protocolInNode = (ProtocolInNode) ObjectUtils.getObject(protocolFile);
				inProtocolMap.put(protocolInNode.getProtocolInService().getProtocolName(),
						protocolInNode.getProtocolInService());
			}
		}

		protocolFiles = FileUtils.getFilesFromWorkSpace(protocoldir);
		for (File protocolFile : protocolFiles) {
			if (protocolFile.getAbsolutePath().endsWith(OUT_PROTOCOL_POSTFIX)) {
				ProtocolOutNode protocolInNode = (ProtocolOutNode) ObjectUtils.getObject(protocolFile);
				outProtocolMap.put(protocolInNode.getProtocolInService().getProtocolName(),
						protocolInNode.getProtocolInService());
			}
		}
	}

	@Override
	public void reload() {
		// TODO 这里实现判断文件是否更新然后再加载
		load();
	}

	public ProtocolInService getInProtocolInfo(String id) {
		reload();
		return inProtocolMap.get(id);
	}

	public ProtocolInService getOutProtocolInfo(String id) {
		reload();
		return outProtocolMap.get(id);
	}

	public Collection<ProtocolInService> getAllInProtocols() {
		reload();
		return inProtocolMap.values();
	}

	public Collection<ProtocolInService> getAllOutProtocols() {
		reload();
		return outProtocolMap.values();
	}
}
