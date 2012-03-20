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
		if (scriptsOfCurrentVersionIsOutdate()) upgradeCurrentVersion();
		if (versionIsOutdate()) installNewVersions();
	}
	
	private void upgradeCurrentVersion() {
		versionInstaller.upgradeVersion(versionRepository.currentVersion(), versionRepository.lastScript());
	}
	
	private void installNewVersions() {
		versionInstaller.installFullVersionFrom(versionRepository.currentVersion());
	}

	public boolean versionIsOutdate() {
		return neverWasInstalled() || versionRepository.currentVersion() < versionManager.newestVersion();
	}
	
	public boolean scriptsOfCurrentVersionIsOutdate() {
		return !neverWasInstalled() && versionScriptManager.newestScript(versionRepository.currentVersion()) > versionRepository.lastScript();
	}
	
	public boolean neverWasInstalled() {
		return versionRepository.currentVersion() == null;
	}

}
