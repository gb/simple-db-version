package com.simpleDbVersion.domain;

public interface VersionRepository {
	
	Long currentVersion();
	Long lastScript();

}
