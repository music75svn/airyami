package com.lexken.framework.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;

public class OpnionSendManager extends CommonService {
	private final Log logger = LogFactory.getLog(getClass());

	/*************************************************
	 * 쪽지보내기
	 **************************************************/
	public void OpnionSend(SearchMap searchMap) {
		try {
			String sendId = "[업무처리]";
			String targetId = searchMap.getString("targetId");
			String message1 = searchMap.getString("messageTitle", "지표관련의견-성과모니터링/조직성과도/미진사유 확인");
			String message2 = searchMap.getString("message");
					
			searchMap.put("sendId", sendId);
			searchMap.put("targetId", targetId);
			searchMap.put("message1", message1);
			searchMap.put("message2", message2);

			setStartTransaction();

			insertData("bsc.message.message.insertOpnionData", searchMap);

		} catch (Exception e) {
			logger.error(e.toString());
			setRollBackTransaction();
		} finally {
			setEndTransaction();
		}
	}
}
