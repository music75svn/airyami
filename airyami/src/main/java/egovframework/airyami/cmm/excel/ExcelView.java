package egovframework.airyami.cmm.excel;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import egovframework.airyami.cmm.util.CommonUtils;
import egovframework.airyami.cmm.util.ValueMap;


@Controller
public class ExcelView extends AbstractExcelView {

	private static final Logger logger = Logger.getLogger(ExcelView.class.getName());
	
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
	                        throws Exception{
		// 메뉴별로 엑셀 순번이 정의되어 있음. Constants_excelCol.java에서 찾아 보세요.
		System.out.println("Excelview == start~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ");
		ConstantsExcelColDwise.initExcel(); // 엑셀 정보 초기화
	    String excel_name =  (String)model.get("excel_name");
	    
	    String excel_condition =  (String)model.get("excel_condition");	// 조회조건
	    System.out.println("Excelview == excel_condition " + excel_condition);
	    String[] conditionArr = null;
	    if( !CommonUtils.isNull(excel_condition) )
	    	conditionArr = excel_condition.split("\\|"); 
	    
	    
		String fileName =  ConstantsExcelColDwise.get_GC_TITLE( excel_name );// (String)model.get("title");
		System.out.println("Excelview == fileName " + fileName);

		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String((fileName).getBytes("KSC5601"),"8859_1") + "_" +getTimeStamp()+ ".xls" + "\";");
		//response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "_" +getTimeStamp()+ ".xls" + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
				
		ValueMap rowData = null;
		
		List<ValueMap> excelData = (List<ValueMap>)model.get("data");
        ArrayList<String> columnLabelList = new ArrayList<String>();
		
		Map mapColInfo = ConstantsExcelColDwise.getExcelColumInfo();
        
        String strCol = ConstantsExcelColDwise.get_GC_PERF_COLUME(excel_name);      
        String[] strColArr = strCol.split( "," ); 
        
        for( int i = 0 ; i < strColArr.length; i++ ) {
            columnLabelList.add( CommonUtils.NVL( (String)mapColInfo.get( strColArr[i] ), " " ) );
        }
        
        System.out.println("columnLabelList :: " + columnLabelList.size());
        
        ExcelStyle excelStyle = new ExcelStyle();
        Map<String, CellStyle> styles = excelStyle.createStyles( workbook );
        CellStyle headerStyle = styles.get( "header" );
        headerStyle = excelStyle.setLineStyle( headerStyle );
        CellStyle titleStyle = styles.get( "title" );
        titleStyle = excelStyle.setLineStyle( titleStyle );
        CellStyle titleSerchStyle = styles.get( "titleSerch" );
        titleSerchStyle = excelStyle.setLineStyle( titleSerchStyle );
        CellStyle titleConditionStyle = styles.get( "titleCondition" );
        titleConditionStyle = excelStyle.setLineStyle( titleConditionStyle );
        CellStyle contentConditionStyle = styles.get( "contentCondition" );
        contentConditionStyle = excelStyle.setLineStyle( contentConditionStyle );
		
