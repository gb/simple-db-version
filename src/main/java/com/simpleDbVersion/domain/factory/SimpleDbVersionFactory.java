package com.simpleDbVersion.domain.factory;

import java.io.File;
import java.util.Map;

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

public class SimpleDbVersionFactory {
	
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
		VersionInstaller versionInstaller = new VersionFileInstaller(repository, versionManager, versionScriptManager);
		
		return new SimpleDbVersion(repository, versionManager, versionScriptManager, versionInstaller);
	}

}
