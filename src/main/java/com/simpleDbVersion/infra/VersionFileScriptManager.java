package com.simpleDbVersion.infra;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.simpleDbVersion.domain.VersionScriptManager;

public class VersionFileScriptManager implements VersionScriptManager {
	
	private final File installDir;

	public VersionFileScriptManager(File installDir) {
		this.installDir = installDir;
	}
	
	public Long newestScript(Long version) {
		return newestScriptInTheFolder( getVersionFolder(version) );
	}
	
	private Long newestScriptInTheFolder(File folder) {
		List<Long> scripts = orderedScriptsInFolder(folder);
		if (scripts == null || scripts.isEmpty()) return null;
		
		return scripts.get(scripts.size() - 1);
	}

	private List<Long> orderedScriptsInFolder(File folder) {
		if (folder == null) return null;
		
		List<Long> scripts = new ArrayList<Long>();
		
		for (File file : folder.listFiles()) 
			if (file.isFile()) scripts.add(Long.parseLong(file.getName()));

		Collections.sort(scripts);
		
		return scripts;
	}

	private File getVersionFolder(Long version) {
		File versionFolder = new File(installDir.getPath() + File.separator + version.toString());
		return (!versionFolder.exists() || versionFolder.listFiles().length == 0) ? null : versionFolder;
	}

}
