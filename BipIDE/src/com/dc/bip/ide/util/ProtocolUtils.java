package com.dc.bip.ide.util;

import java.io.File;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import com.dc.bip.ide.objects.ProtocolInService;
import com.dc.bip.ide.views.objects.ProtocolInNode;
import com.dc.bip.ide.views.objects.ProtocolOutNode;

public class ProtocolUtils {

	private static final String IN_PROTOCOL_POSTFIX = "protolin";
	private static final String OUT_PROTOCOL_POSTFIX = "protolout";

	public static List<ProtocolInService> getAllInProtocols(String projectName) {
		List<ProtocolInService> protocols = new ArrayList<ProtocolInService>();
		String protocoldir = new StringBuilder("/").append(projectName).append("/dev/services/protocol").toString();
		List<File> protocolFiles = FileUtils.getFilesFromWorkSpace(protocoldir);
		for (File protocolFile : protocolFiles) {
			if (protocolFile.getAbsolutePath().endsWith(IN_PROTOCOL_POSTFIX)) {
				ProtocolInNode protocolInNode = (ProtocolInNode) ObjectUtils.getObject(protocolFile);
				protocols.add(protocolInNode.getProtocolInService());
			}
		}
		return protocols;
	}

	public static List<ProtocolInService> getAllOutProtocols(String projectName) {
		List<ProtocolInService> protocols = new ArrayList<ProtocolInService>();
		String protocoldir = new StringBuilder("/").append(projectName).append("/dev/services/protocol").toString();
		List<File> protocolFiles = FileUtils.getFilesFromWorkSpace(protocoldir);
		for (File protocolFile : protocolFiles) {
			if (protocolFile.getAbsolutePath().endsWith(OUT_PROTOCOL_POSTFIX)) {
				ProtocolOutNode protocolInNode = (ProtocolOutNode) ObjectUtils.getObject(protocolFile);
				protocols.add(protocolInNode.getProtocolInService());
			}
		}
		return protocols;
	}

}
