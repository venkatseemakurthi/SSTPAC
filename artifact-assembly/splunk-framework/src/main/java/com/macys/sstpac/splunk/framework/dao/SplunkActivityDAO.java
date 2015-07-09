package com.macys.sstpac.splunk.framework.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.macys.sstpac.splunk.framework.bean.AccessLogVO;
import com.macys.sstpac.splunk.framework.bean.ApplicationLogVO;
import com.macys.sstpac.splunk.framework.bean.D2CLogVO;
import com.macys.sstpac.splunk.framework.bean.LogVO;
import com.macys.sstpac.splunk.framework.comparator.LogComparator;

@Component
public class SplunkActivityDAO {

	@Autowired
	private AccessLogDAO accessLogDAO;

	@Autowired
	private ApplicationLogDAO applicationLogDAO;

	@Autowired
	private D2CLogDAO d2cLogDAO;

	public ArrayList<LogVO> performSplunkActivity(JdbcTemplate jdbcTemplate,
			ArrayList<LogVO> logVOList) throws Exception {
		int searchId = 10;
		ArrayList<LogVO> sortedLogVOList = new ArrayList<LogVO>();
		try {
			insertSplunkActivityDetails(jdbcTemplate, logVOList, searchId);
			sortedLogVOList = getSplunkActivityDetails(jdbcTemplate, searchId);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return sortedLogVOList;
	}

	private void insertSplunkActivityDetails(JdbcTemplate jdbcTemplate,
			ArrayList<LogVO> logVOList, int searchId) throws Exception {
		LogVO logVO = null;
		try {
			for (Iterator<LogVO> iterator = logVOList.iterator(); iterator
					.hasNext();) {
				logVO = iterator.next();
				if (logVO instanceof AccessLogVO) {
					accessLogDAO.insertAccessLog(jdbcTemplate, searchId,
							(AccessLogVO) logVO);
				} else if (logVO instanceof ApplicationLogVO) {
					applicationLogDAO.insertApplicationLog(jdbcTemplate,
							searchId, (ApplicationLogVO) logVO);
				} else if (logVO instanceof D2CLogVO) {
					d2cLogDAO.insertD2CLog(jdbcTemplate, searchId,
							(D2CLogVO) logVO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private ArrayList<LogVO> getSplunkActivityDetails(
			JdbcTemplate jdbcTemplate, int searchId) throws Exception {
		ArrayList<LogVO> logVOList = new ArrayList<LogVO>();
		try {
			logVOList.addAll(accessLogDAO.getAccessLog(jdbcTemplate, searchId));
			logVOList.addAll(applicationLogDAO.getApplicationLog(jdbcTemplate,
					searchId));
			logVOList.addAll(d2cLogDAO.getD2CLog(jdbcTemplate, searchId));

			Collections.sort(logVOList, new LogComparator());

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return logVOList;
	}

}
