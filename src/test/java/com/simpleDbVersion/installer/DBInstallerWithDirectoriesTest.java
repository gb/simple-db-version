package com.simpleDbVersion.installer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DBInstallerWithDirectoriesTest {
	
	@Rule
	public ExpectedException error = ExpectedException.none();

	
	@Test
	public void findDefaultScriptPath() {
		final DBInstaller installer = new DBInstaller(null);
		
		error.expectMessage("Could not find resource: scripts");
		error.expect(RuntimeException.class);
		
		installer.findScriptsPath();
	}
	
	@Test
	public void findScriptPath() {
		final DBInstaller installer = new DBInstaller("myScripts/");
		
		error.expectMessage("Could not find resource: myScripts/");
		error.expect(RuntimeException.class);
		
		installer.findScriptsPath();
	}
	
	@Test
	public void findPropertiesFile() {
		final DBInstaller installer = new DBInstaller(null);
		
		error.expectMessage("Could not find resource: db.properties");
		error.expect(RuntimeException.class);
		
		installer.findPropertiesFile();
	}
	

}
