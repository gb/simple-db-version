package com.simpleDbVersion.installer;

import java.io.File;
import java.net.URL;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.simpleDbVersion.installer.DBInstaller;

public class DBInstallerWithNoDirectoriesTest {
	
	@Rule
	public ExpectedException error = ExpectedException.none();
	
	@BeforeClass
	public static void setup() {
		final File scriptsPath = getScriptsPath();
		createPath(scriptsPath);
	}

	@AfterClass
	public static void goingDown() {
		final File scriptsPath = getScriptsPath();
		scriptsPath.delete();
		//Assert.assertFalse(scriptsPath.exists());
	}

	@Test
	public void findDefaultScriptPath(){
		final DBInstaller installer = new DBInstaller(null);
		final File file = installer.findScriptsPath();
		
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());
	}
	
	@Test
	public void findScriptPathByParameter() {
		File otherPath = new File(getCurrentDir(), "otherScriptPath");
		createPath(otherPath);
		
		final DBInstaller installer = new DBInstaller("otherScriptPath");
		final File file = installer.findScriptsPath();
		
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());
		
		otherPath.delete();
	}
	
	@Test
	public void findPropertiesFile() {
		File properties = new File(getCurrentDir(), "db.properties");
		createPath(properties);
		
		final DBInstaller installer = new DBInstaller(null);
		final File file = installer.findPropertiesFile();
		
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());
		
		properties.delete();
	}
	
	private static void createPath(final File scriptsPath) {
		boolean mkdirs = scriptsPath.mkdirs();
		
		Assert.assertTrue(mkdirs);
		Assert.assertTrue(scriptsPath.exists());
	}

	private static File getScriptsPath() {
		final String currentDir = getCurrentDir();
		final File scriptsFile = new File(currentDir, "scripts");
		return scriptsFile;
	}

	private static String getCurrentDir() {
		final URL url = DBInstallerWithNoDirectoriesTest.class.getClassLoader().getResource(".");
		final String currentDir = url.toString().replaceAll("file:/", "");
		return currentDir;
	}
	
}
