package com.simpleDbVersion.installer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.simpleDbVersion.infra.Logging;

public class DBInstaller {

	private final String scriptsPathName;

	public DBInstaller(String scriptsPathName) {
		this.scriptsPathName = scriptsPathName;
	}

	public Map<String, String> getWildTags(Properties properties) {
		String allTags = properties.getProperty("db.tags");

		if (allTags == null)
			return null;

		HashMap<String, String> wildTags = new HashMap<String, String>();

		String[] chavesValores = allTags.split(",");

		for (String chaveValor : chavesValores) {
			String[] keyValue = chaveValor.split("=");
			wildTags.put(keyValue[0], keyValue[1]);
		}

		return wildTags;
	}

	public DataSource getDataSource(Properties properties) {
		String driver = properties.getProperty("db.driver");
		String url = properties.getProperty("db.url");
		String username = properties.getProperty("db.username");
		String password = properties.getProperty("db.password");

		final DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);

		return dataSource;
	}

	public File findPropertiesFile() {
		return findFile("db.properties");
	}

	public File findScriptsPath() {
		String name = scriptsPathName == null ? "scripts" : scriptsPathName;
		return findFile(name);
	}

	public Properties loadProperties(String propertiesFileName) {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(propertiesFileName));
			return properties;
		} catch (IOException e) {
			Logging.warning("Error loading properties from " + propertiesFileName + " : " + e.getMessage());
			return null;
		}
	}

	private File findFile(String name) {
		File file = new File(name);

		if (!file.exists()) {
			file = findResource(name);

			if (file == null || !file.exists())
				throw new RuntimeException("Could not find resource: " + name);
		}

		Logging.info("Loading file: " + file + " from: " + file.getAbsolutePath());

		return file;
	}

	private File findResource(String name) {
		try {
			URL resource = getClass().getResource("/" + name);

			if (resource == null)
				return null;

			return new File(resource.toURI());
		} catch (URISyntaxException e) {
			Logging.warning("Error loading resource " + name + " : " + e.getMessage());
		}

		return null;
	}

}
