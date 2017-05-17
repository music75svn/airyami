/**
 * Author		: bsh
 * Date   		: 2017. 5. 18.
 * Description 	: 엑셀 상수 정의 클래스입니다.
 */

package egovframework.airyami.cmm.excel;

import java.util.HashMap;
import java.util.Map;

public class ConstantsExcelColDwise {
	
    public static Map TITLE_MAP = null;         // 엑셀별 타이틀 정보
    public static Map COLUME_MAP = null;        // 엑셀별 컬럼 정보 셋팅 
    public static Map EXCELCOLUMINFO = null;    // 컬럼의 이름 정보
    
    public static void initExcel(){
    	setTitleInfo();
    	setPageColumeInfo();
    	setColumeNameInfo();
    }
    
    
    public static String get_GC_TITLE(String excel_name){
    	return (String)TITLE_MAP.get(excel_name);
    }
    
    public static String get_GC_PERF_COLUME(String excel_name){
        return (String)COLUME_MAP.get(excel_name);
    }
    
    public static Map getExcelColumInfo(){
    	return EXCELCOLUMINFO;
    }
   
    
    
    public static void setTitleInfo(){
    	if(TITLE_MAP == null){
    		TITLE_MAP = new HashMap();
    		// system
    		TITLE_MAP.put( "COMMCODE", "공통코드");
    		TITLE_MAP.put( "GROUPCODE", "그룹코드테스트");
    		TITLE_MAP.put( "COMMGROUPCODE", "공통코드그룹");
    		TITLE_MAP.put( "COMMON_CODE", "공통코드");
    		
    		
    		// 현황
    		TITLE_MAP.put( "EDU_APPLY", "교육신청자");
    		TITLE_MAP.put( "EDU_JOIN", "교육참여자");
    		TITLE_MAP.put( "MATERIAL_HISTORY", "교재 입/출고이력");
    		TITLE_MAP.put( "EVENT_APPLY", "행사신청자");
    		TITLE_MAP.put( "EVENT_JOIN", "행사참여자");
    		TITLE_MAP.put( "EXPERT", "전문가POOL");
    		TITLE_MAP.put( "LECTURE_APPLY", "YES리더특강신청");
    		TITLE_MAP.put( "LECTURER_LIST", "YES리더현황");
    		
    		
    		// 로그
    		TITLE_MAP.put( "MENULOG", "시스템 사용현황");
    		TITLE_MAP.put( "MENUCONNECT", "시스템 접속정보");
    		TITLE_MAP.put( "ERRORLOG", "시스템 에러로그");
    		
    		//GEW
    		TITLE_MAP.put( "GEWEVENT", "GEW KOREA 행사 신청자정보");
    		TITLE_MAP.put( "GEWSUPPORTER", "GEW KOREA 서포터즈 신청자정보");
    	}
    }
    
    public static void setPageColumeInfo(){
    	if(COLUME_MAP == null){
    		COLUME_MAP = new HashMap();
    		// 운영
    		COLUME_MAP.put( "COMMCODE", "GROUP_CODE,CD");
    		COLUME_MAP.put( "GROUPCODE", "CD,GROUP_CODE");
    		COLUME_MAP.put( "COMMGROUPCODE", "GROUP_CODE,GROUP_CODE_NAME,GROUP_CODE_DESC,USE_YN");
    		COLUME_MAP.put( "COMMON_CODE", "GROUP_CODE,CODE,CODE_NAME,UPPER_CODE,CODE_DESC,USE_YN");
    		
    		COLUME_MAP.put( "EDU_APPLY", "NAME,GENDER_NM,COMPANY,JIKWE,MOBILE,EMAIL,APPLY_STATUS_NM");
    		COLUME_MAP.put( "MATERIAL_HISTORY", "STORE_CNT,RELEASE_CNT,STOCK_CNT,PLACE_NM,STOCK_DT");
    		
    		COLUME_MAP.put( "EVENT_APPLY", "NAME,COMPANY,DEPT_NAME,JIKWE,MOBILE,EMAIL,APPLY_COUNT,APPLY_STATUS_NM");
    		COLUME_MAP.put( "EVENT_JOIN", "NAME,COMPANY,DEPT_NAME,JIKWE,MOBILE,EMAIL,ATTEND_YN_NM");
    		COLUME_MAP.put( "EXPERT", "EXPERT_TYPE_CDNM,REALM,NAME,AREANM,EMAIL,TEL,STATUS_CDNM");
    		COLUME_MAP.put( "LECTURE_APPLY", "LECTURE_DT,LECTURE_STATUSNM,LECTURE_APP_TYPENM,ORGAN_NM,LECTURER_NM,CAST_STATUSNM,BIZCOOL,PREPAY_YN");
    		COLUME_MAP.put( "LECTURER_LIST", "LECTURER_NM,AREA_CDNM,COMPANY_NM,BUSINESS_CDNM,LECTURE_CNT,AVG_POINT,AVG_REWARD,RECOMMEND_YNNM,STATUS_CDNM");

    		COLUME_MAP.put( "GEWEVENT", "INFO_YEAR,INFO_NUM,EMAIL,NAME,TEL,PROGRAM_NAME,EVENT_DT,EVENT_TM,GENDER_C,WORKPLACE,JIKWE,JOB_NM,ADDRESS,NATIONALITY,COUNTRY_LIVE");
    		COLUME_MAP.put( "GEWSUPPORTER", "EMAIL,NAME,TEL,SUPPORT_WORKPLACE,SUPPORT_JIKWE,ADDRESS,EN_TALK_LEVEL,EN_LISTEN_LEVEL,EN_READ_LEVEL,EN_WRITE_LEVEL,REG_DT");
    		
    	}
    }
    
