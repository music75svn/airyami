package com.lexken.bsc.module;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.misc.BASE64Decoder;

import java.io.*;

/**
 * Servlet implementation class MatrixPdfDownload
 */
public class MatrixPdfDownload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MatrixPdfDownload() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.pdfDownload(request, response);
	}
	
	/**
	 * Matrix flash pdf download
	 */
	protected void pdfDownload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String data=request.getParameter("data"); 
		String name=request.getParameter("name"); 
		String type=request.getParameter("type"); 
		//String encode=request.getParameter("encode"); 

		OutputStream outs = null;
		try{ 

		     response.reset(); 
		     if(type.equals("excel")){
		    	 response.setContentType("application/vnd.ms-excel;charset="+type); 
		     }else{
		    	 response.setContentType("application/smnet;charset="+type); 
		     }
		     response.setHeader("Content-Disposition","attachment; filename="+name);     
		     response.setHeader("Content-Transfer-Encoding", "binary;"); 
		     response.setHeader("Pragma", "no-cache;"); 
		     response.setHeader("Expires", "-1;"); 

		     BASE64Decoder decode= new BASE64Decoder(); 
		     byte[] bt= decode.decodeBuffer(data);
		     
		     outs = response.getOutputStream(); 
		     outs.write(bt);
		}finally{ 
		     outs.close();
		}
		
	}

}
