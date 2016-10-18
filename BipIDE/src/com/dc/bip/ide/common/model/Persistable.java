package com.dc.bip.ide.common.model;

public interface Persistable<T> {
	void persist();
	T unPersist();
}
