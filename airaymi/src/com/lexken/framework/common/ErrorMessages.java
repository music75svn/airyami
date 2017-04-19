package com.lexken.framework.common;

import java.text.MessageFormat;

/**
 * 각종 에러 메세지를 정의
 *
 */
public class ErrorMessages {
	// -------------------------------
	// 0 : 공통
	// -------------------------------
	public final static String SUCCESS_CODE = "0";
	public final static String SUCCESS_MESSAGE = "성공";
	public final static String SUCCESS_EMAIL_MESSAGE = "사용 가능합니다";
	
	
	// -------------------------------
	// 10 : 등록, 수정, 삭제 관련
	// -------------------------------
	public final static String SUCCESS_SAVE_CODE = "10";
	public final static String SUCCESS_SAVE_MESSAGE = "저장이 완료되었습니다.";
	
	public final static String SUCCESS_EDIT_CODE = "11";
	public final static String SUCCESS_EDIT_MESSAGE = "수정이 완료되었습니다.";
	
	public final static String SUCCESS_DELETE_CODE = "12";
	public final static String SUCCESS_DELETE_MESSAGE = "삭제가 완료되었습니다.";
	
	public final static String SUCCESS_PROCESS_CODE = "13";
	public final static String SUCCESS_PROCESS_MESSAGE = "정상적으로 처리되었습니다.";
	
	public final static String APPROVE_REQUEST_CODE = "14";
	public final static String APPROVE_REQUEST_MESSAGE = "승인요청 처리되었습니다.";
	
	public final static String APPROVE_SUCCESS_CODE = "15";
	public final static String APPROVE_SUCCESS_MESSAGE = "승인 처리되었습니다.";
	
	public final static String NOT_APPROVE_SUCCESS_CODE = "16";
	public final static String NOT_APPROVE_SUCCESS_MESSAGE = "반려 처리되었습니다.";
	
	public final static String SUCCESS_PW_INIT_CODE = "17";
	public final static String SUCCESS_PW_INIT_MESSAGE = "본인 확인에 성공하였습니다.\n{0} 번으로 SMS에 인증번호가 발송되었습니다.\n인증번호를 입력해 주십시요.";
	
	public final static String SUCCESS_FILE_CODE = "18";
	public final static String SUCCESS_FILE_MESSAGE = "파일업로드가 완료되었습니다.";
	
	public final static String SUCCESS_PW_INIT_CODE2 = "19";
	public final static String SUCCESS_PW_INIT_MESSAGE2 = "본인 확인에 성공하였습니다.\n대표번호로 문의한 인증번호를 입력해 주십시요.";

	public final static String SUCCESS_DELETE_USER_CODE = "20";
	public final static String SUCCESS_DELETE_USER_MESSAGE = "회원 탈퇴가 정상적으로 처리되었습니다.";
	
	public final static String SUCCESS_USER_JOIN_CODE = "21";
	public final static String SUCCESS_USER_JOIN_MESSAGE = "회원가입이 완료되었습니다.";
	
	public final static String SUCCESS_ACNUT_NO_CODE = "22";
	public final static String SUCCESS_ACNUT_NO_MESSAGE = "가상계좌발급이 완료 되었습니다.";
	
	public final static String PAYMENT_REQUEST_CODE = "23";
	public final static String PAYMENT_REQUEST_MESSAGE = "지급요청 처리되었습니다.";
	
	// -------------------------------
	// -10 : 등록, 수정, 삭제 관련
	// -------------------------------
	public final static String FAILURE_SAVE_CODE = "-10";
	public final static String FAILURE_SAVE_MESSAGE = "저장중 오류가 발생되었습니다.";
	
	public final static String FAILURE_EDIT_CODE = "-11";
	public final static String FAILURE_EDIT_MESSAGE = "수정중 오류가 발생되었습니다.";
	
	public final static String FAILURE_DELETE_CODE = "-12";
	public final static String FAILURE_DELETE_MESSAGE = "삭제중 오류가 발생되었습니다.";
	
	public final static String FAILURE_PROCESS_CODE = "-13";
	public final static String FAILURE_PROCESS_MESSAGE = "작업 실패입니다.";
	
	public final static String FAILURE_CHK_CODE = "-14";
	public final static String FAILURE_CHK_MESSAGE = "{0} 항목을 선택하십시오.";
	
