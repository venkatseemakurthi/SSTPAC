package com.macys.sstpac.databaseConfigure;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("dbConfiguration")
public  class DBConfiguration {

	@Autowired
	private JdbcTemplate mcomReportingjdbcTemplate;

	@Autowired
	private JdbcTemplate mcomStandByjdbcTemplate;

	@Autowired
	private JdbcTemplate bcomReportingjdbcTemplate;

	@Autowired
	private JdbcTemplate bcomStandByjdbcTemplate;

	@Autowired
	private JdbcTemplate mysqldbjdbcTemplate;

	@Autowired
	private Properties mySqlDbQueries;

	@Autowired
	private Properties db2SqlDbQueries;
	
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public final JdbcTemplate getJdbcTemplate(String database) {
		JdbcTemplate jdbcTemplate = null;
		if (database.equals("MCOMReporting"))
			jdbcTemplate = mcomReportingjdbcTemplate;
		else if (database.equals("MCOMStandBy"))
			jdbcTemplate = mcomStandByjdbcTemplate;
		else if (database.equals("BCOMReporting"))
			jdbcTemplate = bcomReportingjdbcTemplate;
		else if (database.equals("BCOMStandBy"))
			jdbcTemplate = bcomStandByjdbcTemplate;
		else if (database.equals("MySql"))
			jdbcTemplate = mysqldbjdbcTemplate;
		return jdbcTemplate;
	}

	public final String getMySqlQuery(String querykey) throws Exception {
		return mySqlDbQueries.getProperty(querykey);
	}

	public final String getDb2SqlQuery(String querykey) throws Exception {
		return db2SqlDbQueries.getProperty(querykey);
	}
	
	public final int processDMLQuery(String query, Object[] data) throws Exception {
		return jdbcTemplate.update(getMySqlQuery(query),data);
	}
	
}
