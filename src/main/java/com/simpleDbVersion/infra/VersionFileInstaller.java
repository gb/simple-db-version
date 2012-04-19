package com.simpleDbVersion.infra;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
	private final Map<String, String> wildTags;

	private Long version;
	private Long currentScript;
	private String currentFile;

	public VersionFileInstaller(VersionRepository versionRepository, VersionManager versionManager,
			ScriptManager<File> versionScriptManager) {
		this(versionRepository, versionManager, versionScriptManager, new HashMap<String, String>());
	}

	public VersionFileInstaller(VersionRepository versionRepository, VersionManager versionManager,
			ScriptManager<File> versionScriptManager, Map<String, String> wildTags) {
		this.versionManager = versionManager;
		this.versionScriptManager = versionScriptManager;
		this.versionRepository = versionRepository;
		this.wildTags = wildTags;
	}

	@Override
	public void upgradeVersion(Long version, Long lastInstalledScript) {
		this.version = version;
		for (File file : versionScriptManager.availablesScripts(version))
			if (numberOfScript(file) > lastInstalledScript)
				installScript(file);

		//updateInfoAboutCurrentVersion();
	}

	@Override
	public void installFullVersionsFrom(Long currentVersion) {
		for (Long version : versionManager.availablesVersions())
			if (currentVersion == null || version > currentVersion)
				installAllScriptsOfVersion(version);

		//updateInfoAboutCurrentVersion();
	}

	private Long numberOfScript(File file) {
		return Long.parseLong(file.getName().replaceAll("[^\\d]", ""));
	}

	private void installAllScriptsOfVersion(Long version) {
		this.version = version;

		for (File file : versionScriptManager.availablesScripts(version))
			installScript(file);

		//		updateInfoAboutCurrentVersion();
	}

	private void installScript(File file) {
		currentFile = file.getName();
		System.out.println("Runing " + currentFile);
		currentScript = numberOfScript(file);
		String script = toString(file);

		for (String command : script.split("#"))
			if (!command.trim().isEmpty())
				executeScript(file.getName(), command);

		updateInfoAboutCurrentVersion();
	}

	private void executeScript(String scriptName, String command) {
		try {
			command = replaceWildTags(command);
			versionRepository.executeScript(command.replaceAll("\r\n", "\n"));
			//updateInfoAboutCurrentVersion();
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new RuntimeException("Error trying install script: " + scriptName);
		}
	}

	private String replaceWildTags(String command) {
		if (getWildTags().isEmpty())
			return command;

		for (String key : getWildTags().keySet())
			command = command.replaceAll("\\[" + key + "\\]", getWildTags().get(key));

		return command;
	}

	private void updateInfoAboutCurrentVersion() {
		Version newVersion = new Version(new Date(), version, currentScript, currentFile);
		versionRepository.updateInfoAboutCurrentVersion(newVersion);
	}

	// FIXME exposing method for mocking capabilities /visibility

	protected Map<String, String> getWildTags() {
		return wildTags;
	}

	protected String toString(File file) {
		try {
			return IOUtils.toString(new FileInputStream(file));
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

}