	public final static String FAILURE_DUP_CODE = "-15";
	public final static String FAILURE_DUP_MESSAGE = "중복된 데이터 입니다.";
	
	public final static String FAILURE_FOREIGN_CODE = "-16";
	public final static String FAILURE_FOREIGN_MESSAGE = "하위에 등록된 데이터가 존재합니다. 데이터를 확인해 주십시오.";
	public final static String FAILURE_FOREIGN2_MESSAGE = "상위에 등록된 데이터가 없습니다. 데이터를 확인해 주십시오.";
	public final static String FAILURE_FOREIGN3_MESSAGE = "{0}에 등록된 데이터가 존재합니다. 데이터를 확인해 주십시오.";
	
	public final static String FAILURE_REGNO_DUP_CODE = "-17";
	public final static String FAILURE_REGNO_DUP_MESSAGE = "주민번호 또는 사업자번호가 이미 존재합니다.";
	
	public final static String FAILURE_CHK_CLTHD = "-18";
	public final static String FAILURE_CHK_CLTHD_MESSAGE = "산식에 사용될 세부지표를 선택하십시오.(A~J)";

	public final static String FAILURE_DUP2_CODE = "-19";
	public final static String FAILURE_DUP2_MESSAGE = "중복된 {0} 데이터 입니다.";

	public final static String FAILURE_PW_INIT_CODE = "-20";
	public final static String FAILURE_PW_INIT_MESSAGE = "본인 인증이 실패하였습니다.\n전송된 인증번호를 확인해 주십시요.";
	
	public final static String FAILURE_FILE_CODE = "-21";
	public final static String FAILURE_FILE_MESSAGE = "허용되지 않은 파일형식입니다. xls, xlsx, ppt, pptx, doc, docx, hwp, pdf, zip, txt, gif, jpg, png, jpeg 외의 파일인 경우 zip 파일로 압축 후 첨부해주십시요.";
	
	// -------------------------------
	// -100 : 로그인 및 권한 관련
	// -------------------------------
	public final static String USER_EXISTED_CODE = "-100";
	public final static String USER_EXISTED_MESSAGE = "{0} 는 존재하는 아이디 입니다.";
	
	public final static String USER_NOTFOUND_CODE = "-101";
	public final static String USER_NOTFOUND_MESSAGE = "{0} 는 존재하지 않는 아이디 입니다.";
	
	public final static String PASSWORD_MISMATCH_CODE = "-102";
	public final static String PASSWORD_MISMATCH_MESSAGE = "사용자의 비밀번호가 일치하지 않습니다.";
	
	public final static String USER_NO_AUTHORITY_CODE = "-103";
	public final static String USER_NO_AUTHORITY_MESSAGE = "{0} 님은 접속 권한이 없습니다.";
	
	public final static String COMPNUM_EXISTED_CODE = "-104";
	public final static String COMPNUM_EXISTED_MESSAGE = "{0} 는 존재하는 사원번호 입니다.";
	
	public final static String CASH_EXIST_CODE = "-105";
	public final static String CASH_EXIST_MESSAGE = "캐시 정보가 존재합니다.";
	
	public final static String ORDER_EXIST_CODE = "-106";
	public final static String ORDER_EXIST_MESSAGE = "주문 정보가 존재합니다.";
	
	public final static String USER_SECESS_CODE = "-107";
	public final static String USER_SECESS_MESSAGE = "강제 탈퇴한 회원입니다.";
	
	public final static String REFERER_ERROR_CODE = "-108";
	public final static String REFERER_ERROR_MESSAGE = "정상적인 경로로 접근하여 주십시요.";
	
	public final static String NO_SCRIPT_CODE = "-109";
	public final static String NO_SCRIPT_MESSAGE = "이 화면은 스크립트 기능이 작동되지 않을 경우 사용할 수 없습니다. Active스크립팅 기능을 사용함으로 바꿔주세요.";
	
	public final static String LOGIN_INFO_DUP_CODE = "-110";
	public final static String LOGIN_INFO_DUP_MESSAGE = "동일한 아이디와 비밀번호를 사용하는 사용자가 존재합니다.\n주민등록번호를 입력해 주십시요.";
	
