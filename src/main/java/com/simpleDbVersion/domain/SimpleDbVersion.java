package com.simpleDbVersion.domain;

public class SimpleDbVersion {
	
	private final VersionRepository versionRepository;
	private final VersionManager versionManager;
	private final VersionScriptManager versionScriptManager;
	private final VersionInstaller versionInstaller;
	
	public SimpleDbVersion(VersionRepository versionRepository, VersionManager versionManager, 
			VersionScriptManager versionScriptManager, VersionInstaller versionInstaller) {
		this.versionRepository = versionRepository;
		this.versionManager = versionManager;
		this.versionScriptManager = versionScriptManager;
		this.versionInstaller = versionInstaller;
	}
	
	public void install() {
		if (!dbIsOutOfVersion()) return;
		upgradeCurrentVersion();
		installNewVersions();
	}
	
	private void upgradeCurrentVersion() {
		versionInstaller.upgradeVersion(versionRepository.currentVersion(), versionRepository.lastScript());
	}
	
	private void installNewVersions() {
		for (Long version : versionManager.availablesVersions())
			if (version > versionRepository.currentVersion()) versionInstaller.installFullVersion(version);
	}

	public boolean dbIsOutOfVersion() {
		if (versionRepository.currentVersion() == null) return true;
		if (versionRepository.currentVersion() < versionManager.newestVersion()) return true;
		return versionScriptManager.newestScript(versionRepository.currentVersion()) > versionRepository.lastScript();
	}

}
