package com.lexken.framework.util;

import java.text.*;
import java.util.*;


public class FormulaHelper 
{
	/**
	 * 증감율을 구한다.
	 * @param basicPeriod 	-> 기준연도.
	 * @param previousYear 	-> 이전연도
	 * @return
	 */
	public double getIncreasePercent( double basicPeriod, double previousYear )
	{	
		float fPercent = 0;			
		
		// (기준연도 - 이전연도) / 이전연도 * 100		
		fPercent = (float)((basicPeriod - previousYear) / previousYear) * 100;		
		return fPercent;
	}
	
	/**
	 * 주민번호로 나이구하는 공식
	 * @param idNum - 주민번호 13자리 ('-' 제외)
	 * @return
	 */
	public int getMyAge(String idNum)
	{
		String today 	= ""; //시스템 날짜
		String birthday = ""; //생일
		int myAge 		= 0; //만 나이

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy", Locale.KOREA);
		today = formatter.format(new Date()); //시스템 날짜를 가져와서 yyyy 형태로 변환

		if(idNum.charAt(6) == '1' || idNum.charAt(6) == '2')
		{
			birthday = "19" + idNum.substring(0, 2); //주민번호 7번째 자리수가 1 또는 2이면 1900년대 출생
		}
		else
		{ 
			birthday = "20" + idNum.substring(0, 2); //주민번호 7번째 자리수가 1 또는 2가 아니면 2000년대 출생
		} 

		myAge = Integer.parseInt(today) - Integer.parseInt(birthday) + 1; //현재연도 - 생년 + 1

		return myAge;
	}
	
	/**
	 * 생일로 나이구하기
	 * birthDate Format YYYYMMDD
	 * stDate YYYYMMDD -> 현재 년,월,일
	 */
	public String getBirthDateAge( String birthDate, String stDate )
	{
		String stMyAge = "";
		
		if( birthDate != null && !birthDate.equals("") )
		{
			int nTempBirthDate 	= Integer.parseInt(birthDate.substring(0,4));
			int nTempDate		= Integer.parseInt(stDate.substring(0,4));
			int nAge			= (nTempDate - nTempBirthDate) + 1;
			
			stMyAge				= String.valueOf(nAge);
		}
		
		return stMyAge;
	}
}
