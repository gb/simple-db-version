package com.simpleDbVersion.domain.factory;

import java.io.File;

import javax.sql.DataSource;

import com.simpleDbVersion.domain.SimpleDbVersion;
import com.simpleDbVersion.domain.VersionManager;
import com.simpleDbVersion.domain.VersionRepository;
import com.simpleDbVersion.domain.VersionScriptManager;
import com.simpleDbVersion.infra.VersionDAO;
import com.simpleDbVersion.infra.VersionFileManager;
import com.simpleDbVersion.infra.VersionFileScriptManager;

public class SimpleDbVersionFactory {
	
	public static SimpleDbVersion create(DataSource dataSource, File installDir) {
		VersionRepository repository = new VersionDAO(dataSource);
		VersionManager versionManager = new VersionFileManager(installDir);
		VersionScriptManager versionScriptManager = new VersionFileScriptManager(installDir);
		
		return new SimpleDbVersion(repository, versionManager, versionScriptManager, null);
	}

}