		HSSFSheet sheet = workbook.createSheet();
		int iStartRow = 0;
		sheet = createTitle(titleStyle, fileName, sheet, columnLabelList.size(), iStartRow++);
		if(conditionArr != null){
			int iConCnt = conditionArr.length;
			long lRowCnt = Math.round( iConCnt / 2.0 );
			sheet = createCondition(titleSerchStyle, titleConditionStyle, contentConditionStyle, conditionArr, sheet, columnLabelList.size(), iStartRow);
			iStartRow += lRowCnt + 1;	// 검색조건 표시줄
		}
		sheet = createColumnLabel(headerStyle, columnLabelList, sheet, iStartRow++);
		sheet = createData_ValueMap(workbook, excelData, sheet, excel_name, iStartRow++);
		// TODO Auto-generated method stub
		System.out.println("EXCEL END!!! :: ");
		
	}

	public HSSFSheet createTitle(CellStyle titleStyle, String fileName, HSSFSheet sheet, int iMergeCnt, int iStartRow){
		// Insert Column Name
		
		HSSFRow firstRow = sheet.createRow(iStartRow);
		
		sheet.addMergedRegion(new Region(( int) iStartRow , ( short )0 , ( int) iStartRow, (short )(iMergeCnt -1) ));
		
		HSSFCell cell = null;
		
		firstRow.setHeight((short)1000);
		cell = firstRow.createCell(0);
		cell.setCellValue(fileName);
		cell.setCellStyle( titleStyle );
		sheet.setColumnWidth( 0, 5000 );
		
		return sheet;
	}
	
	public HSSFSheet createCondition(CellStyle titleSerchStyle,CellStyle titleConditionStyle, CellStyle contentConditionStyle, String[] conditionArr, HSSFSheet sheet, int iMergeCnt, int iStartRow){
		// Insert Column Name
		
		HSSFRow firstRow = sheet.createRow(iStartRow);
		
		sheet.addMergedRegion(new Region(( int) iStartRow , ( short )0 , ( int) iStartRow, (short )(iMergeCnt -1) ));
		iStartRow++;
		
		HSSFCell cell = null;
		cell = firstRow.createCell(0);
		cell.setCellValue("검색 조건");
		cell.setCellStyle( titleSerchStyle );
		firstRow.setHeight((short)500);
		sheet.setColumnWidth( 0, 5000 );
		
		int startCol = 0;
		HSSFRow row = null;
		for (int i = 0; i < conditionArr.length; i++) {
			// 처음에만 로우 추가
			if((i % 2) == 0){
				row = sheet.createRow(iStartRow);
				sheet.addMergedRegion(new Region(( int) iStartRow , ( short )1 , ( int) iStartRow, (short )2 ));
				sheet.addMergedRegion(new Region(( int) iStartRow , ( short )4 , ( int) iStartRow, (short )5 ));
				
				iStartRow++;
				startCol = 0;
			}
			
			String[] sConArr = conditionArr[i].split("=");
			
			System.out.println("conditionArr :: " + conditionArr[i]);
            
        	cell = row.createCell(startCol);
        	cell.setCellValue(sConArr[0]);
        	cell.setCellStyle( titleConditionStyle );
        	sheet.setColumnWidth( startCol, 5000 );
        	
        	cell = row.createCell(startCol + 1);
        	cell.setCellValue(sConArr[1]);
        	cell.setCellStyle( contentConditionStyle );
        	sheet.setColumnWidth( startCol + 1, 5000 );
        	
        	startCol = 3;	// 다음 검색조건 시작 컬럼 위치
        }
		
		return sheet;
	}
	
    public HSSFSheet createColumnLabel(CellStyle headerStyle, ArrayList<String> columnLabelList, HSSFSheet sheet, int iStartRow){
        // Insert Column Name
        
        HSSFRow firstRow = sheet.createRow(iStartRow);
        HSSFCell cell = null;
        
        for (int i = 0; i < columnLabelList.size(); i++) {
            cell = firstRow.createCell(i);
            cell.setCellValue(columnLabelList.get(i));
            cell.setCellStyle( headerStyle );
            sheet.setColumnWidth( i, 5000 );
        }
        return sheet;
    }
    
    public HSSFSheet createData_ValueMap(HSSFWorkbook workbook, List<ValueMap> data, HSSFSheet sheet, String excel_name, int iStartRow) {
        String strCol = ConstantsExcelColDwise.get_GC_PERF_COLUME(excel_name);       
        String[] strColArr = strCol.split( "," );
        String strValue = "";
        
        ExcelStyle excelStyle = new ExcelStyle();
        CellStyle rowStyle = null;
        CellStyle rowStyle1 = workbook.createCellStyle();
        CellStyle rowStyle2 = workbook.createCellStyle();
        CellStyle rowStyle_SubSum = workbook.createCellStyle();
        CellStyle rowStyle_TotSum = workbook.createCellStyle();
        rowStyle1 = excelStyle.setLineStyle( rowStyle1 );
        rowStyle2 = excelStyle.setEvenStyle( rowStyle2 );
        rowStyle_SubSum = excelStyle.setSubSumStyle( rowStyle_SubSum );
        rowStyle_TotSum = excelStyle.setTotSumStyle( rowStyle_TotSum );
        
        for (int i = 0; i < data.size(); i++) {
            ValueMap rowData = (ValueMap)data.get(i);
            HSSFRow row = sheet.createRow(i+iStartRow);//0 is header
            
            HSSFCell cell = row.createCell(0);
            int cellIdx = 0;
            
            // style 선택
            rowStyle = rowStyle1;
            
            strValue = (String)rowData.getString( "SUBSUM_YN" );
            if( "Y".equals( strValue ) ){
                rowStyle = rowStyle_SubSum;
            }
            
            strValue = (String)rowData.getString( "TOTSUM_YN" );
            if( "Y".equals( strValue ) ){
                rowStyle = rowStyle_TotSum;
            }

            
            for(int j = 0; j < strColArr.length; j++) {
                                
                cell = row.createCell(cellIdx);
                strValue = (String)rowData.getString( strColArr[j] );
                if(strValue == null)
                    strValue = "";
                //날짜 형식 변환하기
                if(strColArr[j].contains( "_DT" ) && strValue.length() == 8 ){
                    strValue = CommonUtils.dateFormat(strValue, '-');
                }
                // 사업자번호 변환하기
                else if( (strColArr[j].contains( "_BIZ_NO" ) || "BIZ_NO".equals( strColArr[j] ) )&& strValue.length() == 10  ){
                    strValue = CommonUtils.bizNoFormat(strValue, '-');
                }
                // 법인번호 변환
                else if (strColArr[j].contains( "CORP_NO" )) {
                    strValue = CommonUtils.corpNoFormat(strValue, '-');
                }
                // 금액 변환하기
                else if(strColArr[j].contains( "_AMT" )) {
                    strValue = CommonUtils.amtFormat(strValue);
                } else if(strColArr[j].contains( "_COST" )) {
                    strValue = CommonUtils.amtFormat(strValue);
                }
                cell.setCellValue( strValue );                
                
                cell.setCellStyle( rowStyle );

                cellIdx++;
            }

        }
        
        return sheet;
    }
    
    private static String getTimeStamp() {
        
        String rtnStr = null;
        
        // 문자열로 변환하기 위한 패턴 설정(년도-월-일)
        String pattern = "yyyyMMdd";
        
        try {
            SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            
            rtnStr = sdfCurrent.format(ts.getTime());
        } catch (Exception e) {
            
            logger.debug("IGNORED: " + e.getMessage());
        }
        
        return rtnStr;
    }
}
