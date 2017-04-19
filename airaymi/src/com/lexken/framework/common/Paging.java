package com.lexken.framework.common;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException; 

public class Paging {
	private String pagingQueryHeader;
	private String pagingQueryFooter;
	private int beginRow;
	private int rowPerPage;
	private int pagePerPage;
	private int totalRow = 0;
	private int prevPageRow;
	private int nextPageRow;
	private int blockFirstPage;
	private int blockLastPage;
	private int currentPage;
	private int lastPage;
	private int lastPageRow;
	private String formName;
	private Collection pageNumCol;
	private List voList;
	
	/**
	 * @return the voList
	 */
	public List getVoList() {
		return voList;
	}

	/**
	 * @param voList the voList to set
	 */
	public void setVoList(List voList) {
		this.voList = voList;
	}

	public Paging(int totalRow, int beginRow, int rowPerPage){
		if(beginRow == 0)
			beginRow = 1;
		if(rowPerPage == 0)
			rowPerPage = 10;
		this.pagingQueryHeader = "SELECT * FROM ( "
    		+ "SELECT A.*, ROWNUM SROWNUM FROM (";
		this.pagingQueryFooter = ") A "
    		+ ")WHERE SROWNUM >= "+ beginRow +" AND ROWNUM <= "+ rowPerPage;
		this.beginRow = beginRow;
		this.rowPerPage = rowPerPage;
		this.totalRow = totalRow;
		this.pagePerPage = 10;
		init();
	}
	
	private void init() {
		
		this.currentPage = (int) Math.ceil((double) beginRow / (double) rowPerPage);
		this.lastPage = (int) Math.ceil((double) getTotalRow() / (double) rowPerPage);
		
		if (lastPage < currentPage)
			this.currentPage = lastPage;
		this.blockFirstPage = currentPage - (int) Math.floor(pagePerPage / 2); 
		if (blockFirstPage < 1)
			this.blockFirstPage = 1;
		this.blockLastPage = blockFirstPage + pagePerPage - 1; 
		if (blockLastPage > lastPage)
			this.blockLastPage = lastPage;
		if (blockFirstPage > blockLastPage - pagePerPage + 1) 
			this.blockFirstPage = blockLastPage - pagePerPage + 1;
		if (blockFirstPage < 1)
			this.blockFirstPage = 1;

		if (currentPage > 1)
			this.prevPageRow = (currentPage - 2) * rowPerPage + 1;
		else
			this.prevPageRow = 0;
		if (currentPage < lastPage)
			this.nextPageRow = (currentPage) * rowPerPage + 1;
		else
			this.nextPageRow = 0;
		this.lastPageRow = (lastPage - 1) * rowPerPage + 1;
		
		pageNumCol = new ArrayList();
		for (int pageNum = blockFirstPage; blockLastPage >= pageNum; pageNum++) {
			this.pageNumCol.add(new Integer(pageNum));
		}
	}

	
	/**
	 * @return the formName
	 */
	public String getFormName() {
		return formName;
	}

	/**
	 * @param formName the formName to set
	 */
	public void setFormName(String formName) {
		this.formName = formName;
	}


	/**
	 * @return the pageNumCol
	 */
	public Collection getPageNumCol() {
		return pageNumCol;
	}

	/**
	 * @param pageNumCol the pageNumCol to set
	 */
	public void setPageNumCol(Collection pageNumCol) {
		this.pageNumCol = pageNumCol;
	}

	/**
	 * @return the pagePerPage
	 */
	public int getPagePerPage() {
		return pagePerPage;
	}

	/**
	 * @param pagePerPage the pagePerPage to set
	 */
	public void setPagePerPage(int pagePerPage) {
		this.pagePerPage = pagePerPage;
	}

	/**
	 * @return the beginRow
	 */
	public int getBeginRow() {
		return beginRow;
	}

	/**
	 * @return the blockFirstPage
	 */
	public int getBlockFirstPage() {
		return blockFirstPage;
	}

	/**
	 * @return the blockLastPage
	 */
	public int getBlockLastPage() {
		return blockLastPage;
	}

	/**
	 * @return the currentPage
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * @return the lastPage
	 */
	public int getLastPage() {
		return lastPage;
	}