	public final static String USER_UNDEFINED_CODE = "-111";
	public final static String USER_UNDEFINED_MESSAGE = "존재하지 않는 아이디이거나 잘못 입력된 사용자 정보입니다.";
	
	public final static String USER_UNDEFINED2_CODE = "-112";
	public final static String USER_UNDEFINED2_MESSAGE = "존재하지 않는 사용자 정보입니다.";
	
	public final static String LOGIN_EXCEED_CODE = "-113";
	public final static String LOGIN_EXCEED_MESSAGE = "접속시도 회수 5회 초과로 접근이 거부되었습니다. \n5분 후에 다시 시도해 주십시요.";
	
	public final static String USER_DEL_ONLY_GENERAL_CODE = "-150";
	public final static String USER_DEL_ONLY_GENERAL_MESSAGE = "일반사용자 권한만 탈퇴가 가능합니다.";

	public final static String USER_DATA_EXIST_CODE = "-151";
	public final static String USER_DATA_EXIST_MESSAGE = "사용자의 아이디로 등록된 데이터가 있는 경우 탈퇴가 불가능합니다.\n";
	
	// -------------------------------
	// -200 : 메뉴 관련
	// -------------------------------
	public final static String MENU_EXISTED_CODE = "-200";
	public final static String MENU_EXISTED_MESSAGE = "{0} 는 존재하는 메뉴 아이디 입니다.";
	
	public final static String HIGH_MENU_EXISTED_CODE = "-201";
	public final static String HIGH_MENU_EXISTED_MESSAGE = "상위메뉴가 존재하지 않습니다.";

	public final static String ROOT_MENU_NO_ACCEESS_CODE = "-202";
	public final static String ROOT_MENU_NO_ACCEESS_MESSAGE = "전체메뉴는 설정을 변경할수 없습니다.";
		
	// -------------------------------
	// 300 : 데이터 무결성 관련
	// -------------------------------
	public final static String FAILURE_ONLY_NUM_CODE = "-300";
	public final static String FAILURE_ONLY_NUM_MESSAGE = "[{0}] : 숫자만 입력하실 수 있습니다! 다시 한번 확인하십시오.";

	public final static String FAILURE_ONLY_ENG_CODE = "-301";
	public final static String FAILURE_ONLY_ENG_MESSAGE = "영문만 사용하실 수 있습니다.";

	public final static String FAILURE_ONLY_ENG_NUM_CODE = "-302";
	public final static String FAILURE_ONLY_ENG_NUM_MESSAGE = "{0}은(는) 영/숫자만 사용하실 수 있습니다.";

	public final static String FAILURE_CHK_JUMIN_CODE = "-303";
	public final static String FAILURE_CHK_JUMIN_MESSAGE = "주민등록번호가 유효하지 않습니다. 다시 입력하세요.";

	public final static String FAILURE_CHK_REGIT_NUMBER_CODE = "-304";
	public final static String FAILURE_CHK_REGIT_NUMBER_MESSAGE = "없는 주민등록번호 입니다. 다시 입력해 주세요!!";

	public final static String FAILURE_CHK_PHONE_NUMBER_CODE = "-305";
	public final static String FAILURE_CHK_PHONE_NUMBER_MESSAGE = "{0}는 숫자와 '-'만 입력할 수 있습니다.";

	public final static String FAILURE_CHK_PHONE_NUMBER_LEN_CODE = "-306";
	public final static String FAILURE_CHK_PHONE_NUMBER_LEN_MESSAGE = "[{0}]를  {1}자리 이하로 입력하십시오.";

	public final static String FAILURE_CHK_EMAIL_CODE = "-307";
	public final static String FAILURE_CHK_EMAIL_MESSAGE = "이메일 주소를 확인해 주세요.";

	public final static String FAILURE_FIRST_NOT_ONLY_NUM_CODE = "-308";
	public final static String FAILURE_FIRST_NOT_ONLY_NUM_MESSAGE = "[{0}] : 첫글자는 숫자로 입력하실 수 없습니다! 다시 한번 확인하십시오.";

	public final static String FAILURE_CHK_SEL_EMPTY_CODE = "-309";
	public final static String FAILURE_CHK_SEL_EMPTY_MESSAGE = "[{0}] : 선택해주십시오!";
	
