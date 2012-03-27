package com.simpleDbVersion.infra;

import java.util.Date;

import javax.sql.DataSource;

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
		return this.jdbcTemplate.queryForLong(LAST_VERSION_NUMBER_SQL);
	}

	@Override
	public Long lastScript() {
		return this.jdbcTemplate.queryForLong(LAST_SCRIPT_SQL);
	}
	
	@Override
	public Version currentVersion() {
		return (Version) this.jdbcTemplate.queryForObject(LAST_VERSION_SQL, new VersionMapper()); 
	}
	
	@Override
	public void updateInfoAboutCurrentVersion(Version version) {
		String sql = "insert into db_version (version, current_script, install_date, script_file) values (?, ?, ?, ?)";
		this.jdbcTemplate.update(sql, new Object[] { version.getCurrentVersion(), version.getCurrentScript(), new Date(), version.getLastScript() });
	}
	
	@Override
	public void executeScript(String sql) {
		jdbcTemplate.execute(sql);
	}

	private static final String WHERE_MAX_INSTALL_DATE = "WHERE INSTALL_DATE = (SELECT MAX(INSTALL_DATE) FROM DB_VERSION)";
	
	private static final String LAST_VERSION_NUMBER_SQL = "SELECT VERSION FROM DB_VERSION " + WHERE_MAX_INSTALL_DATE;
	
	private static final String LAST_SCRIPT_SQL = "SELECT LAST_SCRIPT FROM DB_VERSION " + WHERE_MAX_INSTALL_DATE;
	
	private static final String LAST_VERSION_SQL = "SELECT FROM * DB_VERSION " + WHERE_MAX_INSTALL_DATE;

}
