package com.macys.sstpac.splunk.framework.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.macys.sstpac.databaseConfigure.DBConfiguration;
import com.macys.sstpac.splunk.framework.bean.ApplicationLogVO;
import com.macys.sstpac.splunk.framework.bean.LogVO;

@Component
public class ApplicationLogDAO extends DBConfiguration {

	public void insertApplicationLog(JdbcTemplate jdbcTemplate, int searchId,
			ApplicationLogVO applicationLogVO) throws Exception {
		try {
			super.setJdbcTemplate(jdbcTemplate);
			processDMLQuery(
					"",
					new Object[] { applicationLogVO.getUserId(), searchId,
							applicationLogVO.getApplicationLog(),
							applicationLogVO.getLogLevel(),
							applicationLogVO.getLogTime() });
			insertAppilcationLogAttributes(applicationLogVO.getAttributeMap(),
					searchId);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void insertAppilcationLogAttributes(
			HashMap<String, String> accessLogAttrMap, int searchId)
			throws Exception {
		try {
			for (Map.Entry<String, String> attrMap : accessLogAttrMap
					.entrySet()) {
				processDMLQuery("", new Object[] { searchId, attrMap.getKey(),
						attrMap.getValue() });
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public List<LogVO> getApplicationLog(final JdbcTemplate jdbcTemplate,
			int searchId) throws Exception {
		try {
			return jdbcTemplate.query((String) getMySqlQuery("SELECT_MENU"),
					new Object[] { searchId }, new RowMapper<LogVO>() {
						@Override
						public ApplicationLogVO mapRow(
								ResultSet applicationLogResultSet, int rownumber)
								throws SQLException {
							ApplicationLogVO applicationLogVO = new ApplicationLogVO();
							applicationLogVO
									.setApplicationLog(applicationLogResultSet
											.getString("app_log"));
							applicationLogVO.setUserId(applicationLogResultSet
									.getInt("user_id"));
							applicationLogVO
									.setLogLevel(applicationLogResultSet
											.getString("log_level"));
							applicationLogVO.setLogTime(applicationLogResultSet
									.getTimestamp("created"));
							applicationLogVO
									.setAttributeMap(getApplicationLogAttributes(
											jdbcTemplate,
											applicationLogResultSet
													.getInt("app_id")));
							return applicationLogVO;
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private HashMap<String, String> getApplicationLogAttributes(
			JdbcTemplate jdbcTemplate, int applicationLogId) {
		HashMap<String, String> attributeMap = new HashMap<String, String>();
		ResultSetExtractor<HashMap<String, String>> mapExtractor = null;
		try {
			mapExtractor = new ResultSetExtractor<HashMap<String, String>>() {
				public HashMap<String, String> extractData(
						ResultSet attributeResultSet) throws SQLException {
					HashMap<String, String> attributeMap = new HashMap<String, String>();
					while (attributeResultSet.next()) {
						attributeMap
								.put(attributeResultSet
										.getString("attribute_key"),
										attributeResultSet
												.getString("attribute_value"));
					}
					return attributeMap;
				}
			};
			attributeMap = (HashMap<String, String>) jdbcTemplate.query("",
					new Object[] { applicationLogId }, mapExtractor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attributeMap;
	}
}
