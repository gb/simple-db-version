package com.simpleDbVersion.infra;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.simpleDbVersion.domain.VersionManager;

public class VersionFileManager implements VersionManager {
	
	private final List<Long> versions = new ArrayList<Long>();
	
	public VersionFileManager(File scriptsDir) {
		loadVersions(scriptsDir);
	}
	
	@Override
	public Long newestVersion() {
		return versions.get(versions.size() - 1);
	}
	
	@Override
	public List<Long> availablesVersions() {
		return Collections.unmodifiableList(versions);
	}

	private void loadVersions(File scriptsDir) {
		for (File file : scriptsDir.listFiles(onlyNumericFolders)) addVersion(file);
		Collections.sort(versions);
	}
	
	private FileFilter onlyNumericFolders = new FileFilter() {

		@Override
		public boolean accept(File pathname) {
			return pathname.getName().matches("[0-9]*");
		}
	};
	
	private void addVersion(File file) {
		if (file.isDirectory()) versions.add(getVersionName(file));
	}
	
	private Long getVersionName(File file) {
		return Long.parseLong(file.getName());
	}

}
