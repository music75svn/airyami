package com.lexken.framework.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;

public class NoteSendManager extends CommonService {
	private final Log logger = LogFactory.getLog(getClass());

	/*************************************************
	 * 쪽지보내기
	 **************************************************/
	public void NoteSend(SearchMap searchMap) {
		try {
			String sendId = "[업무처리]";
			String targetId = searchMap.getString("targetId");
			String message = searchMap.getString("message");
			
			//targetId = "960034";
			
			searchMap.put("sendId", sendId);
			searchMap.put("targetId", targetId);
			searchMap.put("message", message);

			setStartTransaction();

			insertData("bsc.message.message.insertData", searchMap);

		} catch (Exception e) {
			logger.error(e.toString());
			setRollBackTransaction();
		} finally {
			setEndTransaction();
		}
	}
}
