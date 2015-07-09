package com.macys.sstpac.splunk.framework.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.macys.sstpac.databaseConfigure.DBConfiguration;
import com.macys.sstpac.splunk.framework.bean.D2CLogVO;
import com.macys.sstpac.splunk.framework.bean.LogVO;

@Component
public class D2CLogDAO extends DBConfiguration {

	public void insertD2CLog(JdbcTemplate jdbcTemplate, int searchId,
			D2CLogVO d2cLogVO) throws Exception {
		super.setJdbcTemplate(jdbcTemplate);
		try {
			processDMLQuery("", new Object[] { searchId, d2cLogVO.getUserId(),
					d2cLogVO.getD2cLog(), d2cLogVO.getLogTime() });
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public List<LogVO> getD2CLog(final JdbcTemplate jdbcTemplate, int searchId)
			throws Exception {
		try {
			return jdbcTemplate.query((String) getMySqlQuery("SELECT_MENU"),
					new Object[] { searchId }, new RowMapper<LogVO>() {
						@Override
						public D2CLogVO mapRow(ResultSet d2CLogResultSet,
								int rownumber) throws SQLException {
							D2CLogVO d2CLogVO = new D2CLogVO();
							d2CLogVO.setD2cLog(d2CLogResultSet
									.getString("ext_log"));
							d2CLogVO.setUserId(d2CLogResultSet
									.getInt("user_id"));
							d2CLogVO.setLogTime(d2CLogResultSet
									.getTimestamp("created"));
							return d2CLogVO;
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