	/**
	 * @return the lastPageRow
	 */
	public int getLastPageRow() {
		return lastPageRow;
	}

	/**
	 * @return the nextPageRow
	 */
	public int getNextPageRow() {
		return nextPageRow;
	}

	/**
	 * @return the prevPageRow
	 */
	public int getPrevPageRow() {
		return prevPageRow;
	}

	/**
	 * @return the rowPerPage
	 */
	public int getRowPerPage() {
		return rowPerPage;
	}

	/**
	 * @return the totalRow
	 */
	public int getTotalRow() {
		return totalRow;
	}

	/**
	 * @param beginRow the beginRow to set
	 */
	public void setBeginRow(int beginRow) {
		this.beginRow = beginRow;
	}

	/**
	 * @param blockFirstPage the blockFirstPage to set
	 */
	public void setBlockFirstPage(int blockFirstPage) {
		this.blockFirstPage = blockFirstPage;
	}

	/**
	 * @param blockLastPage the blockLastPage to set
	 */
	public void setBlockLastPage(int blockLastPage) {
		this.blockLastPage = blockLastPage;
	}

	/**
	 * @param currentPage the currentPage to set
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * @param lastPage the lastPage to set
	 */
	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	/**
	 * @param lastPageRow the lastPageRow to set
	 */
	public void setLastPageRow(int lastPageRow) {
		this.lastPageRow = lastPageRow;
	}

	/**
	 * @param nextPageRow the nextPageRow to set
	 */
	public void setNextPageRow(int nextPageRow) {
		this.nextPageRow = nextPageRow;
	}


	/**
	 * @param prevPageRow the prevPageRow to set
	 */
	public void setPrevPageRow(int prevPageRow) {
		this.prevPageRow = prevPageRow;
	}

	/**
	 * @param rowPerPage the rowPerPage to set
	 */
	public void setRowPerPage(int rowPerPage) {
		this.rowPerPage = rowPerPage;
	}

	/**
	 * @param totalRow the totalRow to set
	 */
	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}

	/**
	 * @return the pagingFooter
	 */
	public String getPagingQueryFooter() {
    	return pagingQueryFooter;
	}
	
	/**
	 * @param pagingQueryFooter the pagingQueryFooter to set
	 */
	public void setPagingQueryFooter(String pagingQueryFooter) {
		this.pagingQueryFooter = pagingQueryFooter;
	}

	/**
	 * @return the pagingQueryHeader
	 */
	public String getPagingQueryHeader() {
		return pagingQueryHeader;
	}

	/**
	 * @param pagingHeader the pagingHeader to set
	 */
	public void setPagingQueryHeader(String pagingQueryHeader) {
		this.pagingQueryHeader = pagingQueryHeader;
	}

	public String getPageHtml(String formName, String pagingTemplate) throws Exception {
		setFormName(formName);
		pageNumCol = new ArrayList() ;
		for (int pageNum=blockFirstPage ; blockLastPage >= pageNum ; pageNum ++ ) {
			pageNumCol.add(new Integer(pageNum)) ;
		}
		
		StringWriter w = new StringWriter() ;
		
		try {
			
			VelocityEngine ve = new VelocityEngine() ;
			Properties p = new Properties() ;
			p.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, System.getProperty("webapp.root")) ;
			p.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_CACHE, "true");
			
			ve.init(p);
			
			Template t = ve.getTemplate(pagingTemplate);
			
	        VelocityContext context = new VelocityContext() ;
			context.put("paging", this) ;
			
			/* now render the template into a StringWriter */
	        t.merge( context,w );
	        
	    } catch (ResourceNotFoundException e) {
			throw new Exception(pagingTemplate+" 페이징 템플릿 파일이 없습니다") ;
		} catch (ParseErrorException e) {
			throw new Exception(pagingTemplate+" 페이징 템플릿 파일 Parsing Error") ;
		} catch (Exception e) {
			throw e ;
		}
		return w.toString() ;
	}
	public String getPageHtml(String formName) throws Exception {
		return getPageHtml(formName, "/WEB-INF/vl/common/template/pagingTemplate.vm") ;
	}
	public String getPageHtml() throws Exception {
		return getPageHtml("frm0", "/WEB-INF/vl/common/template/pagingTemplate.vm") ;
	}	
}
