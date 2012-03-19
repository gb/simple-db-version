package com.simpleDbVersion.domain;

public interface VersionInstaller {

	void installFullVersion(Long version);
	void upgradeVersion(Long version, Long lastInstalledScript);

}
