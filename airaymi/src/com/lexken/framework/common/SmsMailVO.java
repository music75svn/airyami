/*************************************************************************
 * CLASS 명  	: SmsMailVO
 * 작 업 자  		: 박재현
 * 작 업 일  		: 2009.09.16
 * 기    능  	: SMS email 수신자 정보
 * ---------------------------- 변 경 이 력 --------------------------------
 * 번호  작 업 자     작     업     일        변 경 내 용                 비고
 * ----  --------  -----------------  -------------------------    --------
 *   1    박재현		 2009.09.16			  최 초 작 업 
 **************************************************************************/
package com.lexken.framework.common;

public class SmsMailVO {

	private String toUserId;	//수신자 ID
	private String toUserNm;	//수신자 명
	private String toTelNo;		//수신자 휴대폰 번호
	private String toEmail;		//수신자 이메일 주소
	
	private String fromUserId;	//발신자 ID
	private String fromUserNm;	//발신자 명
	private String fromTelNo;	//발신자 휴대폰 번호
	private String fromEmail;	//발신자 이메일 주소
	
	private String smsContent;	//발송메시지
	
	private String mailTitle;	//메일제목
	private String mailContent; //메일내용
	
	
	public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public String getToUserNm() {
		return toUserNm;
	}
	public void setToUserNm(String toUserNm) {
		this.toUserNm = toUserNm;
	}
	public String getToTelNo() {
		return toTelNo;
	}
	public void setToTelNo(String toTelNo) {
		this.toTelNo = toTelNo.replaceAll("-", "").replaceAll("\\)", "").replaceAll("\\.", "");
	}
	public String getToEmail() {
		return toEmail;
	}
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
	public String getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
	public String getFromUserNm() {
		return fromUserNm;
	}
	public void setFromUserNm(String fromUserNm) {
		this.fromUserNm = fromUserNm;
	}
	public String getFromTelNo() {
		return fromTelNo;
	}
	public void setFromTelNo(String fromTelNo) {
		this.fromTelNo = fromTelNo.replaceAll("-", "").replaceAll("\\)", "").replaceAll("\\.", "");
	}
	public String getFromEmail() {
		return fromEmail;
	}
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}
	public String getSmsContent() {
		return smsContent;
	}
	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}
	public String getMailTitle() {
		return mailTitle;
	}
	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}
	public String getMailContent() {
		return mailContent;
	}
	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}
	
	
}

