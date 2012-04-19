package com.simpleDbVersion.commandLine;

import java.io.File;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.simpleDbVersion.domain.SimpleDbVersion;
import com.simpleDbVersion.domain.factory.SimpleDbVersionFactory;
import com.simpleDbVersion.installer.DBInstaller;

public class CommandLineAppMain {

	public static void main(String... args) {
		final SimpleDbVersion simpleDbVersion = getSimpleDbVersion(args);

		simpleDbVersion.install();

		System.out.println("DB INSTALLED - " + simpleDbVersion.currentVersion());

	}

	public static SimpleDbVersion getSimpleDbVersion(String... args) {
		
		final String scriptsPathName = args.length == 0 ? null : args[0];
		final DBInstaller installer = new DBInstaller(scriptsPathName);

		final Properties loadProperties = getPropertiesFile(installer);

		final DataSource dataSource = getDataSource(installer, loadProperties);

		final Map<String, String> wildTags = getWildTags(installer, loadProperties);

		final File scriptsPath = installer.findScriptsPath();

		return createSimpleDbVersion(dataSource, wildTags, scriptsPath);
	}

	private static SimpleDbVersion createSimpleDbVersion(final DataSource dataSource,
			final Map<String, String> wildTags, final File scriptsPath) {

		if (wildTags == null)
			return SimpleDbVersionFactory.create(dataSource, scriptsPath);

		return SimpleDbVersionFactory.create(dataSource, scriptsPath, wildTags);
	}

	private static Map<String, String> getWildTags(final DBInstaller installer, final Properties loadProperties) {
		final Map<String, String> wildTags = installer.getWildTags(loadProperties);
		System.out.println("Wild Tags loaded: " + wildTags);
		return wildTags;
	}

	private static DataSource getDataSource(final DBInstaller installer, Properties loadProperties) {
		final DataSource dataSource = installer.getDataSource(loadProperties);
		System.out.println("Datasource loaded: " + dataSource);
		return dataSource;
	}

	private static Properties getPropertiesFile(DBInstaller installer) {
		final File file = installer.findPropertiesFile();
		final Properties loadProperties = installer.loadProperties(file.getAbsolutePath());
		System.out.println("Properties loaded: " + loadProperties);
		return loadProperties;
	}
	
}
