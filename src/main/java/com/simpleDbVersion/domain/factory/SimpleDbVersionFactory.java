package com.simpleDbVersion.domain.factory;

import java.io.File;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.simpleDbVersion.domain.ScriptManager;
import com.simpleDbVersion.domain.SimpleDbVersion;
import com.simpleDbVersion.domain.VersionInstaller;
import com.simpleDbVersion.domain.VersionManager;
import com.simpleDbVersion.domain.VersionRepository;
import com.simpleDbVersion.infra.ScriptFileManager;
import com.simpleDbVersion.infra.VersionDAO;
import com.simpleDbVersion.infra.VersionFileInstaller;
import com.simpleDbVersion.infra.VersionFileManager;
import com.simpleDbVersion.installer.DBInstaller;

public class SimpleDbVersionFactory {

	private SimpleDbVersionFactory() {
	}

	public static SimpleDbVersion createWith(String[] args) {
		final String scriptsPathName = args.length == 0 ? null : args[0];
		final DBInstaller installer = new DBInstaller(scriptsPathName);
		final File propertiesFile = installer.findPropertiesFile();
		final Properties loadProperties = installer.loadProperties(propertiesFile.getAbsolutePath());
		final DataSource dataSource = installer.getDataSource(loadProperties);
		final Map<String, String> wildTags = installer.getWildTags(loadProperties);
		final File scriptsPath = installer.findScriptsPath();

		return createSimpleDbVersion(dataSource, wildTags, scriptsPath);
	}

	private static SimpleDbVersion createSimpleDbVersion(final DataSource dataSource,
			final Map<String, String> wildTags, final File scriptsPath) {

		if (wildTags == null)
			return SimpleDbVersionFactory.create(dataSource, scriptsPath);

		return SimpleDbVersionFactory.create(dataSource, scriptsPath, wildTags);
	}

	public static SimpleDbVersion create(DataSource dataSource, File installDir) {
		VersionRepository repository = new VersionDAO(dataSource);
		VersionManager versionManager = new VersionFileManager(installDir);
		ScriptManager<File> versionScriptManager = new ScriptFileManager(installDir);
		VersionInstaller versionInstaller = new VersionFileInstaller(repository, versionManager, versionScriptManager);

		return new SimpleDbVersion(repository, versionManager, versionScriptManager, versionInstaller);
	}

	public static SimpleDbVersion create(DataSource dataSource, File installDir, Map<String, String> wildTags) {
		VersionRepository repository = new VersionDAO(dataSource);
		VersionManager versionManager = new VersionFileManager(installDir);
		ScriptManager<File> versionScriptManager = new ScriptFileManager(installDir);
		VersionInstaller versionInstaller = new VersionFileInstaller(repository, versionManager, versionScriptManager,
				wildTags);

		return new SimpleDbVersion(repository, versionManager, versionScriptManager, versionInstaller);
	}

}
