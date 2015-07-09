package com.macys.sstpac.splunk.framework.comparator;

import java.util.Comparator;
import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.macys.sstpac.splunk.framework.bean.LogVO;

@Component
public class LogComparator implements Comparator<LogVO> {

	private static HashMap<String, Integer> classHirarchy = new HashMap<String, Integer>();

	static {
		classHirarchy.put("AccessLogVO", 1);
		classHirarchy.put("ApplicationLogVO", 2);
		classHirarchy.put("D2CLogVO", 3);
	}

	public int compare(LogVO logVO1, LogVO logVO2) {
		long log1Time = logVO1.getLogTime().getTime();
		long log2Time = logVO2.getLogTime().getTime();
		if (log2Time > log1Time)
			return 1;
		else if (log1Time > log2Time)
			return -1;
		else {
			int logV1HirachyValue = classHirarchy.get(logVO1.getClass()
					.getCanonicalName());
			int logV2HirachyValue = classHirarchy.get(logVO2.getClass()
					.getCanonicalName());

			if (logV1HirachyValue > logV2HirachyValue)
				return 1;
			else if (log1Time > log2Time)
				return -1;
		}
		return 0;
	}
}
