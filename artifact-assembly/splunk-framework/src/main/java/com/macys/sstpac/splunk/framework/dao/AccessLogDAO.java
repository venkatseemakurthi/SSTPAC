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
import com.macys.sstpac.splunk.framework.bean.AccessLogVO;
import com.macys.sstpac.splunk.framework.bean.LogVO;

@Component
public class AccessLogDAO extends DBConfiguration {

	public void insertAccessLog(JdbcTemplate jdbcTemplate, int searchId,
			AccessLogVO accessLogVO) throws Exception {
		try {
			super.setJdbcTemplate(jdbcTemplate);
			processDMLQuery("", new Object[] { accessLogVO.getUserId(),
					searchId, accessLogVO.getUrl(), accessLogVO.getLogTime() });
			insertCookies(accessLogVO.getCookiesMap(), searchId);
			insertAccessLogAttributes(jdbcTemplate,
					accessLogVO.getAttributeMap(), searchId);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void insertAccessLogAttributes(JdbcTemplate jdbcTemplate,
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

	private void insertCookies(HashMap<String, String> cookiesMap, int searchId)
			throws Exception {
		try {
			for (Map.Entry<String, String> cookie : cookiesMap.entrySet()) {
				processDMLQuery("", new Object[] { searchId, cookie.getKey(),
						cookie.getValue() });
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public List<LogVO> getAccessLog(final JdbcTemplate jdbcTemplate,
			int searchId) throws Exception {
		try {
			return jdbcTemplate.query((String) getMySqlQuery("SELECT_MENU"),
					new Object[] { searchId }, new RowMapper<LogVO>() {
						@Override
						public AccessLogVO mapRow(ResultSet accessLogResultSet,
								int rownumber) throws SQLException {
							AccessLogVO accessLogVO =  new AccessLogVO();
							accessLogVO.setUrl(accessLogResultSet
									.getString("access_log"));
							accessLogVO.setLogTime(accessLogResultSet
									.getTimestamp("created"));
							accessLogVO.setUserId(accessLogResultSet
									.getInt("user_id"));
							accessLogVO.setCookiesMap(getCookies(jdbcTemplate,
									accessLogResultSet.getInt("access_id")));
							accessLogVO.setAttributeMap(getAccessLogAttributes(
									jdbcTemplate,
									accessLogResultSet.getInt("access_id")));
							return accessLogVO;
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private HashMap<String, String> getAccessLogAttributes(
			JdbcTemplate jdbcTemplate, int accessLogid) {
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
					new Object[] { accessLogid }, mapExtractor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attributeMap;
	}

	private HashMap<String, String> getCookies(JdbcTemplate jdbcTemplate,
			int accessLogid) {
		HashMap<String, String> cookiesMap = new HashMap<String, String>();
		ResultSetExtractor<HashMap<String, String>> mapExtractor = null;
		try {
			mapExtractor = new ResultSetExtractor<HashMap<String, String>>() {
				public HashMap<String, String> extractData(
						ResultSet cookiesResultSet) throws SQLException {
					HashMap<String, String> cookiesMap = new HashMap<String, String>();
					while (cookiesResultSet.next()) {
						cookiesMap.put(
								cookiesResultSet.getString("cookie_key"),
								cookiesResultSet.getString("cookie_value"));
					}
					return cookiesMap;
				}
			};
			cookiesMap = (HashMap<String, String>) jdbcTemplate.query("",
					new Object[] { accessLogid }, mapExtractor);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return cookiesMap;
	}

}
