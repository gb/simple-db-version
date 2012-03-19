package com.simpleDbVersion;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.simpleDbVersion.domain.VersionScriptManager;
import com.simpleDbVersion.infra.VersionFileScriptManager;

public class VersionScriptManagerTest {
	
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    
    private VersionScriptManager versionScriptManager;
    
    @Before
    public void setUp() throws IOException {
    	testFolder.newFolder("1");
    	
    	File version = testFolder.newFolder("2");
    	
    	new File(version, "1").createNewFile();
    	new File(version, "3").createNewFile();
    	new File(version, "2").createNewFile();
    	
    	versionScriptManager = new VersionFileScriptManager(testFolder.getRoot());
    }
    
    @Test
    public void shouldReturnNullWhenAVersionFolderIsEmpty() {
    	assertEquals(null, versionScriptManager.newestScript(1L));
    }
    
    @Test
    public void shouldReturnNullWhenAVersionNotExists() {
    	assertEquals(null, versionScriptManager.newestScript(9L));
    }
    
    @Test
    public void testLastVersionScript() {
    	assertEquals(new Long(3), versionScriptManager.newestScript(2L));
    }

}
