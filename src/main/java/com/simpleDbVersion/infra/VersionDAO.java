package com.simpleDbVersion.infra;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.simpleDbVersion.domain.VersionRepository;

public class VersionDAO implements VersionRepository {

	private final JdbcTemplate jdbcTemplate;

	public VersionDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public Long currentVersion() {
		return this.jdbcTemplate.queryForLong(LAST_VERSION_SQL);
	}

	public Long lastScript() {
		return this.jdbcTemplate.queryForLong(LAST_SCRIPT_SQL);
	}

	private static final String WHERE_MAX_INSTALL_DATE = "WHERE INSTALL_DATE = (SELECT MAX(INSTALL_DATE) FROM DB_VERSION)";

	private static final String LAST_VERSION_SQL = "SELECT VERSION FROM DB_VERSION " + WHERE_MAX_INSTALL_DATE;

	private static final String LAST_SCRIPT_SQL = "SELECT LAST_SCRIPT FROM DB_VERSION " + WHERE_MAX_INSTALL_DATE;

}
