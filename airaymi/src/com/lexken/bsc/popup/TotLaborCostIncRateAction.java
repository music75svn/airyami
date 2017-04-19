package com.lexken.bsc.popup;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class TotLaborCostIncRateAction extends CommonService {
	private static final long serialVersionUID = 1L;

	// Logger
	private final Log logger = LogFactory.getLog(getClass());

    /**
     * 시스템 연계데이터 조회
     * @param      
     * @return String  
     * @throws 
     */
	public SearchMap totLaborCostIncRateList(SearchMap searchMap) {
		if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.totLaborCostIncRate.getScDeptMapping", searchMap));
		}
		
		// 최상위 평가조직 조회
		searchMap.addList("deptInfo", getList("bsc.popup.totLaborCostIncRate.getDeptList", searchMap));

		searchMap.addList("itemInfo", getList("bsc.popup.totLaborCostIncRate.getItem", searchMap));

		return searchMap;
	}
    
    /**
     * 시스템 연계데이터 조회 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap totLaborCostIncRateList_xml(SearchMap searchMap) {
        
    	String itemCd = searchMap.getString("findItemCd");
    	
    	if ("S069D001".equals(itemCd)) {	// 기본급(총인건비인상률)
    		searchMap.addList("list", getList("bsc.popup.totLaborCostIncRate.S069D001", searchMap));
    	} else if ("S069D002".equals(itemCd)) {	// 제수당(총인건비인상률)
    		searchMap.addList("list", getList("bsc.popup.totLaborCostIncRate.S069D002", searchMap));
    	} else if ("S069D003".equals(itemCd)) {	// 성과연봉(총인건비인상률)
    		searchMap.addList("list", getList("bsc.popup.totLaborCostIncRate.S069D003", searchMap));
    	} else if ("S069D004".equals(itemCd)) {	// 교통보조비(총인건비인상률)
    		searchMap.addList("list", getList("bsc.popup.totLaborCostIncRate.S069D004", searchMap));
    	} else if ("S069D005".equals(itemCd)) {	// 자가운전보조금(총인건비인상률)
    		searchMap.addList("list", getList("bsc.popup.totLaborCostIncRate.S069D005", searchMap));
    	} else if ("S069D006".equals(itemCd)) {	// 자기계발비(총인건비인상률)
    		searchMap.addList("list", getList("bsc.popup.totLaborCostIncRate.S069D006", searchMap));
    	} else if ("S069D007".equals(itemCd)) {	// 통신비(총인건비인상률)
    		searchMap.addList("list", getList("bsc.popup.totLaborCostIncRate.S069D007", searchMap));
    	} else if ("S069D008".equals(itemCd)) {	// 특별근로대가(총인건비인상률)
    		searchMap.addList("list", getList("bsc.popup.totLaborCostIncRate.S069D008", searchMap));
    	} else if ("S069D009".equals(itemCd)) {	// 중식보조비(총인건비인상률)
    		searchMap.addList("list", getList("bsc.popup.totLaborCostIncRate.S069D009", searchMap));
    	} else if ("S069D010".equals(itemCd)) {	// 학자금(총인건비인상률)
    		searchMap.addList("list", getList("bsc.popup.totLaborCostIncRate.S069D010", searchMap));
    	}
    	

        return searchMap;
    }
    
    /**
     * 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap excel(SearchMap searchMap) {
    	String excelFileName = "총인건비인상률";
    	String excelTitle = "총인건비인상률";
    	String itemCd = searchMap.getString("findItemCd");
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("월", (String)searchMap.get("monNm")));
    	excelSearchInfoList.add(new ExcelVO("부서", (String)searchMap.get("deptNm")));
    	excelSearchInfoList.add(new ExcelVO("항목", (String)searchMap.get("itemNm")));
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();

    	if ("S069D001".equals(itemCd)) {	// 기본급(총인건비인상률)
    		excelInfoList.add(new ExcelVO("지급년월","SLRY_YM", "center"));
        	excelInfoList.add(new ExcelVO("사번", "EMPN", "center"));
        	excelInfoList.add(new ExcelVO("이름", "KOR_NM", "center"));
        	excelInfoList.add(new ExcelVO("부서", "DEPT_NM", "left"));
        	excelInfoList.add(new ExcelVO("직급", "CAST_TC_NM", "center"));
        	excelInfoList.add(new ExcelVO("기본연봉", "E00", "center"));
        	excelInfoList.add(new ExcelVO("호봉급", "E01", "center"));
        	excelInfoList.add(new ExcelVO("직무급", "E02", "center"));
        	excelInfoList.add(new ExcelVO("안전급", "E03", "center"));
        	excelInfoList.add(new ExcelVO("합계", "TOTAL", "center"));
    	} else if ("S069D002".equals(itemCd)) {	// 제수당(총인건비인상률)
    		excelInfoList.add(new ExcelVO("지급년월","SLRY_YM", "center"));
        	excelInfoList.add(new ExcelVO("사번", "EMPN", "center"));
        	excelInfoList.add(new ExcelVO("이름", "KOR_NM", "center"));
        	excelInfoList.add(new ExcelVO("부서", "DEPT_NM", "left"));
        	excelInfoList.add(new ExcelVO("직급", "CAST_TC_NM", "center"));
        	excelInfoList.add(new ExcelVO("연차수당", "E41", "center"));
        	excelInfoList.add(new ExcelVO("근속수당", "E42", "center"));
        	excelInfoList.add(new ExcelVO("시간외수당", "E51", "center"));
        	excelInfoList.add(new ExcelVO("야간근무수당", "E52", "center"));
        	excelInfoList.add(new ExcelVO("휴일근무수당", "E53", "center"));
        	excelInfoList.add(new ExcelVO("기술수당", "E61", "center"));
        	excelInfoList.add(new ExcelVO("연구수당", "E71", "center")); 
        	excelInfoList.add(new ExcelVO("감사수당", "E72", "center"));
        	excelInfoList.add(new ExcelVO("전산수당", "E73", "center"));
        	excelInfoList.add(new ExcelVO("강사수당", "E74", "center"));
        	excelInfoList.add(new ExcelVO("출납수당", "E75", "center"));
        	excelInfoList.add(new ExcelVO("파견수당", "E76", "center"));
        	excelInfoList.add(new ExcelVO("주임수당", "E81", "center"));
        	excelInfoList.add(new ExcelVO("가족수당", "E82", "center"));
        	excelInfoList.add(new ExcelVO("기타수당", "E84", "center"));
        	excelInfoList.add(new ExcelVO("특수업무수당", "E85", "center"));
        	excelInfoList.add(new ExcelVO("정산금(지급)", "E99", "center"));
        	excelInfoList.add(new ExcelVO("합계", "TOTAL", "center"));
    	} else if ("S069D003".equals(itemCd)) {	// 성과연봉(총인건비인상률)
    		excelInfoList.add(new ExcelVO("지급년월","SLRY_YM", "center"));
        	excelInfoList.add(new ExcelVO("사번", "EMPN", "center"));
        	excelInfoList.add(new ExcelVO("이름", "KOR_NM", "center"));
        	excelInfoList.add(new ExcelVO("부서", "DEPT_NM", "left"));
        	excelInfoList.add(new ExcelVO("직급", "CAST_TC_NM", "center"));
        	excelInfoList.add(new ExcelVO("성과연봉", "E13", "center"));
    	} else if ("S069D004".equals(itemCd) || // 교통보조비(총인건비인상률)
    			"S069D005".equals(itemCd) || // 자가운전보조금(총인건비인상률)
    			"S069D006".equals(itemCd) || // 자기계발비(총인건비인상률)
    			"S069D007".equals(itemCd) || // 통신비(총인건비인상률)
    			"S069D008".equals(itemCd) || // 특별근로대가(총인건비인상률)
    			"S069D010".equals(itemCd) // 학자금(총인건비인상률)
    			) {	
    		excelInfoList.add(new ExcelVO("전표일자", "SLIP_DATE", "center"));
        	excelInfoList.add(new ExcelVO("사번", "EMPN", "center"));
        	excelInfoList.add(new ExcelVO("이름", "KOR_NM", "center"));
        	excelInfoList.add(new ExcelVO("부서", "DEPT_NM", "left"));
        	excelInfoList.add(new ExcelVO("금액", "INCO_AMT", "center"));
        	excelInfoList.add(new ExcelVO("비고", "RMK", "left"));
    	} else if ("S069D009".equals(itemCd)) {	// 중식보조비(총인건비인상률)
    		excelInfoList.add(new ExcelVO("지급년월","SLRY_YM", "center"));
        	excelInfoList.add(new ExcelVO("사번", "EMPN", "center"));
        	excelInfoList.add(new ExcelVO("이름", "KOR_NM", "center"));
        	excelInfoList.add(new ExcelVO("부서", "DEPT_NM", "left"));
        	excelInfoList.add(new ExcelVO("직급", "CAST_TC_NM", "center"));
        	excelInfoList.add(new ExcelVO("중식보조비", "E92", "center"));
    	} 
    							
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	if ("S069D001".equals(itemCd)) {	// 기본급(총인건비인상률)
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.totLaborCostIncRate.S069D001", searchMap));
    	} else if ("S069D002".equals(itemCd)) {	// 제수당(총인건비인상률)
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.totLaborCostIncRate.S069D002", searchMap));
    	} else if ("S069D003".equals(itemCd)) {	// 성과연봉(총인건비인상률)
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.totLaborCostIncRate.S069D003", searchMap));
    	} else if ("S069D004".equals(itemCd)) {	// 교통보조비(총인건비인상률)
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.totLaborCostIncRate.S069D004", searchMap));
    	} else if ("S069D005".equals(itemCd)) {	// 자가운전보조금(총인건비인상률)
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.totLaborCostIncRate.S069D005", searchMap));
    	} else if ("S069D006".equals(itemCd)) {	// 자기계발비(총인건비인상률)
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.totLaborCostIncRate.S069D006", searchMap));
    	} else if ("S069D007".equals(itemCd)) {	// 통신비(총인건비인상률)
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.totLaborCostIncRate.S069D007", searchMap));
    	} else if ("S069D008".equals(itemCd)) {	// 특별근로대가(총인건비인상률)
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.totLaborCostIncRate.S069D008", searchMap));
    	} else if ("S069D009".equals(itemCd)) {	// 중식보조비(총인건비인상률)
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.totLaborCostIncRate.S069D009", searchMap));
    	} else if ("S069D010".equals(itemCd)) {	// 학자금(총인건비인상률)
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.totLaborCostIncRate.S069D010", searchMap));
    	}
    	
    	return searchMap;
    }
}
