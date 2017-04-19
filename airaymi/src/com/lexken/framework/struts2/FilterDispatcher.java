package com.lexken.framework.struts2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * com.oreilly.servlet.MultipartRequest을 사용하기위해
 * org.apache.struts2.dispatcher.FilterDispatcher을 상속받아 Filter제거
 */
public class FilterDispatcher extends org.apache.struts2.dispatcher.FilterDispatcher
{
	protected HttpServletRequest prepareDispatcherAndWrapRequest(
			  HttpServletRequest request,
			  HttpServletResponse response) throws ServletException  {
		return request;
	}
}