	public final static String FAILURE_CHK_EMPTY_CODE = "-310";
	public final static String FAILURE_CHK_EMPTY_MESSAGE = "[{0}] : 입력해주십시오.";
	
	public final static String FAILURE_CHK_LENGTH_CODE = "-311";
	public final static String FAILURE_CHK_LENGTH_MESSAGE = "[{0}] : {1}자를 입력해주십시오! (주의: 한글 1자는 3자로 계산함.)";
	
	public final static String FAILURE_CHK_IF_KO_CHR_CODE = "-312";
	public final static String FAILURE_CHK_IF_KO_CHR_MESSAGE = "{0}는 한글 또는 특수문자는 사용불가능합니다.";
	
	public final static String FAILURE_CHK_MENU_NAME_CODE = "-313";
	public final static String FAILURE_CHK_MENU_NAME_MESSAGE = "[{0}] : 한글과 영문과 숫자와 특수문자(&, /, ', -)만 입력할 수 있습니다.";
	
	public final static String FAILURE_CHK_IMAGE_CODE = "-314";
	public final static String FAILURE_CHK_IMAGE_MESSAGE = "이미지 파일은 gif, jpg, bmp, png 파일만 등록할 수 있습니다.";
	
	public final static String FAILURE_CHK_CONTINUTY_CODE = "-315";
	public final static String FAILURE_CHK_CONTINUTY_MESSAGE = "[{0}] 연속해서 입력하시면 안 됩니다. ({1})";

	public final static String FAILURE_NOT_ONLY_NUM_CODE = "-316";
	public final static String FAILURE_NOT_ONLY_NUM_MESSAGE = "[{0}] : 숫자만 입력하실 수 없습니다! 다시 한번 확인하십시오.";
	
	public final static String FAILURE_CHK_EMPTY_BLANK_CODE = "-317";
	public final static String FAILURE_CHK_EMPTY_BLANK_MESSAGE = "{0}을(를) 입력하십시오. ({1})";
	
	public final static String FAILURE_CHK_LENGTH2_CODE = "-318";
	public final static String FAILURE_CHK_LENGTH2_MESSAGE = "[{0}] : {1} - {2}자를 입력해주십시오! (주의: 한글 1자는 3자로 계산함.)";
	
	public final static String FAILURE_CHK_IF_CHR_CODE = "-319";
	public final static String FAILURE_CHK_IF_CHR_MESSAGE = "{0}는 영문자를 반드시 포함해야 합니다.";
	
	public final static String FAILURE_ONLY_UPP_ENG_UNDERBAR_CODE = "-320";
	public final static String FAILURE_ONLY_UPP_ENG_UNDERBAR_MESSAGE = "[{0}] : 영문 대문자와 '_'만 입력하실 수 있습니다! 다시 한번 확인하십시오.";
	
	public final static String FAILURE_ONLY_LOW_ENG_NUM_CODE = "-330";
	public final static String FAILURE_ONLY_LOW_ENG_NUM_MESSAGE = "[{0}] : 영문 소문자와 숫자만 입력하실 수 있습니다! 다시 한번 확인하십시오.";
	
	public final static String FAILURE_CHK_MENU_ID_CODE = "-331";
	public final static String FAILURE_CHK_MENU_ID_MESSAGE = "{0}는 영문과 숫자와 '-'만 입력할 수 있습니다.";
	
	public final static String FAILURE_CHK_DATE_CODE = "-332";
	public final static String FAILURE_CHK_DATE_MESSAGE = "{0}를 확인해 주십시오.";
	
	public final static String FAILURE_CHK_PERIOD_CODE = "-333";
	public final static String FAILURE_CHK_PERIOD_MESSAGE = "{0}기간 마지막일이 시작일보다 빠릅니다.";
	
	public final static String FAILURE_CHK_LENGTH3_CODE = "-334";
	public final static String FAILURE_CHK_LENGTH3_MESSAGE = "[{0}] : {1}자를 입력해주십시오!";
	
	public final static String FAILURE_CHK_ATTACH_FILE_TYPE_CODE = "-335";
	public final static String FAILURE_CHK_ATTACH_FILE_TYPE_MESSAGE = "파일 형식이 맞지 않습니다.";
	
