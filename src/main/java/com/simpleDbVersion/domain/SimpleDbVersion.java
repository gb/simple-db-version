package com.simpleDbVersion.domain;

public class SimpleDbVersion {
	
	private final VersionRepository versionRepository;
	private final VersionManager versionManager;
	private final ScriptManager<?> versionScriptManager;
	private final VersionInstaller versionInstaller;
	
	public SimpleDbVersion(VersionRepository versionRepository, VersionManager versionManager, 
			ScriptManager<?> versionScriptManager, VersionInstaller versionInstaller) {
		this.versionRepository = versionRepository;
		this.versionManager = versionManager;
		this.versionScriptManager = versionScriptManager;
		this.versionInstaller = versionInstaller;
	}
	
	public void install() {
		installNewScriptsOfCurrentVersion();
		upgradeToNewestVersion();
	}
	
	private void installNewScriptsOfCurrentVersion() {
		if (!scriptsOfCurrentVersionIsOutdate()) return;
		versionInstaller.upgradeVersion(versionRepository.currentVersion(), versionRepository.lastScript());
	}
	
	private void upgradeToNewestVersion() {
		if (!versionIsOutdate()) return;
		versionInstaller.installFullVersionsFrom(versionRepository.currentVersion());
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
