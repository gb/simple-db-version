package com.simpleDbVersion.infra;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import com.simpleDbVersion.domain.ScriptManager;
import com.simpleDbVersion.domain.Version;
import com.simpleDbVersion.domain.VersionInstaller;
import com.simpleDbVersion.domain.VersionManager;
import com.simpleDbVersion.domain.VersionRepository;

public class VersionFileInstaller implements VersionInstaller {
	
	private final VersionRepository versionRepository;
	private final VersionManager versionManager;
	private final ScriptManager<File> versionScriptManager;
	
	private Long version;
	private Long currentScript;
	private String currentFile;
	
	public VersionFileInstaller(VersionRepository versionRepository, VersionManager versionManager, ScriptManager<File> versionScriptManager) {
		this.versionManager = versionManager;
		this.versionScriptManager = versionScriptManager;
		this.versionRepository = versionRepository;
	}
	
	@Override
	public void upgradeVersion(Long version, Long lastInstalledScript) {
		for (File file : versionScriptManager.availablesScripts(version))
			if (numberOfScript(file) > lastInstalledScript) installScript(file);
		
		updateInfoAboutCurrentVersion();
	}
	
	@Override
	public void installFullVersionsFrom(Long currentVersion) {
		for (Long version : versionManager.availablesVersions())
			if (version > currentVersion) installAllScriptsOfVersion(version);
		
		updateInfoAboutCurrentVersion();
	}
	
	private Long numberOfScript(File file) {
		return Long.parseLong(file.getName().replaceAll("[^\\d]", ""));
	}
	
	private void installAllScriptsOfVersion(Long version) {
		this.version = version;
		
		for (File file : versionScriptManager.availablesScripts(version))
			installScript(file);
		
		updateInfoAboutCurrentVersion();
	}

	private void installScript(File file) {
		currentFile = file.getName();
		currentScript = numberOfScript(file);
		String script = toString(file);
		
		for (String command : script.split("#")) 
			if (!command.trim().isEmpty()) executeScript(file.getName(), command);
	}

	private void executeScript(String scriptName, String command) {
		try {
			versionRepository.executeScript(command.replaceAll("\r\n", "\n"));
		} catch (Exception exception) {
			updateInfoAboutCurrentVersion();
			throw new RuntimeException("Error trying install script: " + scriptName);
		}
	}
	
	private void updateInfoAboutCurrentVersion() {
		Version newVersion = new Version(new Date(), version, currentScript, currentFile);
		versionRepository.updateInfoAboutCurrentVersion(newVersion);
	}

	private String toString(File file) {
		try {
			return IOUtils.toString(new FileInputStream(file));
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

}