	public final static String FAILURE_CHK_ATTACH_FILE_SIZE_CODE = "-336";
	public final static String FAILURE_CHK_ATTACH_FILE_SIZE_MESSAGE = "파일 첨부 용량은 최대 {0}를 넘을 수 없습니다.";

	public final static String FAILURE_CHK_COMMA_NUMBER_CODE = "-337";
	public final static String FAILURE_CHK_COMMA_NUMBER_MESSAGE = "{0}는 숫자와 ','만 입력할 수 있습니다.";
	
	public final static String FAILURE_COMPARE_PERIOD_CODE = "-338";
	public final static String FAILURE_COMPARE_PERIOD_MESSAGE = "[{0}]이 {1}보다 이후일 수 없습니다.";
	
	public final static String FAILURE_CHK_NUMBER_RANGE_CODE = "-339";
	public final static String FAILURE_CHK_NUMBER_RANGE_MESSAGE = "[{0}]점수가  {1}점보다 큽니다.";

    public final static String FAILURE_CHK_NUMBER_COMMA_CODE = "-340";
	public final static String FAILURE_CHK_NUMBER_COMMA_MESSAGE = "[{0}]는 정수 {1}자리 소수{2}자리까지 입력할 수 있습니다.";
	
	public final static String FAILURE_CHK_DUPLICATED = "-341";
	public final static String FAILURE_CHK_DUPLICATED_MESSAGE = "[{0}]자료를 중복으로 입력할 수 없습니다. 다시 한번 확인하십시오.";

	public final static String FAILURE_CHK_PAGE_VAILD_CODE= "-337";
	public final static String FAILURE_CHK_PAGE_VAILD_CODE_MESSAGE = "{0}는 1 페이지 이상 입력할 수 있습니다.";	

	public final static String FAILURE_CHK_PERMIT_CODE = "-338";
	public final static String FAILURE_CHK_PERMIT_MESSAGE = "{0}가 유효하지 않습니다. 다시 입력하세요.";
	
    public final static String FAILURE_CHK_WEIGHT_SUM_CODE = "-400";
    public final static String FAILURE_CHK_WEIGHT_SUM_MESSAGE = "{0} 가중치의 합을 확인하여 주십시오.";
	
	public final static String FAILURE_CHK_IF_NUM_CODE = "-342";
	public final static String FAILURE_CHK_IF_NUM_MESSAGE = "{0}는 숫자를 반드시 포함해야 합니다.";
	
	public final static String FAILURE_CHK_IF_SPC_CHR_CODE = "-343";
	public final static String FAILURE_CHK_IF_SPC_CHR_MESSAGE = "{0}는 특수문자를 반드시 포함해야 합니다.";
	
	public final static String FAILURE_CHK_FIRST_ENG_CODE = "-344";
	public final static String FAILURE_CHK_FIRST_ENG_MESSAGE = "{0}는 영문자로 시작되어야 합니다.";
	
	public final static String FAILURE_CHK_REGTNO_CODE = "-401";
	public final static String FAILURE_CHK_REGTNO_MESSAGE = "출원 번호가 유효하지 않습니다. 다시 입력하세요.";
	
	public final static String FAILURE_CHK_OVER_CODE = "-403";
	public final static String FAILURE_CHK_OVER_MESSAGE = "[{0}] 는 징수누계금액 보다 작아야 합니다..";
	
	public final static String FAILURE_CHK_COLLECT_CODE = "-404";
	public final static String FAILURE_CHK_COLLECT_MESSAGE = "징수금액의 합은 징수총액을 초과할 수 없습니다.";
	

	public final static String FAILURE_CHK_COLLECT_OVER_CODE = "-405";
	public final static String FAILURE_CHK_COLLECT_OVER_MESSAGE = "기술료 징수금액 입력을 먼저 하셔야 합니다.";
	
	public final static String FAILURE_CHK_MILEAGE_OVER_CODE = "-406";
	public final static String FAILURE_CHK_MILEAGE_OVER_MESSAGE = "잔여마일리지보다 사용할 마일리지가 더 큽니다.";
	
