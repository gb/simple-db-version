package com.simpleDbVersion.infra;

import java.util.Date;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.simpleDbVersion.domain.Version;
import com.simpleDbVersion.domain.Version.VersionMapper;
import com.simpleDbVersion.domain.VersionRepository;

public class VersionDAO implements VersionRepository {

	private final JdbcTemplate jdbcTemplate;

	public VersionDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Long currentVersionNumber() {
		try {
			return this.jdbcTemplate.queryForLong(LAST_VERSION_NUMBER_SQL);
		} catch (EmptyResultDataAccessException e) {
			error(e, "currentVersionNumber");
			return null;
		}
	}

	@Override
	public Long lastScript() {
		try {
			return this.jdbcTemplate.queryForLong(LAST_SCRIPT_SQL);
		} catch (EmptyResultDataAccessException e) {
			error(e, "lastScript");
			return 0l;
		}
	}
	
	@Override
	public Version currentVersion() {
		try {
			return (Version) this.jdbcTemplate.queryForObject(LAST_VERSION_SQL, new VersionMapper()); 			
		}  catch (EmptyResultDataAccessException e) {
			error(e, "currentVersion");
			return null;
		}
	}
	
	@Override
	public void updateInfoAboutCurrentVersion(Version version) {
		String sql;
		
		if (!hasVersion(version))
			sql = "insert into db_version (current_script, install_date, last_script, version) values (?, ?, ?, ?)";
		else
			sql = "update db_version set current_script = ?, install_date = ?, last_script = ? where version = ?";
		
		this.jdbcTemplate.update(sql, new Object[] { version.getCurrentScript(), new Date(), version.getLastScript(), version.getCurrentVersion() });
	}
	
	private boolean hasVersion(Version version) {
		Long currentVersionNumber = currentVersionNumber();
		return currentVersionNumber != null && currentVersionNumber == version.getCurrentVersion();
	}

	@Override
	public void executeScript(String sql) {
		jdbcTemplate.execute(sql);
	}

	private void error(EmptyResultDataAccessException e, String fieldName) {
		System.err.println(String.format("<Error retrieving %s> %s ",fieldName, e.getMessage()));
	}
	
	private static final String WHERE_MAX_INSTALL_DATE = "WHERE INSTALL_DATE = (SELECT MAX(INSTALL_DATE) FROM DB_VERSION)";
	
	private static final String LAST_VERSION_NUMBER_SQL = "SELECT VERSION FROM DB_VERSION " + WHERE_MAX_INSTALL_DATE;
	
	private static final String LAST_SCRIPT_SQL = "SELECT CURRENT_SCRIPT FROM DB_VERSION " + WHERE_MAX_INSTALL_DATE;
	
	private static final String LAST_VERSION_SQL = "SELECT * FROM DB_VERSION " + WHERE_MAX_INSTALL_DATE;

}
