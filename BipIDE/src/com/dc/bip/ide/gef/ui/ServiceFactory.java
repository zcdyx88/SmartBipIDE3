package com.dc.bip.ide.gef.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.requests.SimpleFactory;

import com.dc.bip.ide.gef.model.ParamContainer;

public class ServiceFactory extends SimpleFactory {
	
	private ParamContainer model;
	
	public ServiceFactory(Class Class,ParamContainer model) {
		super(Class);
		this.model = model;
	}

	public Object getNewObject() {
		try {
			return model;
		} catch (Exception exc) {
			return null;
		}
	}
	
}