    public static void setColumeNameInfo(){
        if(EXCELCOLUMINFO == null){
        	EXCELCOLUMINFO = new HashMap();
        	
        	// 시스템 
        	EXCELCOLUMINFO.put( "ADDR", "");
        	EXCELCOLUMINFO.put( "ADDRESS", "주소");
        	EXCELCOLUMINFO.put( "ADDRESS1", "");
        	EXCELCOLUMINFO.put( "ADDRESS2", "");
        	EXCELCOLUMINFO.put( "ADDR_DESC", "");
        	EXCELCOLUMINFO.put( "ADMIN_COMMENT", "관리자코멘트");
        	EXCELCOLUMINFO.put( "ADMIN_GROUP_YN", "관리자그룹여부");
        	EXCELCOLUMINFO.put( "ADMIN_GRP_YN", "관리자 ID가 ID인지 GRPUP인지 구분");
        	EXCELCOLUMINFO.put( "ADMIN_ID", "관리자ID");
        	EXCELCOLUMINFO.put( "ADMIN_LOGIN_IP", "");
        	EXCELCOLUMINFO.put( "ADMIN_RECOMMEND", "관리자추천");
        	EXCELCOLUMINFO.put( "ADMIN_TYPE", "관리자구분");
        	EXCELCOLUMINFO.put( "ADMIN_YN", "");
        	EXCELCOLUMINFO.put( "ANSWER_CONTENT", "");
        	EXCELCOLUMINFO.put( "ANSWER_NO", "");
        	EXCELCOLUMINFO.put( "APPLY_STATUS_C", "공통코드(1:대기, 2:승인, 3:반려)");
        	EXCELCOLUMINFO.put( "APPLY_STATUS_CD", "접수/완료/반려");
        	EXCELCOLUMINFO.put( "APPROVAL_DATE", "승인자ID");
        	EXCELCOLUMINFO.put( "APPROVAL_DTTM", "승인일자");
        	EXCELCOLUMINFO.put( "APPROVAL_ID", "신청일자");
        	EXCELCOLUMINFO.put( "APP_DATE", "신청자ID");
        	EXCELCOLUMINFO.put( "APP_DTTM", "신청일자");
        	EXCELCOLUMINFO.put( "APP_END_DT", "");
        	EXCELCOLUMINFO.put( "APP_ID", "신청자ID");
        	EXCELCOLUMINFO.put( "APP_START_DT", "");
        	EXCELCOLUMINFO.put( "AREA_CDNM", "지역");
        	EXCELCOLUMINFO.put( "ARTICLE_HIST_C", "'1':신규, '2':수정, '3':삭제, '4':복원, '5':이동, '6':복사");
        	EXCELCOLUMINFO.put( "ARTICLE_PW", "게시글비밀번호");
        	EXCELCOLUMINFO.put( "ARTICLE_SEQ", "게시글일련번호");
        	EXCELCOLUMINFO.put( "ATTACH_FILE_KIND_C", "공통코드(1:첨부파일, 2:이미지, 3:동영상)");
        	EXCELCOLUMINFO.put( "ATTEND_YN", "");
        	EXCELCOLUMINFO.put( "AUTH_DEL_YN", "");
        	EXCELCOLUMINFO.put( "AUTH_GROUP", "권한그룹");
        	EXCELCOLUMINFO.put( "AUTH_READ_YN", "");
        	EXCELCOLUMINFO.put( "AUTH_REPLY_YN", "");
        	EXCELCOLUMINFO.put( "AUTH_WRITE_YN", "");
        	EXCELCOLUMINFO.put( "AVG_POINT", "평균점수");
        	EXCELCOLUMINFO.put( "AVG_REWARD", "평균강사비");
        	EXCELCOLUMINFO.put( "BANNER_DESC", "배너설명");
        	EXCELCOLUMINFO.put( "BANNER_ID", "");
        	EXCELCOLUMINFO.put( "BANNER_TITLE", "");
        	EXCELCOLUMINFO.put( "BIGO", "");
        	EXCELCOLUMINFO.put( "BIRTH_DT", "");
        	EXCELCOLUMINFO.put( "BIZCOOL", "비즈쿨");
        	EXCELCOLUMINFO.put( "BLDG", "일련번호");
        	EXCELCOLUMINFO.put( "BOARD_DESIGN", "게시판디자인");
        	EXCELCOLUMINFO.put( "BOARD_DESIGN_C", "");
        	EXCELCOLUMINFO.put( "BOARD_ID", "게시판ID");
        	EXCELCOLUMINFO.put( "BOARD_ID1", "게시판ID1");
        	EXCELCOLUMINFO.put( "BOARD_ID2", "게시판ID2");
        	EXCELCOLUMINFO.put( "BOARD_ID3", "게시판ID3");
        	EXCELCOLUMINFO.put( "BOARD_ID4", "게시판ID4");
        	EXCELCOLUMINFO.put( "BOARD_KIND_C", "공통코드");
        	EXCELCOLUMINFO.put( "BOARD_NAME", "게시판명");
        	EXCELCOLUMINFO.put( "BOARD_RIGHT_CD", "공통코드에 고정값으로 등록한다. 읽기/쓰기/댓글/답급 권한, 권한코드 값은 모든 사이트에 공통적으로 사용되는 값이다.");
        	EXCELCOLUMINFO.put( "BOARD_TYPE", "게시판타입");
        	EXCELCOLUMINFO.put( "BUNJI", "건물명");
        	EXCELCOLUMINFO.put( "BUSINESS_CDNM", "업종");
        	EXCELCOLUMINFO.put( "CAST_STATUSNM", "강사섭외상태");
        	EXCELCOLUMINFO.put( "CATEGORY_NAME", "카테고리명");
        	EXCELCOLUMINFO.put( "CATEGORY_SEQ", "카테고리일련번호");
        	EXCELCOLUMINFO.put( "CATEGORY_YN", "카테고리사용");
        	EXCELCOLUMINFO.put( "CHARGE", "");
        	EXCELCOLUMINFO.put( "CHARGE_DEPT", "");
        	EXCELCOLUMINFO.put( "CHARGE_GUBUN", "기업 사용자인경우는 필요없을듯?");
        	EXCELCOLUMINFO.put( "CHARGE_ID", "담당자ID");
        	EXCELCOLUMINFO.put( "CHARGE_NAME", "담당자명");
        	EXCELCOLUMINFO.put( "CHARGE_NM", "");
        	EXCELCOLUMINFO.put( "CHARGE_PHONE", "");
        	EXCELCOLUMINFO.put( "CHG_RQST_CONTENT", "");
        	EXCELCOLUMINFO.put( "CHG_USER_GROUP", "일반/기업/전문가/담당자/일반관리자/시스템관리자");
        	EXCELCOLUMINFO.put( "CHILD_QUEST_YN", "");
        	EXCELCOLUMINFO.put( "CHILD_YN", "");
        	EXCELCOLUMINFO.put( "CLOSE_RESON", "폐쇄이유");
        	EXCELCOLUMINFO.put( "CODE", "코드");
        	EXCELCOLUMINFO.put( "CODE_DESC", "코드설명");
        	EXCELCOLUMINFO.put( "CODE_INFO1", "");
        	EXCELCOLUMINFO.put( "CODE_INFO2", "");
        	EXCELCOLUMINFO.put( "CODE_LEVEL", "코드레벨");
        	EXCELCOLUMINFO.put( "CODE_NAME", "코드명");
        	EXCELCOLUMINFO.put( "COLUMN_NAME", "");
        	EXCELCOLUMINFO.put( "COLUMN_ORDER", "");
        	EXCELCOLUMINFO.put( "COMMENT_AUTH", "댓글권한");
        	EXCELCOLUMINFO.put( "COMMENT_CONTENT", "댓글");
        	EXCELCOLUMINFO.put( "COMMENT_PAGE_SIZE", "");
        	EXCELCOLUMINFO.put( "COMMENT_SEQ", "댓글일련번호");
        	EXCELCOLUMINFO.put( "COMMENT_YN", "댓글사용");
        	EXCELCOLUMINFO.put( "COMMUNITY_CATEGORY", "커뮤니티분류");
        	EXCELCOLUMINFO.put( "COMMUNITY_DEFAULT_SKIN", "커뮤니티기본스킨");
        	EXCELCOLUMINFO.put( "COMMUNITY_DESC", "커뮤니티설명");
        	EXCELCOLUMINFO.put( "COMMUNITY_FLAG", "커뮤니티상태 : N:대기,Y:승인,D:폐쇄");
        	EXCELCOLUMINFO.put( "COMMUNITY_ID", "커뮤니티ID");
        	EXCELCOLUMINFO.put( "COMMUNITY_NAME", "커뮤니티명");
        	EXCELCOLUMINFO.put( "COMMUNITY_ORG_SKIN_NAME", "커뮤니티원본스킨명");
        	EXCELCOLUMINFO.put( "COMMUNITY_SAVE_SKIN_NAME", "커뮤니티저장스킨명");
        	EXCELCOLUMINFO.put( "COMMUNITY_SEQ", "커뮤니티ID");
        	EXCELCOLUMINFO.put( "COMMUNITY_SKIN_FLAG", "커뮤니티스킨구분");
        	EXCELCOLUMINFO.put( "COMMUNITY_SKIN_PATH", "커뮤니티스킨경로");
        	EXCELCOLUMINFO.put( "COMPANY", "회사명");
        	EXCELCOLUMINFO.put( "COMPANY_NM", "회사명");
        	EXCELCOLUMINFO.put( "CONTENT", "내용");
        	EXCELCOLUMINFO.put( "COST", "");
        	EXCELCOLUMINFO.put( "CURRICULUM", "");
        	EXCELCOLUMINFO.put( "DATA_INFO", "");
        	EXCELCOLUMINFO.put( "DATA_NM", "");
        	EXCELCOLUMINFO.put( "DEL_AUTH_YN", "삭제권한여부");
        	EXCELCOLUMINFO.put( "DEL_YN", "삭제");
        	EXCELCOLUMINFO.put( "DEPARTMENT", "부서");
        	EXCELCOLUMINFO.put( "DETAIL_INFO", "");
        	EXCELCOLUMINFO.put( "DETAIL_SEQ", "사용자구분별 상세 내용 항목 순번");
        	EXCELCOLUMINFO.put( "DONG", "리");
        	EXCELCOLUMINFO.put( "DOSEO", "번지");
        	EXCELCOLUMINFO.put( "DROP_DATE", "탈퇴일자");
        	EXCELCOLUMINFO.put( "DROP_DISPOSER_ID", "탈퇴처리자ID");
        	EXCELCOLUMINFO.put( "DROP_DTTM", "탈퇴일자");
        	EXCELCOLUMINFO.put( "EDITOR_TYPE_C", "공통코드(0:미사용, 1:ckeditor)");
        	EXCELCOLUMINFO.put( "EDIT_YN", "에디터사용여부");
        	EXCELCOLUMINFO.put( "EDU_CD", "");
        	EXCELCOLUMINFO.put( "EDU_END_DT", "");
        	EXCELCOLUMINFO.put( "EDU_INFO", "");
        	EXCELCOLUMINFO.put( "EDU_NM", "");
        	EXCELCOLUMINFO.put( "EDU_SEQ", "");
        	EXCELCOLUMINFO.put( "EDU_START_DT", "");
        	EXCELCOLUMINFO.put( "EMAIL", "이메일");
        	EXCELCOLUMINFO.put( "END_DATE", "게시종료기간");
        	EXCELCOLUMINFO.put( "END_DT", "");
        	EXCELCOLUMINFO.put( "END_TIME", "");
        	EXCELCOLUMINFO.put( "ETC", "기타");
        	EXCELCOLUMINFO.put( "EVALUATION", "평가점수");
        	EXCELCOLUMINFO.put( "EVALUATION_SEQ", "평가번호");
        	EXCELCOLUMINFO.put( "EVAL_GRADE", "");
        	EXCELCOLUMINFO.put( "EVAL_SEQ", "");
        	EXCELCOLUMINFO.put( "EVAL_YN", "");
        	EXCELCOLUMINFO.put( "EVENT_CD", "");
        	EXCELCOLUMINFO.put( "EVENT_ID", "");
        	EXCELCOLUMINFO.put( "EVENT_INFO", "");
        	EXCELCOLUMINFO.put( "EVENT_NM", "");
        	EXCELCOLUMINFO.put( "EXAMPLE_SEQ", "");        	
        	EXCELCOLUMINFO.put( "FAX1", "");
        	EXCELCOLUMINFO.put( "FAX2", "");
        	EXCELCOLUMINFO.put( "FAX3", "");
        	EXCELCOLUMINFO.put( "FILE_ALLOW_EXT", "");
        	EXCELCOLUMINFO.put( "FILE_CNT", "첨부파일개수");
        	EXCELCOLUMINFO.put( "FILE_EXT", "파일유형");
        	EXCELCOLUMINFO.put( "FILE_LIMIT_SIZE", "첨부파일제한크기");
        	EXCELCOLUMINFO.put( "FILE_PATH", "파일경로");
        	EXCELCOLUMINFO.put( "FILE_SEQ", "파일일련번호");
        	EXCELCOLUMINFO.put( "FILE_SIZE", "파일크기");
        	EXCELCOLUMINFO.put( "FILE_TYPE", "파일구분");
        	EXCELCOLUMINFO.put( "FILE_TYPE_C", "공통코드(1:일반첨부, 2:교육자료)");
        	EXCELCOLUMINFO.put( "FILE_YN", "첨부파일사용");
        	EXCELCOLUMINFO.put( "FIX_YN", "고정글");
        	EXCELCOLUMINFO.put( "GENDER_C", "성별");
        	EXCELCOLUMINFO.put( "GROUP_CODE", "그룹코드");
        	EXCELCOLUMINFO.put( "GROUP_CODE_DESC", "코드설명");
        	EXCELCOLUMINFO.put( "GROUP_CODE_NAME", "그룹코드명");
        	EXCELCOLUMINFO.put( "GROUP_PRIN_ORDER", "그룹출력순서");
        	EXCELCOLUMINFO.put( "GROUP_YN", "그룹여부");
        	EXCELCOLUMINFO.put( "GUGUN", "동면읍");
        	EXCELCOLUMINFO.put( "HEIGHT", "창높이");
        	EXCELCOLUMINFO.put( "HISTORY_DATE", "이력일");
        	EXCELCOLUMINFO.put( "HISTORY_SEQ", "이력일련번호");
        	EXCELCOLUMINFO.put( "HISTORY_TYPE", "이력구분");
        	EXCELCOLUMINFO.put( "HIST_SEQ", "ARTICLE_SEQ 기준 MAX 채번");
        	EXCELCOLUMINFO.put( "HIST_YN", "");
        	EXCELCOLUMINFO.put( "HTML_USE", "HTML사용");
        	EXCELCOLUMINFO.put( "HTML_YN", "HTML사용여부");
        	EXCELCOLUMINFO.put( "IMAGE_PATH", "");
        	EXCELCOLUMINFO.put( "INPUT_TYPE", "현재는 TEXT BOX만 처리(text, radio, check), 추후 확장성을 위해 ");
        	EXCELCOLUMINFO.put( "ITEM_CNT", "");
        	EXCELCOLUMINFO.put( "JIKWE", "기업 사용자인경우는 필요없을듯?");
        	EXCELCOLUMINFO.put( "JOB", "선택(공통코드에서 목록 생성후 선택하도록 조치)");
        	EXCELCOLUMINFO.put( "JOIN_DT", "");
        	EXCELCOLUMINFO.put( "LECTURE_DT", "강의일자");
        	EXCELCOLUMINFO.put( "LECTURE_CNT", "강의횟수");
        	EXCELCOLUMINFO.put( "LECTURE_STATUSNM", "강의상태");
        	EXCELCOLUMINFO.put( "LECTURE_APP_TYPENM", "강의구분");
        	EXCELCOLUMINFO.put( "LECTURER_NM", "강사명");
        	EXCELCOLUMINFO.put( "LINK_PARAM", "파라미터");
        	EXCELCOLUMINFO.put( "LINK_TYPE", "링크타입");
        	EXCELCOLUMINFO.put( "LINK_URL", "링크주소");
        	EXCELCOLUMINFO.put( "LOGIN_DATE", "로그인일자");
        	EXCELCOLUMINFO.put( "LOGIN_DTTM", "");
        	EXCELCOLUMINFO.put( "LOGIN_IP", "로그인IP");
        	EXCELCOLUMINFO.put( "MAIN_DEFAULT_IMAGE", "메인기본이미지");
        	EXCELCOLUMINFO.put( "MAIN_IMAGE_FLAG", "메인이미지구분");
        	EXCELCOLUMINFO.put( "MAIN_IMAGE_PATH", "메인이미지경로");
        	EXCELCOLUMINFO.put( "MAIN_ORG_IMAGE_NAME", "메인원본이미지명");
        	EXCELCOLUMINFO.put( "MAIN_SAVE_IMAGE_NAME", "메인저장이미지명");
        	EXCELCOLUMINFO.put( "MAIN_TYPE", "메인타입");
        	EXCELCOLUMINFO.put( "MATERIAL_SEQ", "교재일련번호");
        	EXCELCOLUMINFO.put( "MATERIAL_INFO", "교재내용");
        	EXCELCOLUMINFO.put( "MATERIAL_NM", "교재명");
        	EXCELCOLUMINFO.put( "MATERIAL_CD", "교재구분");
        	EXCELCOLUMINFO.put( "HISOTRY_CD", "입출고구분");
        	EXCELCOLUMINFO.put( "STORE_CNT", "입고");
        	EXCELCOLUMINFO.put( "RELEASE_CNT", "출고");
        	EXCELCOLUMINFO.put( "STOCK_CNT", "현황");
        	EXCELCOLUMINFO.put( "MEMBER_FLAG", "회원상태");
        	EXCELCOLUMINFO.put( "MEMBER_GRADE", "회원등급");
        	EXCELCOLUMINFO.put( "MEMBER_GRADE_NAME", "회원등급명");
        	EXCELCOLUMINFO.put( "MEMBER_IMAGE_PATH", "회원이미지경로");
        	EXCELCOLUMINFO.put( "MEMBER_INTRO", "회원소개");
        	EXCELCOLUMINFO.put( "MEMBER_ORG_IMAGE_NAME", "회원원본이미지명");
        	EXCELCOLUMINFO.put( "MEMBER_SAVE_IMAGE_NAME", "회원저장이미지명");
        	EXCELCOLUMINFO.put( "MEMEBER_TYPE", "회원구분 : R:정회원,E:준회원+정회원");
        	EXCELCOLUMINFO.put( "MENU_CODE", "메뉴코드");
        	EXCELCOLUMINFO.put( "MENU_LEVEL", "메뉴레벨");
        	EXCELCOLUMINFO.put( "MENU_NAME", "메뉴명");
        	EXCELCOLUMINFO.put( "MENU_ORDER", "");
        	EXCELCOLUMINFO.put( "MENU_TYPE", "사용자 : U, 관리자 : A");
        	EXCELCOLUMINFO.put( "MOBILE1", "");
        	EXCELCOLUMINFO.put( "MOBILE2", "");
        	EXCELCOLUMINFO.put( "MOBILE3", "");
        	EXCELCOLUMINFO.put( "MOD_DATE", "수정자ID");
        	EXCELCOLUMINFO.put( "MOD_DT", "수정일자");
        	EXCELCOLUMINFO.put( "MOD_ID", "승인일자");
        	EXCELCOLUMINFO.put( "MOD_TM", "수정시간");
        	EXCELCOLUMINFO.put( "MULTI_CHOICE_YN", "");
        	EXCELCOLUMINFO.put( "MULTI_YN", "");
        	EXCELCOLUMINFO.put( "NAME", "성명");
        	EXCELCOLUMINFO.put( "NICKNAME", "닉네임");
        	EXCELCOLUMINFO.put( "NOTI_END_DT", "");
        	EXCELCOLUMINFO.put( "NOTI_POPUP_YN", "");
        	EXCELCOLUMINFO.put( "NOTI_START_DT", "");
        	EXCELCOLUMINFO.put( "NOW_USER_GROUP", "일반/기업/전문가/담당자/일반관리자/시스템관리자");
        	EXCELCOLUMINFO.put( "OFF_IMAGE_NAME", "ON이미지명");
        	EXCELCOLUMINFO.put( "OFF_IMAGE_PATH", "ON이미지경로");
        	EXCELCOLUMINFO.put( "ON_IMAGE_NAME", "OFF이미지명");
        	EXCELCOLUMINFO.put( "ON_IMAGE_PATH", "OFF이미지경로");
        	EXCELCOLUMINFO.put( "OPEN", "공개/비공개");
        	EXCELCOLUMINFO.put( "OPEN_TYPE", "가입구분 : P:승인없이 회원가입,D:승인 후 회원가입");
        	EXCELCOLUMINFO.put( "OPEN_YN", "공개여부");
        	EXCELCOLUMINFO.put( "ORDER_SEQ", "");
        	EXCELCOLUMINFO.put( "ORG_FILE_NAME", "원본파일명");
        	EXCELCOLUMINFO.put( "ORG_IMAGE_FILE", "");
        	EXCELCOLUMINFO.put( "ORGAN_NM", "주관기관");
        	EXCELCOLUMINFO.put( "PARENT_SEQ", "");
        	EXCELCOLUMINFO.put( "PASSWORD", "비밀번호");
        	EXCELCOLUMINFO.put( "PASS_YN", "수료/미수료");
        	EXCELCOLUMINFO.put( "PERIOD_YN", "게시기간사용여부");
        	EXCELCOLUMINFO.put( "PHONE", "전화번호");
        	EXCELCOLUMINFO.put( "PHONE1", "");
        	EXCELCOLUMINFO.put( "PHONE2", "");
        	EXCELCOLUMINFO.put( "PHONE3", "");
        	EXCELCOLUMINFO.put( "PLACE_NM", "사용처");
        	EXCELCOLUMINFO.put( "PLACE_SEQ", "");
        	EXCELCOLUMINFO.put( "POLL_SEQ", "");
        	EXCELCOLUMINFO.put( "POPUP_BEGIN_DTTM", "");
        	EXCELCOLUMINFO.put( "POPUP_DESC", "");
        	EXCELCOLUMINFO.put( "POPUP_END_DTTM", "");
        	EXCELCOLUMINFO.put( "POPUP_ID", "");
        	EXCELCOLUMINFO.put( "POPUP_TITLE", "");
        	EXCELCOLUMINFO.put( "POPUP_URL", "");
        	EXCELCOLUMINFO.put( "POSITION_LEFT", "위치_X축");
        	EXCELCOLUMINFO.put( "POSITION_TOP", "위치_Y축");
        	EXCELCOLUMINFO.put( "POSITION_X", "");
        	EXCELCOLUMINFO.put( "POSITION_Y", "");
        	EXCELCOLUMINFO.put( "POST_END_DT", "");
        	EXCELCOLUMINFO.put( "POST_PERIOD_YN", "게시기간사용여부");
        	EXCELCOLUMINFO.put( "POST_START_DT", "");
        	EXCELCOLUMINFO.put( "POST_USE_YN", "");
        	EXCELCOLUMINFO.put( "PRE_NEXT_YN", "이전글다음글사용");
        	EXCELCOLUMINFO.put( "PREPAY_YN", "선급");        	
        	EXCELCOLUMINFO.put( "PRING_ORDER", "출력순서");
        	EXCELCOLUMINFO.put( "PRINT_ORDER", "출력순서");
        	EXCELCOLUMINFO.put( "PROC_STAT_C", "신청/접수/완료");
        	EXCELCOLUMINFO.put( "PWD", "");
        	EXCELCOLUMINFO.put( "PW_MOD_DT", "");
        	EXCELCOLUMINFO.put( "QUEST_SEQ", "");
        	EXCELCOLUMINFO.put( "RATING", "");
        	EXCELCOLUMINFO.put( "READ_AUTH", "읽기권한");
        	EXCELCOLUMINFO.put( "READ_AUTH_YN", "읽기권한여부");
        	EXCELCOLUMINFO.put( "READ_CNT", "조회수");
        	EXCELCOLUMINFO.put( "READ_LOGIN_YN", "");
        	EXCELCOLUMINFO.put( "REALM", "분야");
        	EXCELCOLUMINFO.put( "RECOMMEND_YNNM", "벤처협회추천");        	
        	EXCELCOLUMINFO.put( "REF_BOARD_ID", "참조게시판ID");
        	EXCELCOLUMINFO.put( "REF_DIVISION", "공통코드(1:교육, 2:행사, 3:YES리더)");
        	EXCELCOLUMINFO.put( "REF_LEVEL", "참조레벨");
        	EXCELCOLUMINFO.put( "REF_SEQ", "설문을 사용하는 행사(교육, 행사, YES리더)의 SEQ");
        	EXCELCOLUMINFO.put( "REG_DATE", "등록일자");
        	EXCELCOLUMINFO.put( "REG_DIV", "등록자구분");
        	EXCELCOLUMINFO.put( "REG_DIV_G", "U:사용자, A:관리자 	");
        	EXCELCOLUMINFO.put( "REG_DT", "등록일자");
        	EXCELCOLUMINFO.put( "REG_ID", "등록자ID");
        	EXCELCOLUMINFO.put( "REG_TM", "등록시간");
        	EXCELCOLUMINFO.put( "RENT_DTTM", "");
        	EXCELCOLUMINFO.put( "RENT_STATUS_CD", "대여가능/대여중");
        	EXCELCOLUMINFO.put( "RENT_USER_ID", "");
        	EXCELCOLUMINFO.put( "REPLY_AUTH", "답변권한");
        	EXCELCOLUMINFO.put( "REPLY_AUTH_YN", "답변권한여부");
        	EXCELCOLUMINFO.put( "REPLY_YN", "답변사용");
        	EXCELCOLUMINFO.put( "REQUIRED_YN", "");
        	EXCELCOLUMINFO.put( "RETURN_DTTM", "");
        	EXCELCOLUMINFO.put( "RE_ARTICLE_SEQ", "");
        	EXCELCOLUMINFO.put( "RE_GRP", "참조번호");
        	EXCELCOLUMINFO.put( "RE_LEVEL", "답글레벨");
        	EXCELCOLUMINFO.put( "RE_ORDER", "");
        	EXCELCOLUMINFO.put( "RE_STEP", "답글순서");
        	EXCELCOLUMINFO.put( "RE_UP_REF", "상위게시물번호");
        	EXCELCOLUMINFO.put( "RI", "도서");
        	EXCELCOLUMINFO.put( "RQST_DT", "");
        	EXCELCOLUMINFO.put( "RQST_SEQ", "");
        	EXCELCOLUMINFO.put( "RSS_YN", "");
        	EXCELCOLUMINFO.put( "SAVE_FILE_NAME", "저장파일명");
        	EXCELCOLUMINFO.put( "SAVE_IMAGE_FILE", "");
        	EXCELCOLUMINFO.put( "SEQ", "일련번호");
        	EXCELCOLUMINFO.put( "SIDO", "시군구");
        	EXCELCOLUMINFO.put( "SITE_DESC", "사이트설명");
        	EXCELCOLUMINFO.put( "SITE_ID", "사이트ID");
        	EXCELCOLUMINFO.put( "SITE_NAME", "사이트명");
        	EXCELCOLUMINFO.put( "SITE_URL", "사이트URL");
        	EXCELCOLUMINFO.put( "SIZE_X", "");
        	EXCELCOLUMINFO.put( "SIZE_Y", "");
        	EXCELCOLUMINFO.put( "START_DATE", "게시시작기간");
        	EXCELCOLUMINFO.put( "START_DT", "");
        	EXCELCOLUMINFO.put( "START_TIME", "");
        	EXCELCOLUMINFO.put( "STATE_YN", "등록/접수/처리완료등 상태 사용여부");
        	EXCELCOLUMINFO.put( "STOP_VIEW_YN", "");
        	EXCELCOLUMINFO.put( "STOCK_DT", "입/출고일");
        	EXCELCOLUMINFO.put( "TARGET", "");
        	EXCELCOLUMINFO.put( "TEL", "전화번호");
        	EXCELCOLUMINFO.put( "TITLE", "제목");
        	EXCELCOLUMINFO.put( "TOTAL_PEOPLE", "");
        	EXCELCOLUMINFO.put( "TO_ADMIN", "관리자에게 작성");
        	EXCELCOLUMINFO.put( "TYPE", "구분");
        	EXCELCOLUMINFO.put( "UPLOAD_EXT", "업로드확장자");
        	EXCELCOLUMINFO.put( "UPPER_CODE", "상위코드");
        	EXCELCOLUMINFO.put( "UPPER_MENU_CODE", "상위메뉴코드");
        	EXCELCOLUMINFO.put( "USER_GROUP", "일반/기업/전문가/담당자/일반관리자/시스템관리자 그룹등");
        	EXCELCOLUMINFO.put( "USER_GROUP_NAME", "일반/기업/전문가/담당자/일반관리자/시스템관리자 그룹등");
        	EXCELCOLUMINFO.put( "USER_ID", "작성자ID");
        	EXCELCOLUMINFO.put( "USER_RQST_YN", "");
        	EXCELCOLUMINFO.put( "USE_YN", "사용여부");
        	EXCELCOLUMINFO.put( "VIEW_LOGIN_YN", "상세정보로그인");
        	EXCELCOLUMINFO.put( "VISIT_DATE", "방문일자");
        	EXCELCOLUMINFO.put( "VISIT_DTTM", "방문일자");
        	EXCELCOLUMINFO.put( "VISIT_IP", "방문IP");
        	EXCELCOLUMINFO.put( "WIDTH", "창넓이");
        	EXCELCOLUMINFO.put( "WRITE_AUTH", "등록권한");
        	EXCELCOLUMINFO.put( "WRITE_AUTH_YN", "등록권한여부");
        	EXCELCOLUMINFO.put( "WRITE_GRP_YN", "매핑정보는 별도테이블관리");
        	EXCELCOLUMINFO.put( "WRITE_LOGIN_YN", "등록로그인");
        	EXCELCOLUMINFO.put( "WRITE_ROLE", "등록역할");
        	EXCELCOLUMINFO.put( "ZIP_CD", "");
        	EXCELCOLUMINFO.put( "ZIP_CODE", "시도");
        	

        	EXCELCOLUMINFO.put( "INFO_YEAR", "행사년도");
        	EXCELCOLUMINFO.put( "INFO_NUM", "행사차수");
        	EXCELCOLUMINFO.put( "PROGRAM_NAME", "행사프로그램명");
        	EXCELCOLUMINFO.put( "EVENT_DT", "행사일자");
        	EXCELCOLUMINFO.put( "EVENT_TM", "행사시간");
        	EXCELCOLUMINFO.put( "WORKPLACE", "직장");
        	EXCELCOLUMINFO.put( "NATIONALITY", "국적");
        	EXCELCOLUMINFO.put( "JOB_NM", "직업");
        	EXCELCOLUMINFO.put( "COUNTRY_LIVE", "거주국가");
        	
        	// 사용자 정의
        	EXCELCOLUMINFO.put( "CD", "코드");
        	EXCELCOLUMINFO.put( "GROUP_CODE", "그룹코드");
        	EXCELCOLUMINFO.put( "APPLY_STATUS_NM", "승인여부");
        	EXCELCOLUMINFO.put( "PASS_NM", "수료여부");
        	EXCELCOLUMINFO.put( "MOBILE", "휴대폰");
        	EXCELCOLUMINFO.put( "JIKWE", "직위");
        	EXCELCOLUMINFO.put( "COMPANY", "소속기관");
        	
        	EXCELCOLUMINFO.put( "DEPT_NAME", "부서");
        	EXCELCOLUMINFO.put( "APPLY_COUNT", "참여인원");
        	EXCELCOLUMINFO.put( "ATTEND_YN_NM", "참석여부");
        	
        	EXCELCOLUMINFO.put( "EXPERT_TYPE_CDNM", "전문가구분");
        	EXCELCOLUMINFO.put( "AREANM", "지역");
        	EXCELCOLUMINFO.put( "STATUS_CDNM", "상태");
        	EXCELCOLUMINFO.put( "EN_TALK_LEVEL", "영어말하기활용능력");
        	EXCELCOLUMINFO.put( "EN_LISTEN_LEVEL", "영어듣기활용능력");
        	EXCELCOLUMINFO.put( "EN_WRITE_LEVEL", "영어쓰기활용능력");
        	EXCELCOLUMINFO.put( "EN_READ_LEVEL", "영어읽기활용능력");
        	EXCELCOLUMINFO.put( "SUPPORT_WORKPLACE", "학교");
        	EXCELCOLUMINFO.put( "SUPPORT_JIKWE", "학년/학과");
        	
        	
        	
        	
        }
    }
}
