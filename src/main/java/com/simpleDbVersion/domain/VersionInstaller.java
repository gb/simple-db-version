package com.simpleDbVersion.domain;

public interface VersionInstaller {

	void upgradeVersion(Long version, Long lastInstalledScript);
	void installFullVersionsFrom(Long currentVersion);

}
