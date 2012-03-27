package com.simpleDbVersion.domain;

public class SimpleDbVersion {

	private final VersionRepository versionRepository;
	private final VersionManager versionManager;
	private final ScriptManager<?> versionScriptManager;
	private final VersionInstaller versionInstaller;

	public SimpleDbVersion(VersionRepository versionRepository,
			VersionManager versionManager,
			ScriptManager<?> versionScriptManager,
			VersionInstaller versionInstaller) {

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
		versionInstaller.upgradeVersion(versionRepository.currentVersionNumber(), versionRepository.lastScript());
	}

	private void upgradeToNewestVersion() {
		if (!versionIsOutdate()) return;
		versionInstaller.installFullVersionsFrom(versionRepository.currentVersionNumber());
	}

	public boolean versionIsOutdate() {
		return neverWasInstalled() || versionRepository.currentVersionNumber() < versionManager.newestVersion();
	}

	public boolean scriptsOfCurrentVersionIsOutdate() {
		return !neverWasInstalled()	&& versionScriptManager.newestScript(versionRepository.currentVersionNumber()) > versionRepository.lastScript();
	}

	public boolean neverWasInstalled() {
		return versionRepository.currentVersionNumber() == null;
	}
	
	public Version currentVersion() {
		return versionRepository.currentVersion();
	}

}
