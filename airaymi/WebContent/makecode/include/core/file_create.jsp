<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import = "com.lexken.framework.common.*"%>
<%@ page import = "com.lexken.framework.util.*"%>
<%@ page import = "java.io.*"%>

<%@ include file="./db_connect.jsp"%>

<%
	request.setCharacterEncoding("utf-8");
	ServletContext servletContext = request.getSession().getServletContext();

	int count = 0;
	String[] templatefiles = new String[6];

	templatefiles[0] = "/CLASSNAME-sqlmap.xml";
	templatefiles[1] = "/CLASSNAMEAction.java";
	templatefiles[2] = "/CLASSSMALLNAMEList_xml.vm";
	templatefiles[3] = "/CLASSSMALLNAMEList.vm";
	templatefiles[4] = "/CLASSSMALLNAMEModify.vm";
	templatefiles[5] = "/CLASSSMALLNAMEProcess.vm";
	
	String SQL = request.getParameter("sql");                               //SQL문
	String[] columnComments = request.getParameterValues("columnComments"); //컬럼 코멘트
	String[] columnNames = request.getParameterValues("columnNames");       //컬럼명
	String real_path = request.getParameter("real_path");                   //파일생성경로
	String autoDirectoryCopy = request.getParameter("autoDirectoryCopy");   //폴더 자동생성 여부
	String SYSNAME = request.getParameter("SYSNAME");
	String CLASSNAME = request.getParameter("CLASSNAME");
	String CLASSSMALLNAME = "";
	String USERNAME = request.getParameter("USERNAME");
	String TODAY = "";
	String DESC = request.getParameter("DESC");
	String PKGNAME = request.getParameter("PKGNAME");
	String PKGPATH = "";
	
	//--dml 생성---------------------------------------
	String wTableName = request.getParameter("findTableNm");                 //table명
	String wOverWrite = request.getParameter("overWrite");                   //file over Write
	String[] wColNames = request.getParameterValues("colNames");             //컬럼명
	String[] wComments = request.getParameterValues("comments");             //코멘트
	String[] wColPks = request.getParameterValues("colPks");                 //pk여부
	String[] wTypes = request.getParameterValues("types");                   //데이터타입
	String[] wInputColType = request.getParameterValues("inputColType");     //입력속성
	String[] wNotNullType = request.getParameterValues("notNullType");       //필수입력 여부
	String[] wOnlyNumberType = request.getParameterValues("onlyNumberType"); //숫자만 입력
	String[] wColLength = request.getParameterValues("colLength");           //column length
	String[] wColNamesCarmel = null;
	
	wColNamesCarmel = replaseCamelArr(wColNames);

	String MODIFYCOLNAMES = "";
	count = 0;
	if(wColPks != null) {
		for(int i = 0; i < wColPks.length; i++) {
			if(!"YEAR".equals(wColNames[i])) {
				if("Y".equals(wColPks[i])) {
					if(count != 0) MODIFYCOLNAMES += "\n        ";
					MODIFYCOLNAMES += "J(\"#" + wColNamesCarmel[i] + "\").val(ret." + wColNames[i] + ");";
					count++;
				}
			}
		}
	}
	
	String DELETECOLNAMES1 = "";
	String DELETECOLNAMES2 = "";
	String DELETECOLNAMES3 = "";
	
	count = 0;
	if(wColNamesCarmel != null) {
		for(int i = 0; i < wColNamesCarmel.length; i++) {
			if(!"YEAR".equals(wColNames[i])) {
				if("Y".equals(wColPks[i])) {
					if(count != 0) DELETECOLNAMES1 += "\n        ";
					DELETECOLNAMES1 += "var " + wColNamesCarmel[i] + "s = \"\";";
					DELETECOLNAMES2 += wColNamesCarmel[i] + "s += rowdata." + wColNames[i] + " + '|';";
					DELETECOLNAMES3 += "J(\"#" + wColNamesCarmel[i] + "s\").val(" + wColNamesCarmel[i] + "s);";
					count++;
				}
			}
		}
	}
	
	String KEYCOLNAMES = "";
	String KEYMAINCOLNAMESACTION = "";
	String KEYMAINCOLNAME = "";
	String KEYCOLNAMESACTION1 = "";
	String KEYCOLNAMESACTION2 = "";
	
	count = 0;
	if(wColNamesCarmel != null) {
		for(int i = 0; i < wColNamesCarmel.length; i++) {
			if(!"YEAR".equals(wColNames[i])) {
				if("Y".equals(wColPks[i])) {
					if(count != 0) {
						KEYCOLNAMESACTION1 += "\n            ";
						KEYCOLNAMESACTION2 += "\n            ";
					}
					
					if(count == 0) {
						KEYMAINCOLNAMESACTION = wColNamesCarmel[i] + "s";
						KEYMAINCOLNAME = wColNames[i];
					}
					
					KEYCOLNAMES += "<input type=\"hidden\" id=\"" + wColNamesCarmel[i] +"s\" name=\"" + wColNamesCarmel[i] +"s\" /> \n            ";
					KEYCOLNAMES += "<input type=\"hidden\" id=\"" + wColNamesCarmel[i] +"\"  name=\"" + wColNamesCarmel[i] +"\" />";
					
					KEYCOLNAMESACTION1 += "String[] " + wColNamesCarmel[i] + "s = searchMap.getString(\"" + wColNamesCarmel[i] +"s\").split(\"\\\\\\\\|\", 0);";
					KEYCOLNAMESACTION2 += "searchMap.put(\"" + wColNamesCarmel[i] + "\", " + wColNamesCarmel[i] + "s[i]);";
					count++;
				}
			}
		}
	}

	//상세 sql
	String DETAILSQL = "";
	DETAILSQL += "        SELECT ";

	count = 0;
	if(wColNamesCarmel != null) {
		for(int i = 0; i < wColNamesCarmel.length; i++) {
			if("CREATE_DT".equals(wColNames[i]) || "MODIFY_DT".equals(wColNames[i]) || "DELETE_DT".equals(wColNames[i])) {
			} else {
				//if(!"Y".equals(wColPks[i])) {
					DETAILSQL += "";
					if(count != 0) DETAILSQL += "             , ";
					DETAILSQL += getStrFillBlank(wColNames[i]) + "\n";
					count++;
				//}
			}
		}
		
		DETAILSQL += "          FROM " + wTableName + " \n";
		DETAILSQL += "        WHERE 1 = 1 ";
		
		for(int i = 0; i < wColNamesCarmel.length; i++) {
			if("CREATE_DT".equals(wColNames[i]) || "MODIFY_DT".equals(wColNames[i]) || "DELETE_DT".equals(wColNames[i])) {
			} else {
				if("Y".equals(wColPks[i])) {
					DETAILSQL += "";
					if(count != 0) DETAILSQL += "\n           AND ";
					DETAILSQL += wColNames[i] + " = #" + wColNamesCarmel[i] + "#";
					count++;
				}
			}
		}
	}
	
	//INSERT sql
	String INSERTSQL = "";
	boolean isCreateDt = false;
	
	if(wColNames != null) {
		for(int i = 0; i < wColNames.length; i++) {
			if("CREATE_DT".equals(wColNames[i])) {
				isCreateDt = true;
			}
		}
	}
	
	INSERTSQL += "        INSERT INTO " + wTableName + " ( \n";
	
	if(wColNamesCarmel != null) {
		count = 0;
		for(int i = 0; i < wColNamesCarmel.length; i++) {
			if("MODIFY_DT".equals(wColNames[i]) || "DELETE_DT".equals(wColNames[i])) {
			} else if(!"".equals(nullChk(wInputColType[i]))) {
				INSERTSQL += "             ";
				if(count != 0) INSERTSQL += ", "; else INSERTSQL += "  ";  
				INSERTSQL += wColNames[i] + "\n";
			}
			count++;
		}
		
		INSERTSQL += "             ) VALUES ( \n";
		
		count = 0;
		
		for(int i = 0; i < wColNamesCarmel.length; i++) {
			if("MODIFY_DT".equals(wColNames[i]) || "DELETE_DT".equals(wColNames[i])) {
			} else if("CREATE_DT".equals(wColNames[i])) {
				INSERTSQL += "             , SYSDATE\n";
			} else if(KEYMAINCOLNAME.equals(wColNames[i])) {
				INSERTSQL += "             , #SEQ#\n";
			} else if(!"".equals(nullChk(wInputColType[i]))) {
				INSERTSQL += "             ";
				if(count != 0) INSERTSQL += ", ";  else INSERTSQL += "  ";
				INSERTSQL += "#" + wColNamesCarmel[i] + "#\n";
				
			}
			count++;
		}
		
		INSERTSQL += "             )";
	}
	
	//UPDATE sql
	String UPDATESQL = "";
	
	if(wColNames != null) {
		for(int i = 0; i < wColNames.length; i++) {
			if("CREATE_DT".equals(wColNames[i])) {
				isCreateDt = true;
			}
		}
	}
	
	UPDATESQL += "        UPDATE " + wTableName + " \n";
	UPDATESQL += "           SET ";
	
	count = 0;
	if(wColNamesCarmel != null) {
		for(int i = 0; i < wColNamesCarmel.length; i++) {
			if("CREATE_DT".equals(wColNames[i]) || "DELETE_DT".equals(wColNames[i])) {
			} else if("MODIFY_DT".equals(wColNames[i])) {
				UPDATESQL += "             , " + getStrFillBlank(wColNames[i]) + " = SYSDATE \n";
			} else if(!"".equals(nullChk(wInputColType[i]))) {
				if(!"Y".equals(wColPks[i])) {
					UPDATESQL += "";
					if(count != 0) UPDATESQL += "             , ";
					UPDATESQL += getStrFillBlank(wColNames[i]) + " = #" + wColNamesCarmel[i] + "# \n";
					count++;
				}
			}
		}
		
		UPDATESQL += "         WHERE 1 = 1 \n";
		
		count = 0;
		for(int i = 0; i < wColNamesCarmel.length; i++) {
			if("Y".equals(wColPks[i])) {
				if(count != 0) UPDATESQL += "\n";
				UPDATESQL += "           AND " + wColNames[i] + " = #" + wColNamesCarmel[i] + "#";
				count++;
			}
		}
	}
	
	//DELETE sql
	String DELETESQL = "";
	boolean isDeleteDt = false;
	
	if(wColNames != null) {
		for(int i = 0; i < wColNames.length; i++) {
			if("DELETE_DT".equals(wColNames[i])) {
				isDeleteDt = true;
			}
		}
	}
	
	if(isDeleteDt) {
		DELETESQL += "        UPDATE " + wTableName + " \n";
		DELETESQL += "           SET DELETE_DT       = SYSDATE \n";
		DELETESQL += "         WHERE 1 = 1 \n";
	} else {
		DELETESQL += "        DELETE FROM " + wTableName + " \n";
		DELETESQL += "         WHERE 1 = 1 \n";
	}
	
	count = 0;
	if(wColNamesCarmel != null) {
		for(int i = 0; i < wColNamesCarmel.length; i++) {
			if("Y".equals(wColPks[i])) {
				if(count != 0) DELETESQL += "\n";
				DELETESQL += "           AND " + wColNames[i] + " = #" + wColNamesCarmel[i] + "#";
				count++;
			}
		}
	}
	
	//수정페이지
	String MODIFYCOLUMN = "";
	String MODIFYKEYCOLUMN = "";
	String notNullStr = "";
	
	if(wColNamesCarmel != null) {
		for(int i = 0; i < wColNamesCarmel.length; i++) {
			if(!"Y".equals(wColPks[i])) {
				if("Y".equals(wNotNullType[i])) {
					notNullStr = "<span class=\"txt_red\">(*)</span> "; 
				} else {
					notNullStr = "";
				}
				
				if("CREATE_DT".equals(wColNames[i]) || "DELETE_DT".equals(wColNames[i]) || "MODIFY_DT".equals(wColNames[i])) {
				} else if("InputBox".equals(nullChk(wInputColType[i]))) {
					int maxLength = Integer.parseInt(wColLength[i])/3;
					
					if("NUMBER".equals(wTypes[i])) {
						maxLength = Integer.parseInt(wColLength[i]);
					}
					
					MODIFYCOLUMN += "                        <tr>\n";
					MODIFYCOLUMN += "                            <th scope=\"row\">" + notNullStr + "<label for=\"" + wColNamesCarmel[i] + "\">" + wComments[i] + "</label></th>\n";
					MODIFYCOLUMN += "                            <td><input type=\"text\" id=\"" + wColNamesCarmel[i] + "\" name=\"" + wColNamesCarmel[i] + "\" class=\"inputbox w200\" value='\\$!{CS_HTML.txt2html(\\$detail." + wColNames[i] + ")}' maxlength=\"" + maxLength + "\"/></td>\n";
					MODIFYCOLUMN += "                        </tr>\n";
				} else if("SelectBox".equals(nullChk(wInputColType[i]))) {
					MODIFYCOLUMN += "                        <tr>\n";
					MODIFYCOLUMN += "                            <th scope=\"row\">" + notNullStr + "<label for=\"" + wColNamesCarmel[i] + "\">" + wComments[i] + "</label></th>\n";
					MODIFYCOLUMN += "                            <td>\n";
					MODIFYCOLUMN += "                                <select id=\"" + wColNamesCarmel[i] + "\" name=\"" + wColNamesCarmel[i] + "\" class=\"select w100\" >\n";
					MODIFYCOLUMN += "                                    \\$!CS_CODE.getCodeOption(\"014\", \\$detail." + wColNames[i] + ", \\$!{searchMap.txt2html(\"year\")})\n";
					MODIFYCOLUMN += "                                </select>\n";
					MODIFYCOLUMN += "                            </td>\n";
					MODIFYCOLUMN += "                        </tr>\n";
				} else if("TextArea".equals(nullChk(wInputColType[i]))) {
					MODIFYCOLUMN += "                        <tr>\n";
					MODIFYCOLUMN += "                            <th scope=\"row\">" + notNullStr + "<label for=\"" + wColNamesCarmel[i] + "\">" + wComments[i] + "</label></th>\n";
					MODIFYCOLUMN += "                            <td>\n";
					MODIFYCOLUMN += "                                <textarea id=\"" + wColNamesCarmel[i] + "\" name=\"" + wColNamesCarmel[i] + "\" class=\"tabletext\" title=\"" + wComments[i] + "\" cols=\"\" rows=\"10\">\\$!{CS_HTML.txt2html(\\$detail." + wColNames[i] + ")}</textarea>\n";
					MODIFYCOLUMN += "                                <span id=\"" + wColNamesCarmel[i] + "Indicator\">0</span>/" + wColLength[i] + "byte\n";
					MODIFYCOLUMN += "                            </td>\n";
					MODIFYCOLUMN += "                        </tr>\n";
				}
			}
		}
		
		for(int i = 0; i < wColNamesCarmel.length; i++) {
			if("Y".equals(wColPks[i])) {
				MODIFYKEYCOLUMN += "    <input type=\"hidden\"	id=\"" + getStrFillBlank(wColNamesCarmel[i]+"\"", 15) + " name=\"" + getStrFillBlank(wColNamesCarmel[i]+"\"", 15) + " value=\"\\$!{searchMap.txt2html(\"" + wColNamesCarmel[i] + "\")}\" />\n";
			}
		}
	}
	
	//필수입력(vm)
	String NOTCOLUMN = "";
	 
	if(wColNamesCarmel != null) {
		for(int i = 0; i < wColNamesCarmel.length; i++) {
			if("Y".equals(wNotNullType[i])) {
				if("InputBox".equals(nullChk(wInputColType[i])) || "TextArea".equals(nullChk(wInputColType[i]))) {
					int maxLength = Integer.parseInt(wColLength[i])/3;
					
					if("NUMBER".equals(wTypes[i])) {
						maxLength = Integer.parseInt(wColLength[i]);
					}
					
					NOTCOLUMN += "        if(!J.checkLength(\"" + wColNamesCarmel[i] + "\", \"" + wComments[i] + "\", 1, " + maxLength + ")) {\n";
					NOTCOLUMN += "            return;\n";
					NOTCOLUMN += "        }\n\n";
				} else if("SelectBox".equals(nullChk(wInputColType[i]))) {
					NOTCOLUMN += "        if(J(\"#" + wColNamesCarmel[i] + "\").val() == \"\") {\n";
					NOTCOLUMN += "            J.showMsgBox(\"" + wComments[i] + "를 선택해 주십시요\", null, \"" + wColNamesCarmel[i] + "\");\n";
					NOTCOLUMN += "            return;\n";
					NOTCOLUMN += "        }\n\n";
				} 
			}
		}
	}

	//Validation (action)
	String NOTCOLUMNACTION = "";
	 
	if(wColNamesCarmel != null) {
		for(int i = 0; i < wColNamesCarmel.length; i++) {
			if(!"Y".equals(wColPks[i])) {
				if("CREATE_DT".equals(wColNames[i]) || "DELETE_DT".equals(wColNames[i]) || "MODIFY_DT".equals(wColNames[i])) {
				} else {
					if("InputBox".equals(nullChk(wInputColType[i])) || "TextArea".equals(nullChk(wInputColType[i]))) {
						int maxLength = Integer.parseInt(wColLength[i])/3;
						
						if("NUMBER".equals(wTypes[i])) {
							maxLength = Integer.parseInt(wColLength[i]);
						}
						
						if(!"NUMBER".equals(wTypes[i])) {
							if("Y".equals(wNotNullType[i])) {
								NOTCOLUMNACTION += "        returnMap = ValidationChk.lengthCheck(searchMap.getString(\"" + wColNamesCarmel[i] + "\"), \"" + wComments[i] + "\", 1, " + maxLength + ");\n";
								NOTCOLUMNACTION += "        if((Integer)returnMap.get(\"ErrorNumber\") < 0) {\n";
								NOTCOLUMNACTION += "            return returnMap;\n";
								NOTCOLUMNACTION += "        }\n\n";
							}
						} else {
							NOTCOLUMNACTION += "        returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString(\"" + wColNamesCarmel[i] + "\"), \"" + wComments[i] + "\");\n";
							NOTCOLUMNACTION += "        if((Integer)returnMap.get(\"ErrorNumber\") < 0) {\n";
							NOTCOLUMNACTION += "            return returnMap;\n";
							NOTCOLUMNACTION += "        }\n\n";
						}
						
					} else if("SelectBox".equals(nullChk(wInputColType[i]))) {
						if("Y".equals(wNotNullType[i])) {
							NOTCOLUMNACTION += "        returnMap = ValidationChk.selEmptyCheck(searchMap.getString(\"" + wColNamesCarmel[i] + "\"), \"" + wComments[i] + "\");\n";
							NOTCOLUMNACTION += "        if((Integer)returnMap.get(\"ErrorNumber\") < 0) {\n";
							NOTCOLUMNACTION += "            return returnMap;\n";
							NOTCOLUMNACTION += "        }\n\n";
						}
					} 
				}
			}
		}
	}
	
	
	//Number형 숫자만 입력
	String ONLYINPUTNUMBER = "";
	count = 0;
	if(wColNamesCarmel != null) {
		for(int i = 0; i < wColNamesCarmel.length; i++) {
			if(!"Y".equals(wColPks[i])) {
				if("CREATE_DT".equals(wColNames[i]) || "DELETE_DT".equals(wColNames[i]) || "MODIFY_DT".equals(wColNames[i])) {
				} else if("InputBox".equals(nullChk(wInputColType[i]))) {
					if("NUMBER".equals(wTypes[i])) {
						if(count == 0) {
							ONLYINPUTNUMBER += "        /******************************************\n";
							ONLYINPUTNUMBER += "         * 정렬순서 숫자만 입력\n";
							ONLYINPUTNUMBER += "         ******************************************/\n";
						}
						
						ONLYINPUTNUMBER += "        J(\"#" + wColNamesCarmel[i] + "\").numeric();\n";
						ONLYINPUTNUMBER += "        J(\"#" + wColNamesCarmel[i] + "\").css(\"ime-mode\", \"disabled\");\n";
						count++;
					}
				}
			}
		}
	}

	//비고 byte 체크
	String TEXTAREABYTECHK = "";
	count = 0;
	if(wColNamesCarmel != null) {
		for(int i = 0; i < wColNamesCarmel.length; i++) {
			if(!"Y".equals(wColPks[i])) {
				if("CREATE_DT".equals(wColNames[i]) || "DELETE_DT".equals(wColNames[i]) || "MODIFY_DT".equals(wColNames[i])) {
				} else if("TextArea".equals(nullChk(wInputColType[i]))) {
					if(count == 0) {
						TEXTAREABYTECHK += "        /******************************************\n";
						TEXTAREABYTECHK += "         * 비고 byte 체크\n";
						TEXTAREABYTECHK += "         ******************************************/\n";
					}
					
					TEXTAREABYTECHK += "        J(\"#" + wColNamesCarmel[i] + "\").checkbyte({\n";
					TEXTAREABYTECHK += "            indicator:J(\"#" + wColNamesCarmel[i] + "Indicator\"),\n";
					TEXTAREABYTECHK += "            limit:" + wColLength[i] + "\n";
					TEXTAREABYTECHK += "        });\n";
					TEXTAREABYTECHK += "        J(\"#" + wColNamesCarmel[i] + "\").trigger(\"keyup\");\n";
					count++;
				}
			}
		}
	}
	 
	//jqGrid 설정
	String COLNAMES = "";
	if(0 < columnComments.length) {
		for(int i = 0; i < columnComments.length; i++) {
			COLNAMES += columnComments[i];
			if(i != columnNames.length - 1) COLNAMES += "', '";
		}
	}
	
	String COLMODEL = "";  
	if(0 < columnNames.length) {
		for(int i = 0; i < columnNames.length; i++) {
			if(i == 2)
				COLMODEL += "{name:" + getStrFillBlank("'" + columnNames[i] + "'")+ " ,index:" + getStrFillBlank("'" + columnNames[i] + "'") + " ,width:150   ,align:'center' ,formatter:linkUpdate}";
			else 
				COLMODEL += "{name:" + getStrFillBlank("'" + columnNames[i] + "'")+ " ,index:" + getStrFillBlank("'" + columnNames[i] + "'") + " ,width:150   ,align:'center' }";
			
			if(i != columnNames.length - 1) COLMODEL += ", \n                            ";
		}
	}
	
	//엑셀설정
	String COLEXCEL = "";  
	if(0 < columnNames.length) {
		for(int i = 0; i < columnNames.length; i++) {
			COLEXCEL += "excelInfoList.add(new ExcelVO(\"" + columnComments[i] + "\", \"" + columnNames[i] + "\", \"left\"));\n    	";
		}
	}

	String COLMODELJSON = ""; 
	if(0 < columnNames.length) {
		for(int i = 0; i < columnNames.length; i++) {
			COLMODELJSON += getStrFillBlank("\"" + columnNames[i] + "\"") + " : \"\\$!{CS_HTML.txt2html(\\$list." + columnNames[i] + ")}\"";
			if(i != columnNames.length - 1) COLMODELJSON += ", \n        ";
		}
	}
	
	boolean isOK = true;
	boolean isAlreadyExist = false;
	
	if(CLASSNAME == null || "".equals(CLASSNAME)) isOK = false;
	if(USERNAME == null || "".equals(USERNAME)) isOK = false;
	if(DESC == null || "".equals(DESC)) isOK = false;
	
	if(isOK){
		isOK = false;
		CLASSNAME = CLASSNAME.substring(0,1).toUpperCase() + CLASSNAME.substring(1);
		CLASSSMALLNAME = CLASSNAME.substring(0,1).toLowerCase() + CLASSNAME.substring(1);
		CalendarHelper calendarHelper = new CalendarHelper();
		TODAY = calendarHelper.getFullDayForm(calendarHelper.getStrDate());
		PKGPATH = PKGNAME.replaceAll("bsc.", "").replaceAll("\\.", "/");
		String[] temp = PKGPATH.split("\\/");
		
		String targetJavaFile = real_path + "/src/com/lexken/" + SYSNAME + "/" + PKGNAME;
		String targetSqlXmlFile = real_path + "/src/com/lexken/" + SYSNAME + "/" + PKGNAME + "/sql";
		String targetJspFile = real_path + "/WebContent/WEB-INF/vl/" + SYSNAME + "/" + PKGNAME + "/" + CLASSSMALLNAME;
		String targetSqlMapFile = real_path + "/src/sqlmap-config.xml";
		
		if("N".equals(autoDirectoryCopy)) { 
			targetJavaFile = real_path + "/WebContent/makecode/output";
			targetSqlXmlFile = real_path + "/WebContent/makecode/output";
			targetJspFile = real_path + "/WebContent/makecode/output";
		}
		
		//해당폴더에 기존 파일이 존재하는지 체크
		for(int i = 0; i < templatefiles.length; i++){
			File targetFile = null;
		   	
		   	if(i == 0) {
		   		targetFile = new File(targetSqlXmlFile + templatefiles[i].replaceAll("CLASSSMALLNAME",CLASSSMALLNAME).replaceAll("CLASSNAME",CLASSNAME));
		   	} else if(i == 1) {
		   		targetFile = new File(targetJavaFile + templatefiles[i].replaceAll("CLASSSMALLNAME",CLASSSMALLNAME).replaceAll("CLASSNAME",CLASSNAME));
		   	} else {
		   		targetFile = new File(targetJspFile + templatefiles[i].replaceAll("CLASSSMALLNAME",CLASSSMALLNAME).replaceAll("CLASSNAME",CLASSNAME));
		   	}
		   	
		   	if(targetFile.isFile()) {
		   		if("true".equals(wOverWrite)) {
		   			isAlreadyExist = false; //파일 덮어쓰기
		   		} else {
		   			isAlreadyExist = true;
		   		}
		   	} 
		}
		
		isOK = true;

		
		if(!isAlreadyExist) { //해당폴더에 기존 파일이 존재하지 않는다면 실행
			
			File file = null;
		   	
		   	file = new File(targetJavaFile);
	        if (!file.isDirectory()) { //디렉토리가 없을 경우 디렉토리 생성
	            if(!file.mkdirs()) System.out.println("폴더생성실패");	
	        }
	        
		   	file = new File(targetSqlXmlFile);
	        if (!file.isDirectory()) { //디렉토리가 없을 경우 디렉토리 생성
	            if(!file.mkdirs()) System.out.println("폴더생성실패");	
	        }
	        
	        file = new File(targetJspFile);
	        if (!file.isDirectory()) { //디렉토리가 없을 경우 디렉토리 생성
	        	if(!file.mkdirs()) System.out.println("폴더생성실패");
	        }
			
			for(int i = 0; i < templatefiles.length; i++){
				BufferedReader orgBr = null;

				try {
					orgBr = new BufferedReader(new InputStreamReader(new FileInputStream(real_path + "/WebContent/makecode/include/template" + templatefiles[i]), "UTF-8"));

					String line = "";
				   	String tempContent = "";
				   	boolean bpk = false;
				   	while((line = orgBr.readLine()) != null){
				   		bpk = false;
				   		line = line.replaceAll("@CLASSSMALLNAME@",CLASSSMALLNAME);
				   		line = line.replaceAll("@CLASSNAME@",CLASSNAME);
				   		line = line.replaceAll("@USERNAME@",USERNAME);
				   		line = line.replaceAll("@TODAY@",TODAY);
				   		line = line.replaceAll("@DESC@",DESC);
				   		line = line.replaceAll("@PKGNAME@",PKGNAME);
				   		line = line.replaceAll("@PKGPATH@",PKGPATH);
				   		line = line.replaceAll("@SYSNAME@",SYSNAME);
				   		line = line.replaceAll("@COLNAMES@",COLNAMES);
				   		line = line.replaceAll("'@COLMODEL@'",COLMODEL);
				   		line = line.replaceAll("@COLEXCEL@",COLEXCEL);
				   		//line = line.replaceAll("@COLMODELXML@",COLMODELXML);
				   		line = line.replaceAll("@COLMODELJSON@",COLMODELJSON);
				   		line = line.replaceAll("@SQL@",SQL);
				   		
				   		line = line.replaceAll("@MODIFYCOLNAMES@",MODIFYCOLNAMES);
				   		line = line.replaceAll("@DELETECOLNAMES1@",DELETECOLNAMES1);
				   		line = line.replaceAll("@DELETECOLNAMES2@",DELETECOLNAMES2);
				   		line = line.replaceAll("@DELETECOLNAMES3@",DELETECOLNAMES3);
				   		line = line.replaceAll("@KEYCOLNAMES@",KEYCOLNAMES);
				   		line = line.replaceAll("@KEYCOLNAMESACTION1@",KEYCOLNAMESACTION1);
				   		line = line.replaceAll("@KEYCOLNAMESACTION2@",KEYCOLNAMESACTION2);
				   		line = line.replaceAll("@KEYMAINCOLNAMESACTION@",KEYMAINCOLNAMESACTION);
				   		line = line.replaceAll("@DELETESQL@",DELETESQL);
				   		line = line.replaceAll("@UPDATESQL@",UPDATESQL);
				   		line = line.replaceAll("@INSERTSQL@",INSERTSQL);
				   		line = line.replaceAll("@WTABLENAME@",wTableName);
				   		line = line.replaceAll("@MODIFYCOLUMN@",MODIFYCOLUMN);
				   		line = line.replaceAll("@DETAILSQL@",DETAILSQL);
				   		line = line.replaceAll("@MODIFYKEYCOLUMN@",MODIFYKEYCOLUMN);
				   		line = line.replaceAll("@KEYMAINCOLNAME@",KEYMAINCOLNAME);
				   		line = line.replaceAll("@ONLYINPUTNUMBER@",ONLYINPUTNUMBER);
				   		line = line.replaceAll("@TEXTAREABYTECHK@",TEXTAREABYTECHK);
				   		line = line.replaceAll("@NOTCOLUMN@",NOTCOLUMN);
				   		line = line.replaceAll("@NOTCOLUMNACTION@",NOTCOLUMNACTION);
				   		
				   		if(bpk && (line == null || "".equals(line.trim()))){
				   			
				   		} else {
				   			tempContent += line + "\n";
				   		}
				   	}
				   	orgBr.close();
				   	orgBr = null;
				   	
				   	File targetFile = null;
				   	
				   	if(i == 0) {
				   		targetFile = new File(targetSqlXmlFile + templatefiles[i].replaceAll("CLASSSMALLNAME",CLASSSMALLNAME).replaceAll("CLASSNAME",CLASSNAME));
				   	} else if(i == 1) {
				   		targetFile = new File(targetJavaFile + templatefiles[i].replaceAll("CLASSSMALLNAME",CLASSSMALLNAME).replaceAll("CLASSNAME",CLASSNAME));
				   	} else {
				   		targetFile = new File(targetJspFile + templatefiles[i].replaceAll("CLASSSMALLNAME",CLASSSMALLNAME).replaceAll("CLASSNAME",CLASSNAME));
				   	}
				   	
					FileOutputStream targetOs = new FileOutputStream(targetFile);
					OutputStreamWriter targetWr = new OutputStreamWriter(targetOs, "UTF-8");
					targetWr.write(tempContent);
					targetWr.flush();
					targetWr.close();
				   	
				   	System.out.println( templatefiles[i].replaceAll("CLASSSMALLNAME",CLASSSMALLNAME).replaceAll("CLASSNAME",CLASSNAME) + " complete." );
			  	} catch(Exception e) {
			   		e.printStackTrace();
			   		isOK = false;
			   		System.out.println( templatefiles[i].replaceAll("CLASSSMALLNAME",CLASSSMALLNAME).replaceAll("CLASSNAME",CLASSNAME) + " 실패." );
			  	}
			}
			
			//sqlmap-config 설정 시작
			BufferedReader orgBr = null;

			try {
				orgBr = new BufferedReader(new InputStreamReader(new FileInputStream(targetSqlMapFile), "UTF-8"));

				String line = "";
			   	String tempContent = "";
			   	boolean bpk = false;
			   	
			   	while((line = orgBr.readLine()) != null){
			   		bpk = false;
			   		
			   		if("<!-- @@@ sqlmap-end @@@ -->".equals(line.trim())) {
			   			if(!"true".equals(wOverWrite)) {
			   				tempContent += "    <sqlMap resource=\"/com/lexken/" + SYSNAME + "/" + PKGNAME + "/sql/" + CLASSNAME + "-sqlmap.xml\" />            <!-- " + DESC + " -->" + "\n";
			   			}
			   		}

			   		if(bpk && (line == null || "".equals(line.trim()))) {
			   			
			   		} else {
			   			tempContent += line + "\n";
			   		}
			   	}
			   	orgBr.close();
			   	orgBr = null;
			   	
			   	File targetFile = null;
			   	targetFile = new File(targetSqlMapFile);
			   	
				FileOutputStream targetOs = new FileOutputStream(targetFile);
				OutputStreamWriter targetWr = new OutputStreamWriter(targetOs, "UTF-8");
				targetWr.write(tempContent);
				targetWr.flush();
				targetWr.close();
			   	
			   	System.out.println( targetSqlMapFile + " complete." );
		  	} catch(Exception e) {
		   		e.printStackTrace();
		   		isOK = false;
		   		System.out.println( targetSqlMapFile + " 실패." );
		  	}
			//sqlmap-config 설정 끝
		}
	}
%>
<html>
<head>
<title>code creator</title>
<script>
	function fn_submit(){
		document.getElementById("form1").submit();
	}
	
	<% if(isAlreadyExist) { %>	
		alert(" 해당폴더에 이미 파일이 존재합니다.\n 파일 삭제후 실행해 주십시요");
	<% }else if(isOK){ %>
		alert(" 생성되었습니다.");
	<% }else{ %>
		alert(" 실패하였습니다.");
	<% } %>
	
	history.go(-1);
</script>
</head>
<body>
</body>
</html>
