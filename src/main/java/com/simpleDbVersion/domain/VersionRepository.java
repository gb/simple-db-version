package com.simpleDbVersion.domain;

public interface VersionRepository {
	
	Long currentVersionNumber();
	Long lastScript();
	Version currentVersion();
	void updateInfoAboutCurrentVersion(Version version);
	void executeScript(String sql);

}
