package com.simpleDbVersion;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.simpleDbVersion.domain.VersionManager;
import com.simpleDbVersion.infra.VersionFileManager;

public class VersionFileManagerTest {
	
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    
    private VersionManager versionFileManager;
    
    @Before
    public void setUp() throws IOException {
    	testFolder.newFolder("1");
    	testFolder.newFolder("2");
    	testFolder.newFolder("3");
    	
    	versionFileManager = new VersionFileManager(testFolder.getRoot());
    }
    
    @Test
    public void testAvailablesVersions() {
    	List<Long> expectedVersions = Arrays.asList(1L, 2L, 3L);
    	
    	assertEquals(expectedVersions, versionFileManager.availablesVersions());
    }
    
    @Test
    public void maxVersionTest() {
    	assertEquals(new Long(3), versionFileManager.newestVersion());
    }

}
