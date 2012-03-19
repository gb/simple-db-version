package com.simpleDbVersion.infra;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.simpleDbVersion.domain.VersionManager;

public class VersionFileManager implements VersionManager {
	
	private final List<Long> versions = new ArrayList<Long>();
	
	public VersionFileManager(File scriptsDir) {
		loadVersions(scriptsDir);
	}

	private void loadVersions(File scriptsDir) {
		for (File file : scriptsDir.listFiles()) addVersion(file);
		Collections.sort(versions);
	}
	
	private void addVersion(File file) {
		if (file.isDirectory()) versions.add(getVersionName(file));
	}
	
	private Long getVersionName(File file) {
		return Long.parseLong(file.getName());
	}

	public Long newestVersion() {
		return versions.get(versions.size() - 1);
	}

	public List<Long> availablesVersions() {
		return Collections.unmodifiableList(versions);
	}

}
