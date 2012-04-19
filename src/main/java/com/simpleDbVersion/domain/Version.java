package com.simpleDbVersion.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

public class Version {
	
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	private final Date installDate;
	private final Long currentVersion;
	private final Long currentScript;
	private final String lastScript;
	
	public Version(Date installDate, Long currentVersion, Long currentScript, String lastScript) {
		this.installDate = installDate;
		this.currentVersion = currentVersion;
		this.currentScript = currentScript;
		this.lastScript = lastScript;
	}

	public Date getInstallationDate() {
		return installDate;
	}

	public Long getCurrentVersion() {
		return currentVersion;
	}

	public Long getCurrentScript() {
		return currentScript;
	}

	public String getLastScript() {
		return lastScript;
	}
	
	@Override
	public String toString() {
		return "Current Version: " + currentVersion + "\n" +
			   "Current Script: " + lastScript + "\n" +
			   "Install Date: " + format(installDate);
	}
	
	private String format(Date date) {
		if (date == null) return "";
		return dateFormatter.format(date);
	}
	
	public static final class VersionMapper implements RowMapper<Version> {

	    public Version mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	Version version = new Version(
	    			rs.getDate("install_date"), 
	    			rs.getLong("version"), 
	    			rs.getLong("current_script"), 
	    			rs.getString("last_script")
	    	);
	    	
	        return version;
	    }
	    
	}
	
}
