package com.lexken.framework.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

import com.lexken.framework.config.CommonConfig;


public class HtmlHelper
{
	private final static String conextPath = "";
	//private final static String conextPath = "/BSC_PROJECT";
	/**
	 * 1980년대부터 ~ 올해 + 1년 이다.
	 * @return
	 */
	public static String getHtmlCalendarAllYear()
	{
		CalendarHelper csCal 	= new CalendarHelper();
		FormatHelper csFormat	= new FormatHelper();

		String stOptions 	= "";
		int nYear			= csCal.getYear();

		try
		{
			for( int nTi = nYear + 5; nTi > 1999; nTi-- )
			{
				stOptions += "<option value='" + nTi + "'>" + nTi + "년</option>";
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return stOptions;
	}

	/**
	 * 1980년대부터 ~ 올해 + 1년 이다.
	 * 선택한 연도에 selected
	 * @param Year
	 * @return
	 */
	public static String getHtmlCalendarAllYear( int nChoiceYear )
	{
		CalendarHelper csCal = new CalendarHelper();
		String stOptions 	= "";
		int nYear			= csCal.getYear();

		try
		{
			for( int nTi = nYear + 5; nTi > 1999; nTi-- )
			{
				if( nTi == nChoiceYear )
				{
					stOptions += "<option value='" + nTi + "' selected=\"selected\" >" + nTi + "년</option>";
				}
				else
				{
					stOptions += "<option value='" + nTi + "'>" + nTi + "년</option>";
				}
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return stOptions;
	}

	/**
	 * 1980년대부터 ~ 올해 + 1년 이다.
	 * 선택한 연도에 selected
	 * @param Year
	 * @return
	 */
	public static String getHtmlCalendarAllYear( String stChoiceYear )
	{
		CalendarHelper csCal = new CalendarHelper();
		String stOptions 	= "";
		int nYear			= csCal.getYear();

		try
		{
			for( int nTi = nYear + 5; nTi > 1999; nTi-- )
			{
				if( String.valueOf(nTi).equals(stChoiceYear) )
				{
					stOptions += "<option value='" + nTi + "' selected=\"selected\" >" + nTi + "년</option>";
				}
				else
				{
					stOptions += "<option value='" + nTi + "'>" + nTi + "년</option>";
				}
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return stOptions;
	}
	
	
	/**
	 * 2000년대부터 ~ 올해 + 10년 이다.
	 * @return
	 */
	public static String getHtmlCalendarOver10Year()
	{
		CalendarHelper csCal 	= new CalendarHelper();
		FormatHelper csFormat	= new FormatHelper();

		String stOptions 	= "";
		int nYear			= csCal.getYear();

		try
		{
			for( int nTi = nYear + 10; nTi > 1990; nTi-- )
			{
				stOptions += "<option value='" + nTi + "'>" + nTi + "년</option>";
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return stOptions;
	}

	/**
	 * 2000년대부터 ~ 올해 + 10년 이다.
	 * 선택한 연도에 selected
	 * @param Year
	 * @return
	 */
	public static String getHtmlCalendarOver10Year( int nChoiceYear )
	{
		CalendarHelper csCal = new CalendarHelper();
		String stOptions 	= "";
		int nYear			= csCal.getYear();

		try
		{
			for( int nTi = nYear + 10; nTi > 1990; nTi-- )
			{
				if( nTi == nChoiceYear )
				{
					stOptions += "<option value='" + nTi + "' selected=\"selected\" >" + nTi + "년</option>";
				}
				else
				{
					stOptions += "<option value='" + nTi + "'>" + nTi + "년</option>";
				}
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return stOptions;
	}

	/**
	 * 2000년대부터 ~ 올해 + 10년 이다.
	 * 선택한 연도에 selected
	 * @param Year
	 * @return
	 */
	public static String getHtmlCalendarOver10Year( String stChoiceYear )
	{
		CalendarHelper csCal = new CalendarHelper();
		String stOptions 	= "";
		int nYear			= csCal.getYear();

		try
		{
			for( int nTi = nYear + 10; nTi > 1990; nTi-- )
			{
				if( String.valueOf(nTi).equals(stChoiceYear) )
				{
					stOptions += "<option value='" + nTi + "' selected=\"selected\" >" + nTi + "년</option>";
				}
				else
				{
					stOptions += "<option value='" + nTi + "'>" + nTi + "년</option>";
				}
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return stOptions;
	}



	/**
	 * 입력한  연도부터 올해 + 1년
	 * @param Year - 올해보다 작은 연도를 입력
	 * @return
	 */
	public static String getHtmlCalendarYear( int nYear )
	{
		CalendarHelper csCal = new CalendarHelper();
		String stOptions 	= "";

		int toYear			= csCal.getYear();

		try
		{
			for( int nTi = toYear; nTi >= nYear; nTi-- )
			{
				stOptions += "<option value='" + nTi + "'>" + nTi + "년</option>";
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return stOptions;
	}

	/**
	 * 입력한  연도부터 올해 + 1년
	 * @param Year - 올해보다 작은 연도를 입력
	 * @return
	 */
	public static String getHtmlCalendarYear( String stYear )
	{
		CalendarHelper csCal = new CalendarHelper();
		String stOptions 	= "";

		int toYear			= csCal.getYear();

		try
		{
			for( int nTi = toYear; nTi >= Integer.parseInt(stYear); nTi-- )
			{
				stOptions += "<option value='" + nTi + "'>" + nTi + "년</option>";
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return stOptions;
	}

	/**
	 * 입력한 연도부터 올해 + 1년
	 * 선택한 연도에 selected
	 * @param Year
	 * @return
	 */
	public static String getHtmlCalendarYear( int nYear, int nChoiceYear )
	{
		CalendarHelper csCal = new CalendarHelper();
		String stOptions 	= "";
		int toYear			= csCal.getYear();

		try
		{
			for( int nTi = toYear; nTi >= nYear; nTi-- )
			{
				if( nTi == nChoiceYear )
				{
					stOptions += "<option value='" + nTi + "' selected=\"selected\" >" + nTi + "년</option>";
				}
				else
				{
					stOptions += "<option value='" + nTi + "'>" + nTi + "년</option>";
				}
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return stOptions;
	}

	/**
	 * 입력한 연도부터 올해 + 1년
	 * 선택한 연도에 selected
	 * @param Year
	 * @return
	 */
	public static String getHtmlCalendarYear( String stYear, String stChoiceYear )
	{
		CalendarHelper csCal = new CalendarHelper();
		String stOptions 	= "";
		int toYear			= csCal.getYear();

		try
		{
			for( int nTi = toYear; nTi >= Integer.parseInt(stYear); nTi-- )
			{
				if( String.valueOf(nTi).equals(stChoiceYear) )
				{
					stOptions += "<option value='" + nTi + "' selected=\"selected\" >" + nTi + "년</option>";
				}
				else
				{
					stOptions += "<option value='" + nTi + "'>" + nTi + "년</option>";
				}
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return stOptions;
	}

	/**
	 * 월 option
	 * 선택한 월에 selected
	 * @param int Month
	 * @return
	 */
	public static String getHtmlCalendarMonth( int nChoiceMonth )
	{
		FormatHelper csFormat = new FormatHelper();
		String stOptions 	= "";

		try
		{
			for( int nTi = 1; nTi < 13; nTi++ )
			{
				if( csFormat.monthOrDayFormat(nTi).equals(nChoiceMonth))
				{
					stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "' selected=\"selected\" >" + nTi + "월</option>";
				}
				else
				{
					stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "'>" + nTi + "월</option>";
				}
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return stOptions;
	}

	/**
	 * 월 option
	 * 선택한 월에 selected
	 * @param String Month
	 * @return
	 */
	public static String getHtmlCalendarMonth( String stChoiceMonth )
	{
	    FormatHelper csFormat = new FormatHelper();
		String stOptions 	= "";

		try
		{
			for( int nTi = 1; nTi < 13; nTi++ )
			{
				if( csFormat.monthOrDayFormat(nTi).equals(stChoiceMonth.trim()) )
				{
					stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "' selected=\"selected\" >" + nTi + "월</option>";
				}
				else
				{
					stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "'>" + nTi + "월</option>";
				}
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return stOptions;
	}
	
	/**
	 * 월 option
	 * 선택한 월에 selected
	 * @param Object Month
	 * @return
	 */
	public static String getHtmlCalendarMonth( Object stChoiceMonth )
	{
	    FormatHelper csFormat = new FormatHelper();
		String stOptions 	= "";

		try
		{
			for( int nTi = 1; nTi < 13; nTi++ )
			{
				if( csFormat.monthOrDayFormat(nTi).equals(String.valueOf(stChoiceMonth).trim()) )
				{
					stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "' selected=\"selected\" >" + nTi + "월</option>";
				}
				else
				{
					stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "'>" + nTi + "월</option>";
				}
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return stOptions;
	}

	/**
     * 월 option
     * @param 
     * @return
     */
	public static String getHtmlCalendarMonth()
	{
	    FormatHelper csFormat = new FormatHelper();
		String stOptions 	= "";

		try
		{
			for( int nTi = 1; nTi < 13; nTi++ )
			{
				stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "'>" + nTi + "월</option>";
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return stOptions;
	}
	
	/**
     * 월 option(분기)
     * 선택한 월에 selected
     * @param String Month
     * @return
     */
    public static String getHtmlCalendarMonthQuarter( String stChoiceMonth )
    {
        FormatHelper csFormat = new FormatHelper();
        String stOptions    = "";

        try
        {
            for( int nTi = 3; nTi < 13; nTi+=3 )
            {
                if( csFormat.monthOrDayFormat(nTi).equals(stChoiceMonth.trim()) )
                {
                    stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "' selected=\"selected\" >" + nTi + "월</option>";
                }
                else
                {
                    stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "'>" + nTi + "월</option>";
                }
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        return stOptions;
    }
    
	/**
     * 월 option(평가주기) : 하윤식(2012.7.5 추가)
     * 선택한 월에 selected
     * @param String Month
     * @return
     */
    public static String getEvalCycleCalendar(String stEvalCycle, int nCalIndex, ArrayList targetList)
    {
        FormatHelper csFormat = new FormatHelper();
        String stOptions = "";
        int startMonth = 1;
        String stChoiceMonth = "";

        try {
        	if("M".equals(stEvalCycle)) {  //평가주기 : 월
        		
        		stChoiceMonth = getSelectMonth(stEvalCycle, startMonth, targetList);
        		
        		if(csFormat.monthOrDayFormat(nCalIndex).equals(stChoiceMonth.trim())) {
                    stOptions += "<option value='" + csFormat.monthOrDayFormat(nCalIndex) + "' selected=\"selected\" >" + csFormat.monthOrDayFormat(nCalIndex) + "월</option>";
                } else {
                    stOptions += "<option value='" + csFormat.monthOrDayFormat(nCalIndex) + "'>" + csFormat.monthOrDayFormat(nCalIndex) + "월</option>";
                }
        		
        	} else if("Q".equals(stEvalCycle)) {  //평가주기 : 분기
        		
        		nCalIndex *= 3;
        		startMonth = nCalIndex - 2;
        		
        		for(int nTi = startMonth; nTi < startMonth + 3; nTi++) {
        			stChoiceMonth = getSelectMonth(stEvalCycle, startMonth, targetList);
        			
	                if(csFormat.monthOrDayFormat(nTi).equals(stChoiceMonth.trim())) {
	                    stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "' selected=\"selected\" >" + csFormat.monthOrDayFormat(nTi) + "월</option>";
	                } else {
	                    stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "'>" + csFormat.monthOrDayFormat(nTi) + "월</option>";
	                }
	            }
        		
        	} else if("H".equals(stEvalCycle)) {  //평가주기 : 반기
        		
        		nCalIndex *= 6;
        		startMonth = nCalIndex - 5;
        		
        		for(int nTi = startMonth; nTi < startMonth + 6; nTi++) {
        			stChoiceMonth = getSelectMonth(stEvalCycle, startMonth, targetList);
        			
	                if(csFormat.monthOrDayFormat(nTi).equals(stChoiceMonth.trim())) {
	                    stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "' selected=\"selected\" >" + csFormat.monthOrDayFormat(nTi) + "월</option>";
	                } else {
	                    stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "'>" + csFormat.monthOrDayFormat(nTi) + "월</option>";
	                }
	            }
        	} else if("Y".equals(stEvalCycle)) {  //평가주기 : 년
        		nCalIndex *= 12;
        		startMonth = nCalIndex - 11;
        		
        		stChoiceMonth = getSelectMonth(stEvalCycle, startMonth, targetList);
        		
        		for(int nTi = startMonth; nTi < startMonth + 12; nTi++) {
	        		if(csFormat.monthOrDayFormat(nTi).equals(stChoiceMonth.trim())) {
	                    stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "' selected=\"selected\" >" + csFormat.monthOrDayFormat(nTi) + "월</option>";
	                } else {
	                    stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "'>" + csFormat.monthOrDayFormat(nTi) + "월</option>";
	                }
        		}
        	}
            
        } catch( Exception e) {
            e.printStackTrace();
        }

        return stOptions;
    }
	
    /**
	 * 해당 평가 주기에 해당하는 월반환 
	 * @param int
	 * @return
	 */
	public static String getSelectMonth(String stEvalCycle, int nStartMonth, ArrayList targetList)
	{
	    String result = "";
	    String mon = "";
	    int nEndMonth = 0;
	    int nFindMon;

	    if("M".equals(stEvalCycle)) {
	    	nEndMonth = nStartMonth;
	    }else if("Q".equals(stEvalCycle)) {
	    	nEndMonth = nStartMonth + 2;
	    } else if("H".equals(stEvalCycle)) {
	    	nEndMonth = nStartMonth + 5;
	    } else if("Y".equals(stEvalCycle)) {
	    	nEndMonth = nStartMonth + 11;
	    }
	    
	    if(null != targetList && 0 < targetList.size()) {
	    	for(int i = 0; i < targetList.size(); i++) {
	    		HashMap hMap = (HashMap)targetList.get(i);
	    		mon = (String)hMap.get("MON");
	    		nFindMon = Integer.parseInt(mon);
	    		if(nFindMon >= nStartMonth && nFindMon <= nEndMonth) {
	    			result = mon;
	    			break;
	    		}
	    	}
	    }
	    
	    return result;
	}

    /**
	 * 해당 평가 주기에 해당하는 목표(TGT_VALUE) 반환 
	 * @param int
	 * @return
	 */
	public static String getSelectMonTarget(String stEvalCycle, int nCalIndex, ArrayList targetList)
	{
	    String result = "";
	    String mon = "";
	    int nStartMonth = 1;
	    int nEndMonth = 0;
	    int nFindMon;

	    if("M".equals(stEvalCycle)) {
	    	nEndMonth = nCalIndex;
	    	nStartMonth = nCalIndex;
	    }else if("Q".equals(stEvalCycle)) {
	    	nCalIndex *= 3;
	    	nEndMonth = nCalIndex;
    		nStartMonth = nCalIndex - 2;
	    } else if("H".equals(stEvalCycle)) {
	    	nCalIndex *= 6;
	    	nEndMonth = nCalIndex;
    		nStartMonth = nCalIndex - 5;
	    } else if("Y".equals(stEvalCycle)) {
	    	nCalIndex *= 12;
	    	nEndMonth = nCalIndex;
    		nStartMonth = nCalIndex - 11;
	    }
	    
	    if(null != targetList && 0 < targetList.size()) {
	    	for(int i = 0; i < targetList.size(); i++) {
	    		HashMap hMap = (HashMap)targetList.get(i);
	    		mon = (String)hMap.get("MON");
	    		nFindMon = Integer.parseInt(mon);
	    		if(nFindMon >= nStartMonth && nFindMon <= nEndMonth) {
	    			result = (String)hMap.get("TGT_VALUE");
	    			break;
	    		}
	    	}
	    }
	    
	    return result;
	}
    
	/**
	 * 일 option
	 * 선택한 일에 selected
	 * @param int
	 * @return
	 */
	public static String getHtmlCalendarDay( int nChoiceDay )
	{
	    FormatHelper csFormat = new FormatHelper();
	    String stOptions   = "";
	    
	    try
	    {
	        for( int nTi = 1; nTi < 32; nTi++ )
	        {
	            if( csFormat.monthOrDayFormat(nTi).equals(nChoiceDay))
	            {
	                stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "' selected=\"selected\" >" + nTi + "일</option>";
	            }
	            else
	            {
	                stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "'>" + nTi + "일</option>";
	            }
	        }
	    }
	    catch( Exception e )
	    {
	        e.printStackTrace();
	    }
	    
	    return stOptions;
	}
	
	/**
	 * 일 option
	 * 선택한 일에 selected
	 * @param String
	 * @return
	 */
	public static String getHtmlCalendarDay( String stChoiceDay )
	{
	    FormatHelper csFormat = new FormatHelper();
	    String stOptions   = "";
	    
	    try
	    {
	        for( int nTi = 1; nTi < 32; nTi++ )
	        {
	            if( csFormat.monthOrDayFormat(nTi).equals(stChoiceDay.trim()) )
	            {
	                stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "' selected=\"selected\" >" + nTi + "일</option>";
	            }
	            else
	            {
	                stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "'>" + nTi + "일</option>";
	            }
	        }
	    }
	    catch( Exception e )
	    {
	        e.printStackTrace();
	    }
	    
	    return stOptions;
	}
	
	/**
     * 일 option
     * @param 
     * @return
     */
	public static String getHtmlCalendarDay()
	{
	    FormatHelper csFormat = new FormatHelper();
	    String stOptions   = "";
	    
	    try
	    {
	        for( int nTi = 1; nTi < 32; nTi++ )
	        {
	            stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "'>" + nTi + "일</option>";
	        }
	    }
	    catch( Exception e )
	    {
	        e.printStackTrace();
	    }
	    
	    return stOptions;
	}
	
	/**
	 * 시간 option
	 * 선택한 시간에 selected
	 * @param int
	 * @return
	 */
	public static String getHtmlCalendarHour( int nChoiceHour )
    {
	    FormatHelper csFormat = new FormatHelper();
	    String stOptions   = "";
        
        try
        {
            for( int nTi = 0; nTi < 24; nTi++ )   
            {
                if( csFormat.monthOrDayFormat(nTi).equals(nChoiceHour))
                {
                    stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "' selected=\"selected\" >" + nTi + "시</option>";
                }
                else
                {
                    stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "'>" + nTi + "시</option>";
                }
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        
        return stOptions;
    }
	
	/**
	 * 시간 option
	 * 선택한 시간에 selected
	 * @param String
	 * @return
	 */
	public static String getHtmlCalendarHour( String stChoiceHour )
    {
	    FormatHelper csFormat = new FormatHelper();
	    String stOptions   = "";
        
        try
        {
            for( int nTi = 0; nTi < 24; nTi++ )
            {
                if( csFormat.monthOrDayFormat(nTi).equals(stChoiceHour.trim()) )
                {
                    stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "' selected=\"selected\" >" + nTi + "시</option>";
                }
                else
                {
                    stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "'>" + nTi + "시</option>";
                }
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        
        return stOptions;
    }
	
	/**
	 * 시간 option
	 * @param 
	 * @return
	 */
	public static String getHtmlCalendarHour()
	{
	    FormatHelper csFormat = new FormatHelper();
	    String stOptions   = "";
	    
	    try
	    {
	        for( int nTi = 0; nTi < 24; nTi++ )
	        {
	            stOptions += "<option value='" + csFormat.monthOrDayFormat(nTi) + "'>" + nTi + "시</option>";
	        }
	    }
	    catch( Exception e )
	    {
	        e.printStackTrace();
	    }
	    
	    return stOptions;
	}


	/**
     * 두 인자값이 같다면 "checked" 반환
     * @param s DB에서 가져온 값
     * @param word 비교 값
     * @return String 결과값
     */
    public static String getChecked(String s,String word) {    	
    	if(s==null){return "";}    	
        return s.equals(word) ? "checked='checked'" : "";
    }

    /**
     * 두 인자값이 같다면 "selected" 반환
     * @param s DB에서 가져온 값
     * @param word 비교 값
     * @return String 결과값
     */
    public static String getSelected(String s,String word) {
    	if(s==null){return "";} 
        return s.equals(word) ? "selected='selected'" : "";
    }

    /**
     * 두 인자값이 같다면 "checked" 반환
     * @param s DB에서 가져온 값
     * @param w 비교 값
     * @return int 결과값
     */
    public static String getChecked(int s,int w) {
        return (s==w)? "checked='checked'" : "";
    }

    /**
     * 두 인자값이 같다면 "selected" 반환
     * @param s DB에서 가져온 값
     * @param w 비교 값
     * @return int 결과값
     */
    public static String getSelected(int s,int w) {
        return (s==w)? "selected='selected'" : "";
    }
    /**
     * 메세지를 alert으로 보이고 해당 URL로 이동
     * @param rurl 이동 URL
     * @param alertMessage 메세지
     * @return 스크립트
     */
    public static String NoAlertreturnURL(String rurl) {
        String rHtml = new String(" ");

        rHtml  = "<script language=javascript>\n " +
                 "<!-- \n" +
                 "document.location.href='" + rurl + "';  \n" +
                 "// --> \n" +
                 "</script> \n";
        return rHtml;
    }

    /**
     * 메세지를 alert으로 보이고 해당 URL로 이동
     * @param rurl 이동 URL
     * @param alertMessage 메세지
     * @return 스크립트
     */
    public static String returnURL(String rurl, String alertMessage) {
        String rHtml = new String(" ");
        if ( alertMessage == null || alertMessage.equals("") ) return "";
        rHtml  = "<script language=javascript>\n " +
                 "<!-- \n" +
                 "alert('" + alertMessage.trim() + "'); \n" +
                 "document.location.href='" + rurl + "';  \n" +
                 "// --> \n" +
                 "</script> \n";
        return rHtml;
    }

    /**
     * 메세지를 alert으로 보이고 뒤로 돌아가기
     * @param alertMessage 메세지
     * @return 스크립트
     */
    public static String returnHistory(String alertMessage) {
        String rHtml = "";
        rHtml = "<script language=javascript> \n " +
                "<!-- \n" +
                " alert('"+alertMessage.trim()+"'); \n" +
                " history.back(); \n" +
                "// --> \n" +
                "</script> \n ";
        return rHtml;
    }

    /**
     * 메세지를 alert으로 보이고 opener는 reload시키고 창 닫기
     * @param alertMessage 메세지
     * @return 스크립트
     */
    public static String returnClose(String alertMessage) {
        String rHtml = "";
        rHtml = "<script language=javascript> \n " +
                "<!-- \n" +
                " alert('"+alertMessage.trim()+"'); \n" +
                " opener.location.reload(); \n" +
                " window.close(); \n" +
                "// --> \n" +
                "</script> \n ";
        return rHtml;
    }

    /**
     * 메세지를 alert으로 보이고 창 닫기
     * @param alertMessage 메세지
     * @return 스크립트
     */
    public static String returnClose2(String alertMessage) {
        String rHtml = "";
        rHtml = "<script language=javascript> \n " +
                "<!-- \n" +
                " alert('"+alertMessage.trim()+"'); \n" +
                " window.close(); \n" +
                "// --> \n" +
                "</script> \n ";
        return rHtml;
    }

    /**
     * Html 태그 제거시 사용
     * @param html 포함된 String
     * @return html 제거된 String
     */
    public static String removeTag(String str){

    	String sStr	= "";

    	
        if( str == null || str.equals("") ) {
        	sStr	= "";
        }else{
        	sStr	= str.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
        }
        
        //System.out.println("sStr--------------------------------->" + sStr);
        return sStr;
    }
    
    /**
	 * 주어진 문자열을 html에서 표현가능하도록 아래 문자들을 치환한다.
	 * <pre>
	 * & -> &amp;amp;
	 * < -> &amp;lt;
	 * > -> &amp;gt;
	 * " -> &amp;quot;
	 * ' -> &amp;#039;
	 * </pre>
     * <p>
	 * @param  str 대상 문자열.
	 * @return 치환된 문자열.
	 */
	public static String txt2html(String str) {
		if (str == null) {
			return "";
		}

		String[][] map_toHtml = { {"&","&amp;"}, {"<","&lt;"}, {">","&gt;"}, {"\"", "&quot;"}, {"'", "&#039;"}, {" "," "}, {"\\", "&#92;"} };
		
		return replace(str, map_toHtml);
	}
	
	/**
     * null이면 strdef 로 바꾼다.
     * @param str, strdef
     * @return String
     */
	public static String txt2html(String str, String strdef) {
		if (str == null || "".equals(str)) {
			return txt2html(strdef);
        } else {
        	return txt2html(str);
        }
	}
	
	/**
     * 배열로 변환후 인덱스에 해당하는 문자열 반환
     * @param str, strdef
     * @return String
     */
	public static String txt2html(String str, String separator, int index) {
		String result = "";
		
		try{
			if (str == null || "".equals(str)) {
				return "";
	        } else {
	        	result = str.split("\\" + separator)[index];
	        	return result;
	        }
		}catch(Exception e){
			e.printStackTrace();
			return txt2html(""+str);
		}
		
	}
	
	public static String txt2html(int str) {
		return txt2html("" + str);
	}
    
    /**
	 * 문자열에 포맷 적용
	 * @param  str 대상 문자열.
	 * @param  format 포맷구분.
	 * @return 치환된 문자열.
	 */
	public static String txt2html(Object str, int format) {
		FormatHelper formatHelper = new FormatHelper();
		String result = "";
		String temp = "";
		
		if(DefineHelper.FORMAT_1000000 != format){
			if(str == null) return "";
		}
		
		result = formatHelper.format("" + str, format);
		
		if(str != null){
			temp = txt2html("" + result);
			
		}
		
		return result;
	}
    
    public static String txt2html(Object str) {
		if (str == null) {
			return "";
		}
		return txt2html(""+str);
	}
    
    public static String txt2htmlEncode(Object str) {
		if (str == null) {
			return "";
		}
		
		try{
			String returnStr = URLEncoder.encode(txt2html(""+str), "euc-kr");
			
			return returnStr;
		}catch(Exception e){
			e.printStackTrace();
			return txt2html(""+str);
		}
	}
	
	/**
	 * 주어진 문자열을 html에서 표현가능하도록 아래 문자들을 치환한다.
	 * <pre>
	 * & -> &amp;amp;
	 * < -> &amp;lt;
	 * > -> &amp;gt;
	 * " -> &amp;quot;
	 * ' -> &amp;#039;
	 * </pre>
     * <p>
	 * @param  str 대상 문자열.
	 * @return 치환된 문자열.
	 */
	public static String txt2htmlbr(String str) {
		if (str == null) {
			return "";
		}

		String[][] map_toHtml = { {"&","&amp;"}, {"<","&lt;"}, {">","&gt;"}, {"\"", "&quot;"}, {"'", "&#039;"},{"\n","<br/>"} };
		
		return replace(str, map_toHtml);
	}
	
	/**
	 * 주어진 문자열을 html에서 표현가능하도록 아래 문자들을 치환한다.
	 * @param  str 대상 문자열.
	 * @return 치환된 문자열.
	 */
	public static String txt2htmlbr2(String str) {
		if (str == null) {
			return "";
		}

		String[][] map_toHtml = { {"&","&amp;"}, {"<","&lt;"}, {">","&gt;"}, {"\"", "&quot;"}, {"'", "&#039;"},{"\n","<br/>"},{"\r",""} };
		
		return replace(str, map_toHtml);
	}
	
	/**
	 * 주어진 문자열을 html에서 표현가능하도록 아래 문자들을 치환한다.
	 * @param  str 대상 문자열.
	 * @return 치환된 문자열.
	 */
	public static String txt2htmlbr2_forList(String str) {
		if (str == null) {
			return "";
		}

		String[][] map_toHtml = { {"&","&amp;"}, {"<","&lt;"}, {">","&gt;"}, {"\"", "&quot;"}, {"'", "&#039;"},{"\n"," "},{"\r",""} };
		
		return replace(str, map_toHtml);
	}
	
	public static String txt2htmlbr(int str) {
		return txt2htmlbr("" + str);
	}
	
	public static String txt2htmlbr(Object str) {
		if (str == null) {
			return "";
		}
		return txt2htmlbr(""+str);
	}
	
	public static String txt2htmlbrspace(String str) {
		if (str == null) {
			return "";
		}

		String[][] map_toHtml = { {"&","&amp;"}, {"<","&lt;"}, {">","&gt;"}, {"\"", "&quot;"}, {"'", "&#039;"},{"\n","<br/>"},{" ","&nbsp;"} };
		
		return replace(str, map_toHtml);
	}
	
	  /**
	 * html code로 보여지는 문자열을 html code가 적용된 화면으로 표현 가능하도록 아래 문자들을 치환 
	 * <pre>
	 * &amp; -> & 
	 * &lt;  -> < 
	 * &gt;  -> > 
	 * &#039;-> '
	 * &quot;-> /
	 * </pre>
     * <p>
	 * @param  str 대상 문자열.
	 * @return 치환된 문자열.
	 */
	public static String html2txt(String str) {
		if (str == null) {
			return null;
		}

		String[][] map_toHtml = { {"&amp;", "&"}, {"&lt;","<"}, {"&gt;",">"}, {"&quot;","\""}, { "&#039;","'"}};
		
		return replace(str, map_toHtml);
	}
	 /**
     * map을 참조하여 주어진 문자열의 내용을 치환한다.
	 * <p>
	 * 사용예 :
	 * <p><blockquote><pre>
	 * String a = "This is a String";
	 * String[][] map = {{"s","t"},{"i","k"},{"S","o"}};
	 * out.print(StringUtil.replace(a, map));
	 * ===> Thkt kt a otrkng
	 * </pre></blockquote>
	 * <p>
     * @param		str 대상 문자열.
     * @param		map 변환할 내용을 담은 2차원 문자열배열.
     * @return		변환된 문자열.
     */
	public static String replace(String str, String map[][]) {
		return replace(str, map, true);
	}


    /**
     * 주어진 문자열의 내용을 치환한다.
	 * <p>
     * @param		str 대상 문자열.
     * @param		find 찾을 문자열.
     * @param		to 치환할 문자열.
     * @return		변환된 문자열.
     */
	public static String replace(String str, String find, String to) {
		return replace(str, new String[][] {new String[] {find, to}});
	}
	
    /**
     * map을 참조하여 주어진 문자열의 내용을 치환한다. Case에 Insenstive 하다.
	 * <p>
     * @param		str 대상 문자열.
     * @param		map 변환할 내용을 담은 2차원 문자열배열.
     * @param		caseSensitive Case에 Sensitive한지 여부. true 면 Case Senstive이다.
     * @return		변환된 문자열.
     */
	public static String replace(String str, String[][] map, boolean caseSensitive) {
		if (str == null) {
			return null;
		}
		if (map.length <= 0) {
			throw new IllegalArgumentException("mapping array cannot be null");
		}

		String original = str;
		if(!caseSensitive) {
			str = str.toUpperCase();
		}

		StringBuffer sb = new StringBuffer();
		int nextCmpPoint = 0;

		do {
			int matchIndex = -1;
			int fastestMatchPoint = str.length();
			String from;
			for(int i = 0; i < map.length; i++) {
				from = map[i][0];
				if(!caseSensitive) {
					from = from.toUpperCase();
				}
				int matchPoint = str.indexOf(from, nextCmpPoint);
				if(matchPoint > -1 && matchPoint <= fastestMatchPoint) {
					fastestMatchPoint = matchPoint;
					matchIndex = i;
				}
			}

			sb.append(original.substring(nextCmpPoint, fastestMatchPoint));
			if(matchIndex < 0) {
				break;
			}
			from = map[matchIndex][0];
			String to = map[matchIndex][1];
			sb.append(to);
			nextCmpPoint = fastestMatchPoint + from.length();
		} while(nextCmpPoint < str.length());

		return sb.toString();
	}

  /**
   * 등급에 따라 별이미지 추가
   * @param html 포함된 String
   * @return html 제거된 String
   */
  public static String startImages(String str){
    String sStr	= "";
    int cnt = 0;
    if(str.length() > 1){
      cnt = Integer.parseInt(str.substring(0, 1));
      for(int a=0 ; a<cnt ; a++) sStr += "<img src='" + conextPath + "/images/common/icon_star.gif'/>";
      sStr += "<img src='" + conextPath + "/images/common/icon_star2.gif'/>";
    }else if(str.length() == 1){
      cnt = Integer.parseInt(str);
      for(int a=0 ; a<cnt ; a++) sStr += "<img src='" + conextPath + "/images/common/icon_star.gif'/>";
    }
    return sStr;
  }
  
  /**
   * 커뮤니티 메인 랜덤 노출 이미지
   * @param html 포함된 String
   * @return html 제거된 String
   */
  public static String communityRandomImages(){
    String 	returnValue = "";
    String	stNumber	= "";
    int 	nNumber 	= 0;
    
    Random ran = new Random();
    nNumber = ran.nextInt(9) + 1;
    
    if(nNumber < 10) stNumber = "0";
    stNumber += Integer.toString(nNumber);
    
    returnValue = "" + conextPath + "/images/main/photo_community_" + stNumber + ".jpg";
    return returnValue;
  }
  
  /**
   * 파일다운로드용 이미지 생성
   * @param html 포함된 String
   * @return html 제거된 String
   */
  public static String makeFileIcon(String fileExt, String fileName){
    String sStr	= "";
    int cnt = 0;
    if(fileExt == null ||"".equals(fileExt)){
    	sStr = "없음";
    }else{
    	if(fileExt.length() > 1){
    		//sStr = "<img src='" + conextPath + "/images/fileicon/" + fileExt + ".gif' alt='"+ txt2html(fileName) +"'onerror='this.src=\"" + conextPath + "/images/fileicon/default.gif\"'/>";
    		sStr = "<img src='" + conextPath + "/images/fileicon/" + fileExt + ".gif' alt='"+ txt2html(fileName) +"'  style='align:absmiddle'/>";
    	}else{
    		sStr = "";
    	}
    }
    
    return sStr;
  }
  
  /**
   * 파일다운로드용 이미지 생성
   * @param html 포함된 String
   * @return html 제거된 String
   */
  public static String makeFileIcon(String fileExt){
    String sStr	= "";
    int cnt = 0;
    if(fileExt == null ||"".equals(fileExt)){
    	sStr = "없음";
    }else{
    	if(fileExt.length() > 1){
    		//sStr = "<img src='" + conextPath + "/images/fileicon/" + fileExt + ".gif' alt='' onerror='this.src=\"" + conextPath + "/images/fileicon/default.gif\"'/>";
    		sStr = "<img src='" + conextPath + "/images/fileicon/" + fileExt + ".gif' alt=''  style='align:absmiddle'/>";
    	}else{
    		sStr = "";
    	}
    }
    
    return sStr;
  }
  
  /**
   * 파일다운로드용 이미지 생성
   * @param html 포함된 String
   * @return html 제거된 String
   */
  public static String makeFileIcon(String fileExt, String fileName, String filePath){
	  if(fileExt == null || 
			  "".equals(fileExt) || 
			  fileName == null || 
			  "".equals(fileName) || 
			  filePath == null || 
			  "".equals(filePath)){
		  return makeFileIcon("", "");
	  }
    String sStr	= "";
    try {
		sStr = "<a href='" + conextPath + "/commonModule/fileDownload.vw?file_name="+URLEncoder.encode(filePath, "utf-8")+"&amp;real_file_name="+URLEncoder.encode(fileName, "utf-8")+"'>";
	} catch (UnsupportedEncodingException e) {
		sStr = "<a href='" + conextPath + "/commonModule/fileDownload.vw?file_name="+filePath+"&amp;real_file_name="+txt2html(fileName)+"'>";
		e.printStackTrace();
	}
    sStr += makeFileIcon(fileExt, fileName);
    sStr += "</a>";
    return sStr;
  }
  
  /**
   * 파일다운로드용 이미지 생성
   * @param html 포함된 String
   * @return html 제거된 String
   */
  public static String makeFileIconFileName(String fileExt, String fileName, String filePath){
	  if (fileExt == null || 
			  "".equals(fileExt) || 
			  fileName == null || 
			  "".equals(fileName) || 
			  filePath == null || 
			  "".equals(filePath)) {
		  return makeFileIcon("", "");
	  }
	  
	  String sStr = "";
	  
	  try {
		  sStr = "<a href='" + conextPath + "/file/fileDownload.vw?file_name="+URLEncoder.encode(filePath, "utf-8")+"&amp;real_file_name="+URLEncoder.encode(fileName, "utf-8")+"' title = '"+txt2html(fileName)+"'>";
	  } catch (UnsupportedEncodingException e) {
		  sStr = "<a href='" + conextPath + "/file/fileDownload.vw?file_name="+filePath+"&amp;real_file_name="+txt2html(fileName)+"'  title = '"+txt2html(fileName)+"'>";
		  e.printStackTrace();
	  }
	  sStr += makeFileIcon(fileExt, fileName) + "&nbsp;" + StaticUtil.splitHead(txt2html(fileName), 30);
	  sStr += "</a>";
	  return sStr;
  }
  
  /**
   * 파일다운로드용 이미지 없이 생성
   * @param html 포함된 String
   * @return html 제거된 String
   */
  public static String makeFileName(String fileName, String filePath){
	  if(fileName == null || 
			  "".equals(fileName) || 
			  filePath == null || 
			  "".equals(filePath)){
		  return makeFileIcon("", "");
	  }
	  String sStr	= "";
	  try {
		  sStr = "<a href='" + conextPath + "/file/fileDownload.vw?file_name="+URLEncoder.encode(filePath, "utf-8")+"&amp;real_file_name="+URLEncoder.encode(fileName, "utf-8")+"'>";
	  } catch (UnsupportedEncodingException e) {
		  sStr = "<a href='" + conextPath + "/file/fileDownload.vw?file_name="+filePath+"&amp;real_file_name="+txt2html(fileName)+"'>";
		  e.printStackTrace();
	  }
	  sStr += txt2html(fileName);
	  sStr += "</a>";
	  return sStr;
  }
  
  
  /**
   * 파일다운로드용 이미지 없이 생성(래퍼러 채크 않함)
   * @param html 포함된 String
   * @return html 제거된 String
   */
  public static String makeFileNameExcel(String fileName, String filePath){
	  if(fileName == null || 
			  "".equals(fileName) || 
			  filePath == null || 
			  "".equals(filePath)){
		  return makeFileIcon("", "");
	  }
    String sStr	= "";
    
    CommonConfig commonConfig	 = CommonConfig.getInstance();
    String  http_domain_url      = commonConfig.getProperty("HTTP_DOMAIN_URL");
    
    try {
		sStr = "<a href='" + http_domain_url + conextPath + "/file/fileDownloadExcel.vw?file_name="+URLEncoder.encode(filePath, "utf-8")+"&amp;real_file_name="+URLEncoder.encode(fileName, "utf-8")+"'>";
	} catch (UnsupportedEncodingException e) {
		sStr = "<a href='" + http_domain_url + conextPath + "/file/fileDownloadExcel.vw?file_name="+filePath+"&amp;real_file_name="+txt2html(fileName)+"'>";
		e.printStackTrace();
	}
    sStr += txt2html(fileName);
    sStr += "</a>";
    return sStr;
  }
  

	
	/**  
	 * tab 가져오기
	 * @param 
	 */ 
	public String getTab(String colValue, String selectedValue, String url, String param, String Str){
	    String result = "";
	    String url_temp = url + param + selectedValue;

        if(colValue != null && selectedValue.equals(colValue)){
            result = "<li class=\"selectTab\"><span>" + Str + "</span></li>";
        } else {
        	result = "<li class=\"tab\"><a href=\"" + url_temp + "\" onclick=\"goTab('" + selectedValue + "', '" + url + "'); return false;\"><span>" + Str + "</span></a></li>";
        }
        
	    return result;
	}
	
	
	/**  
	 * tab 가져오기(사용여부 useYn, 불가메세지 msg 추가)
	 * @param 
	 */ 
	public String getTab(String colValue, String selectedValue, String url, String Str, String useYn, String msg){
	    String result = "";
	    String temp = "";
	    String flag = "?";
	    
	    for (int i=0; i<url.length(); i++) {
	        if (flag.indexOf(url.charAt(i)) > -1) {
	            flag = "&amp;";
	        }
	    }

        if(colValue != null && selectedValue.equals(colValue)){
            result = "<li class=\"selectTab\"><span>" + Str + "</span></li>";
        } else {
            if("N".equals(useYn)) {
            	result = "<li class=\"tab\"><a href=\"#\" onclick=\"goMsg('" + msg + "'); return false;\"><span>" + Str + "</span></a></li>";
            } else {
            	result = "<li class=\"tab\"><a href=\"" + url + flag + "tabId=" + selectedValue + "\" onclick=\"goTab('" + selectedValue + "', '" + url + "'); return false;\"><span>" + Str + "</span></a></li>";
            }
        }

	    return result;
	}

	
	/**  
	 * 통합검색 검색어와 조회 데이터 매핑
	 * @param str
	 * @param findOldKeyWord
	 * @param mode
	 * @param findOldKeyMode
     * @return 검색어와 조회 데이터 매핑
	 */ 
    public String mappingSearchWord(String str, String findOldKeyWord, String mode, String findOldKeyMode) {
        String result = str;
        String[] arrMode = findOldKeyMode.split("\\|");
		String[] arrWord = findOldKeyWord.split("\\|");		
        
    	if (str != null && findOldKeyWord != null) {
    		if (arrMode != null && arrWord != null) {                
    			for (int i=arrWord.length; i>0; i--) {
    				int j = i-1;
    				if (arrMode[j] != null && arrMode[j].equals(mode)) {
        				String strToken = arrWord[j].trim();
    	                result = result.replaceAll(strToken, "<b>"+strToken+"</b>");
    				}
    			}
    		}
        }        
        
        return result;
    }
    
    /**  
	 * 하위 트리에 노드가 존재하는지 체크
	 * @param codeId
	 * @param tree
     * @return boolean
	 */ 
    public boolean checkSubTreeExist(String codeId, ArrayList list) {
    	boolean isExist = false;
    	
    	if(null != list && 0 < list.size()) {
	    	for(int i = 0; i < list.size(); i++) {
	    		HashMap hmap = (HashMap)list.get(i);
	    		if(codeId.equals(hmap.get("UP_CODE_ID"))) {
	    			isExist = true;
	    			break;
	    		}
	    	}
    	}
    	return isExist;
    }
    
    /**
	 * 산술식을 사용자가 입력한 패턴으로 변경한다
	 * @param tempStr 산술식
	 * @param tempHash 산술식에 대한 값이 들어있는 Map 
	 * @return String 변환된 스트링
	 **/
	public static String changeCalNmToCalDesc(String tempStr, HashMap tempHash) {
		
		String tempValue = "";

		if (tempStr.toUpperCase().indexOf("상수") > 0) {
			tempStr = tempStr.replaceAll("상수", "상");
		}
		
		if(null == tempStr || "".equals(tempStr)) {
			return "";
		}

		char[] calNmChar = tempStr.toCharArray();

		for (int i = 0; i < calNmChar.length; i++) {
			String temp = "";

			if ((tempHash.containsKey(String.valueOf(calNmChar[i])) && tempHash.get(String.valueOf(calNmChar[i])) != null && tempHash.get(String.valueOf(calNmChar[i])) != "") || (String.valueOf(calNmChar[i])).equals("상") && tempHash.containsKey("constant") && tempHash.get("constant") != null && tempHash.get("constant") != "") {
				if (String.valueOf(calNmChar[i]).trim().equals("상")) {
					temp = (String) tempHash.get("constant");
				} else {
					temp = (String) tempHash.get(String.valueOf(calNmChar[i]));
				}
			} else {
				temp = String.valueOf(calNmChar[i]);
			}

			tempValue += temp;
		}

		return tempValue;
	}

	/**
	 * 목표 포멧형식 반환
	 * @param str 값
	 * @param formatType 포멧형식 
	 * @return String 변환된 스트링
	 **/
	public static String txt2htmlTargetFormat(String str) {
		CommonConfig commonConfig	 = CommonConfig.getInstance();
	    String  TARGET_FORMAT        = commonConfig.getProperty("TARGET_FORMAT");
	    String  iTARGET_FORMAT       = commonConfig.getProperty("iTARGET_FORMAT");  
	    
	    int round = Integer.parseInt(iTARGET_FORMAT);
	    
		return FormatHelper.eRateFormat(str, TARGET_FORMAT, round, "");
	}
    
	/**
	 * 실적 포멧형식 반환
	 * @param str 값
	 * @param formatType 포멧형식 
	 * @return String 변환된 스트링
	 **/
	public static String txt2htmlActualFormat(String str) {
		CommonConfig commonConfig	 = CommonConfig.getInstance();
	    String  ACTUAL_FORMAT        = commonConfig.getProperty("ACTUAL_FORMAT");
	    String  iACTUAL_FORMAT       = commonConfig.getProperty("iACTUAL_FORMAT");  
	    
	    int round = Integer.parseInt(iACTUAL_FORMAT);
	    
		return FormatHelper.eRateFormat(str, ACTUAL_FORMAT, round, "");
	}

	/**
	 * 산식 포멧형식 반환
	 * @param str 값
	 * @param formatType 포멧형식 
	 * @return String 변환된 스트링
	 **/
	public static String txt2htmlActcolFormat(String str) {
		CommonConfig commonConfig	 = CommonConfig.getInstance();
	    String  ACTCOL_FORMAT        = commonConfig.getProperty("ACTCOL_FORMAT");
	    String  iACTCOL_FORMAT       = commonConfig.getProperty("iACTCOL_FORMAT");  
	    
	    int round = Integer.parseInt(iACTCOL_FORMAT);
	    
		return FormatHelper.eRateFormat(str, ACTCOL_FORMAT, round, "");
	}
	
	/**
	 * 점수 포멧형식 반환
	 * @param str 값
	 * @param formatType 포멧형식 
	 * @return String 변환된 스트링
	 **/
	public static String txt2htmlScoreFormat(String str) {
		CommonConfig commonConfig	= CommonConfig.getInstance();
	    String  SCORE_FORMAT        = commonConfig.getProperty("SCORE_FORMAT");
	    String  iSCORE_FORMAT       = commonConfig.getProperty("iSCORE_FORMAT");  
	    
	    int round = Integer.parseInt(iSCORE_FORMAT);
	    
		return FormatHelper.eRateFormat(str, SCORE_FORMAT, round, "");
	}
	
	/**
	 * 문자열 합침
	 * @param str1, str2
	 * @param  
	 * @return String 변환된 스트링
	 **/
	public static String concatString(String str1, String str2) {
	    String str = "";
	    
	    if(null != str2 && !"".equals(str2)) {
	    	str = str1 + "(" + str2 + ")";
	    } else {
	    	str = str1;	    	
	    }
	    
		return str;
	}
}