	// -------------------------------
	// 400 : 포맷 코드 관련
	// -------------------------------
	public final static int FORMAT_DATE = 400;			    	//포맷 날짜
	public final static int FORMAT_COMMA = 400;			    	//포맷 콤마
	public final static int FORMAT_ZIP = 400;			    	//포맷 우편번호
	
	
	// -------------------------------
	// 600 : SMS 메시지
	// -------------------------------
	/*
		99: 인증실패, 포트오류
		98: 사용기간만료
		97: 잔여코인부족
		02: 데이터오류, 전송날짜오류
		16: 발송서버 IP 오류
		00: 정상적으로 이통사에게 전달
	 */
	public final static String SUCCESS_SMS_CODE = "600";
	public final static String SUCCESS_SMS_MESSAGE = "발송되었습니다.";
	
	public final static String FAILURE_SMS_NO_NUM_CODE = "-601";
	public final static String FAILURE_SMS_NO_NUM_MESSAGE = "수신자 정보가 없습니다.\n수신자 정보를 확인하여 주십시요.";
	
	public final static String FAILURE_SMS_NUM_CODE = "-602";
	public final static String FAILURE_SMS_NUM_MESSAGE = "잘못된 전화번호 입니다.\n전화번호를 확인하여 주십시요.";
	
	public final static String FAILURE_SMS_MSG_CODE = "-603";
	public final static String FAILURE_SMS_MSG_MESSAGE = "전달할 메시지가 없습니다.";
	
	public final static String FAILURE_SMS_MSG_LENGTH_CODE = "-604";
	public final static String FAILURE_SMS_MSG_LENGTH_MESSAGE = "메시지의 길이는 80byte(한글 한글자 2byte)입니다.\n길이를 줄여주십시요.";
	
	public final static String FAILURE_SMS_99_CODE = "-605";
	public final static String FAILURE_SMS_99_MESSAGE = "인증실패, 포트오류\n관리자에게 문의하여 주십시요.";
	
	public final static String FAILURE_SMS_98_CODE = "-606";
	public final static String FAILURE_SMS_98_MESSAGE = "사용기간만료\n관리자에게 문의하여 주십시요.";
	
	public final static String FAILURE_SMS_97_CODE = "-607";
	public final static String FAILURE_SMS_97_MESSAGE = "잔여코인부족\n관리자에게 문의하여 주십시요.";
	
	public final static String FAILURE_SMS_02_CODE = "-608";
	public final static String FAILURE_SMS_02_MESSAGE = "데이터오류, 전송날짜오류\n관리자에게 문의하여 주십시요.";
	
	public final static String FAILURE_SMS_16_CODE = "-609";
	public final static String FAILURE_SMS_16_MESSAGE = "발송서버 IP 오류\n관리자에게 문의하여 주십시요.";
	
	public final static String FAILURE_SMS_MANY_CODE = "-610";
	public final static String FAILURE_SMS_MANY_MESSAGE = "한번에 전송할 수 있는 데이터 양을 넘었습니다.\n여러번에 나누어서 보내주십시요.";
	
	public final static String FAILURE_SMS_ETC_CODE = "-699";
	public final static String FAILURE_SMS_ETC_MESSAGE = "SMS 발송중 오류가 발생하였습니다.\n관리자에게 문의하여 주십시요.";
	
	public final static String FAILURE_SMS_NO_CP_NUM_CODE = "-611";
	public final static String FAILURE_SMS_NO_CP_NUM_MESSAGE = "핸드폰번호가 시스템에 등록되어 있지 않습니다.\n관리자에게 확인하여 주십시요.";
	
	
	/**
	 * 메세지를 포멧이 설정된 문자열에 대응하여 설정한다.
	 * 
	 * @param message 포멧 문자열
	 * @param arguments 데이터 배열
	 * @return 결과 문자열
	 */
	public static String format(String message, Object[] arguments) {

		MessageFormat format = new MessageFormat(message);
		return format.format(arguments);

	}

	/**
	 * 메세지를 포멧이 설정된 문자열에 대응하여 설정한다. 단, 1개의 문자열 인수를 받아들인다.
	 * 
	 * @param message 포멧 문자열
	 * @param argment 표시할 인수
	 * @return 결과 문자열
	 */
	public static String format(String message, String argment) {
		MessageFormat format = new MessageFormat(message);
		
		String argments[] = new String[1];
		argments[0] = argment;

		return format.format(argments);
	}
}
