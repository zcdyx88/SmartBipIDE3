package com.dc.bip.ide.repository;

public interface IRepository<T, PK> {
	
	public T get(PK id);

}
