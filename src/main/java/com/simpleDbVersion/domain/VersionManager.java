package com.simpleDbVersion.domain;

import java.util.List;

public interface VersionManager {
	
	Long newestVersion();
	List<Long> availablesVersions();

}
