package com.simpleDbVersion.domain;

public interface ScriptManager<T> {
	
	Long newestScript(Long version);
	T[] availablesScripts(Long version);

}
