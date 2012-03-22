package com.simpleDbVersion.infra;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.simpleDbVersion.domain.ScriptManager;
import com.simpleDbVersion.domain.VersionInstaller;
import com.simpleDbVersion.domain.VersionManager;

public class VersionFileInstaller implements VersionInstaller {
	
	private final JdbcTemplate jdbcTemplate;
	private final VersionManager versionManager;
	private final ScriptManager<File> versionScriptManager;
	
	public VersionFileInstaller(DataSource dataSource, VersionManager versionManager, ScriptManager<File> versionScriptManager) {
		this.versionManager = versionManager;
		this.versionScriptManager = versionScriptManager;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
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
		for (File file : versionScriptManager.availablesScripts(version))
			installScript(file);
		
		updateInfoAboutCurrentVersion();
	}

	private void installScript(File file) {
		String script = toString(file);
		
		for (String command : script.split("#")) 
			if (!command.trim().isEmpty()) executeScript(file.getName(), command);
	}

	private void executeScript(String scriptName, String command) {
		try {
			jdbcTemplate.execute(command.replaceAll("\r\n", "\n"));
		} catch (Exception exception) {
			updateInfoAboutCurrentVersion();
			throw new RuntimeException("Error trying install script: " + scriptName);
		}
	}
	
	private void updateInfoAboutCurrentVersion() {
		String sql = "insert into db_version (current_version, last_script, install_date) values (?, ?, ?)";
		this.jdbcTemplate.update(sql, new Object[] { 1L , "script.sql", new Date() });
	}

	private String toString(File file) {
		try {
			return IOUtils.toString(new FileInputStream(file));
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

}
