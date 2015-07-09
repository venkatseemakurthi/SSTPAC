package com.macys.sstpac.splunk.framework.helper;

import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.macys.sstpac.databaseConfigure.DBConfiguration;
import com.macys.sstpac.splunk.framework.bean.LogVO;
import com.macys.sstpac.splunk.framework.dao.SplunkActivityDAO;
import com.macys.sstpac.splunk.framework.parser.SplunkLogParser;
import com.macys.sstpac.splunk.framework.services.SplunkService;

@Component
public class SplunkLogActivity {

	@Autowired
	private SplunkService splunkService;

	@Autowired
	private SplunkActivityDAO splunkActivityDAO;

	@Autowired
	private DBConfiguration dbConfiguration;

	@Autowired
	private SplunkLogParser splunkLogParser;

	public void processSplunkActivity(ArrayList<LogVO> logVOList)
			throws Exception {
		StringBuffer xmlResponse = null;
		LogVO logVO = null;
		try {
			for (Iterator<LogVO> iterator = logVOList.iterator(); iterator
					.hasNext();) {
				logVO = iterator.next();
				xmlResponse = splunkService.postHttpURLConnection(logVO.getLogQuery(),"GET");
				parseSplunkXMLResponse(logVOList, xmlResponse, logVO.getLogType());
			}
			logVOList = splunkActivityDAO.performSplunkActivity(dbConfiguration.getJdbcTemplate("MySql"),
					logVOList);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private ArrayList<LogVO> parseSplunkXMLResponse(ArrayList<LogVO> logVOList,
			StringBuffer xmlResponse, String logType) throws Exception {
		StringBuffer logXMLResponse = new StringBuffer();
		try {
			if (logType.equals("ACL")) {
				logVOList.add(splunkLogParser.parseAccessLog(logXMLResponse));
			} else if (logType.equals("APL")) {
				logVOList.add(splunkLogParser
						.parseApplicationLog(logXMLResponse));
			} else if (logType.equals("DCL")) {
				logVOList.add(splunkLogParser.parseD2CLog(logXMLResponse));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return logVOList;
	}

}
