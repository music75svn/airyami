package com.lexken.framework.common;

public class InsideCode {
	
	/**
	 * 내부코드
	 * @param type
	 * @param code
	 * @return
	 */
	public String getCodeName(String type, int code) {
		return getCodeName(type, Integer.toString(code));
	}
	
	/**
	 * 내부코드
	 * @param type
	 * @param code
	 * @return
	 */
	public String getCodeName(String type, String code) {
		String stCodeName = "";
			
		/**********************************
		 * useYn(사용, 미사용)
		 **********************************/
		if ("useYn".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "사용";
			} else if( "N".equals(code) ) {
				stCodeName = "미사용";
			}
		
		/**********************************
		 * 성별
		 **********************************/
		} else if ("gender".equals(type) ) {
			if( "1".equals(code) ) {
				stCodeName = "남자";
			} else if( "2".equals(code) ) {
				stCodeName = "여자";
			} else if( "9".equals(code) ) {
				stCodeName = "기타";
			}
			
		/**********************************
		 * 사용자 종류
		 **********************************/
		} else if (type.equals("memberType") ) {
			if( code.equals("1") ) {
				stCodeName = "회원";
			} else if( code.equals("0") ) {
				stCodeName = "비회원";
			} else if( code.equals("2") ) {
				stCodeName = "관리자";
			}
			
		/**********************************
		 * 게시판 종류
		 **********************************/
		} else if (type.equals("kindType") ) {
			if( code.equals("1") ) {
				stCodeName = "FAQ";
			} else if( code.equals("0") ) {
				stCodeName = "일반게시판";
			}
			
		/**********************************
		 * 정렬 모드
		 **********************************/
		} else if ("sortMode".equals(type) ) { 
			if( "ASC".equals(code) ) {
				stCodeName = "▲";
			} else if( "DESC".equals(code) ) {
				stCodeName = "▼";
			}
			
		/**********************************
		 * 회원종류
		 **********************************/
		} else if ("userType".equals(type) ) {
			if( "1".equals(code) ) {
				stCodeName = "내부";
			} else if( "2".equals(code) ) {
				stCodeName = "외부";
			}
									
		/**********************************
		 * 공개여부
		 **********************************/
		} else if ("openYn".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "공개";
			} else if( "N".equals(code) ) {
				stCodeName = "비공개";
			}
									
		/********************************** 
		 * 승인여부
		 **********************************/
		} else if ("apprvlYn".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "승인";
			} else if( "N".equals(code) ) {
				stCodeName = "미승인";
			}
									
		/********************************** 
		 * 선정여부
		 **********************************/
		} else if ("selYn".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "선정";
			} else if( "N".equals(code) ) {
				stCodeName = "미선정";
			}
									
		/********************************** 
		 * 보안여부
		 **********************************/
		} else if ("scrtyGrade".equals(type) ) {
			if( "1".equals(code) ) {
				stCodeName = "보안";
			} else if( "2".equals(code) ) {
				stCodeName = "일반";
			}
			
		/********************************** 
		 * 보안과제
		 **********************************/
		} else if ("scrtySubject".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "보안과제";
			} else if( "N".equals(code) ) {
				stCodeName = "일반과제";
			}	
			
		/********************************** 
		 * 국내외구분
		 **********************************/
		} else if ("areaDiv".equals(type) ) {
			if( "1".equals(code) ) {
				stCodeName = "국내";
			} else if( "2".equals(code) ) {
				stCodeName = "국외";
			}
		
		/********************************** 
		 * 국내외구분
		 **********************************/
		} else if ("areaDivYn".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "국내";
			} else if( "N".equals(code) ) {
				stCodeName = "국외";
			}
		
		/********************************** 
		 * 국외 여부
		 **********************************/
		} else if ("frgnYn".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "국외";
			} else if( "N".equals(code) ) {
				stCodeName = "국내";
			}
		/********************************** 
		 * 국제여부
		 **********************************/
		} else if ("frgnYn2".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "국제";
			} else if( "N".equals(code) ) {
				stCodeName = "국내";
			}	
		/********************************** 
		 * 대내외구분1
		 **********************************/
		} else if ("domesticYn".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "대내";
			} else if( "N".equals(code) ) {
				stCodeName = "대외";
			}	
		/********************************** 
		 * 대내외구분2
		 **********************************/
		} else if ("cprtDiv".equals(type) ) {
			if( "1".equals(code) ) {
				stCodeName = "대내";
			} else if( "2".equals(code) ) {
				stCodeName = "대외";
			}	
		/********************************** 
		 * 연구원상세구분
		 **********************************/
		} else if ("perGradeDetail".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "세부(협동)책임자";
			} else if( "N".equals(code) ) {
				stCodeName = "연구원";
			}
									
		/********************************** 
		 * 연구원상세구분
		 **********************************/
    	} else if ("commonYn".equals(type) ) {
    	    if( "Y".equals(code) ) {
    	        stCodeName = "공통";
    	    } else if( "N".equals(code) ) {
    	        stCodeName = "개별";
    	    }
			
		/********************************** 
		 * 어젠다구분
		 **********************************/
    	} else if ("agendaTypeCd".equals(type) ) {
    	    if( "01".equals(code) ) {
    	        stCodeName = "어젠다";
    	    } else if( "02".equals(code) ) {
    	        stCodeName = "대분류";
    	    } else if( "03".equals(code) ) {
    	        stCodeName = "중분류";
    	    }
			
		/********************************** 
		 * 유무구분
		 **********************************/
    	} else if ("existCd".equals(type) ) {
    	    if( "1".equals(code) ) {
    	        stCodeName = "유";
    	    } else if( "2".equals(code) ) {
    	        stCodeName = "무";
    	    }
        	    
		/********************************** 
		 * 승계여부
		 **********************************/
		} else if ("scinYn".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "승계";
			} else if( "N".equals(code) ) {
				stCodeName = "미승계";
			}
		
		/********************************** 
		 * 참여구분(연구실적)
		 **********************************/
		} else if ("prjSlctYn".equals(type) ) {
			if( "01".equals(code) ) {
				stCodeName = "연차주관과제";
			} else if( "02".equals(code) ) {
				stCodeName = "연차세부과제";
			} else if( "03".equals(code) ) {
				stCodeName = "연차협동과제";
			}	 
    					    
		/********************************** 
		 * 참여구분(지도사업실적)
		 **********************************/
		} else if ("workSlctYn".equals(type) ) {
			if( "01".equals(code) ) {
				stCodeName = "지도사업주관계획";
			} else if( "02".equals(code) ) {
				stCodeName = "지도사업세부계획";
			}
    					    
		/********************************** 
		 * 계약관계설정(생성여부)
		 **********************************/
		} else if ("cntrctYn".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "생성";
			} else if( "M".equals(code) ) {
				stCodeName = "생성대상자";
			} else{
				stCodeName = "미생성";
			}
		    
		/********************************** 
		 * 통합검색 과제구분
		 **********************************/
		} else if ("searchGubun".equals(type) ) {
			if( "01".equals(code) ) {
				stCodeName = "과제";
			} else{
				stCodeName = code;
			}
			
		/********************************** 
		 * 한글논문여부
		 **********************************/
		} else if ("korEsayYn".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "한글논문";
			} else if( "N".equals(code) ) {
				stCodeName = "기타";
			}	
			
		/********************************** 
		 * SCI 논문여부
		 **********************************/
		} else if ("sciYn".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "SCI";
			} else if( "N".equals(code) ) {
				stCodeName = "비SCI";
			}
		/********************************** 
		 * 특허PCT출원여부
		 **********************************/
		} else if ("ptntPctPtappYn".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "특허PCT출원";
			} else if( "N".equals(code) ) {
				stCodeName = "출원아님";
			}		
		/********************************** 
		 * 직종구분코드(근무성적평가)
		 **********************************/
		} else if ("jobTypeClsCd".equals(type) ) {
			if( "00".equals(code) ) {
				stCodeName = "일반직";
			} else if( "01".equals(code) ) {
				stCodeName = "연구직";
			}
			
		/********************************** 
		 * 평가구분(근무성적평가)
		 **********************************/
		} else if ("evalType".equals(type) ) {
			if( "01".equals(code) ) {
				stCodeName = "상반기";
			} else if( "02".equals(code) ) {
				stCodeName = "하반기";
			}
			
		/********************************** 
		 * SMS 발송성공여부
		 **********************************/
		} else if ("smsSuccessYn".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "성공";
			} else if( "N".equals(code) ) {
				stCodeName = "실패";
			}
			
		/********************************** 
		 * normalYn( Y, N )
		 **********************************/
		} else if ("normalYn".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "Y";
			} else if( "N".equals(code) ) {
				stCodeName = "N";
			}		
    					    
    	
        /**********************************
		 * 가부 여부( Y, N )
		 **********************************/
		} else if ("acceptYn".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "가";
			} else if( "N".equals(code) ) {
				stCodeName = "부";
			}

        /**********************************
		 * 소액면제 여부( Y, N )
		 **********************************/
		} else if ("smallSumGb".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "면제";
			} else if( "N".equals(code) ) {
				stCodeName = "납부";
			}
		
        /**********************************
		 * 검증대상 구분( T, V )
		 **********************************/
		} else if ("vfyObjSlct".equals(type) ) {
			if( "T".equals(code) ) {
				stCodeName = "TABLE";
			} else if( "V".equals(code) ) {
				stCodeName = "VIEW";
			}
			
		/**********************************
		 * 성별
		 **********************************/
		} else if ("prjTabCd".equals(type) ) {
			if( "01".equals(code) ) {
				stCodeName = "주관과제";
			} else if( "02".equals(code) ) {
				stCodeName = "세부과제";
			} else if( "03".equals(code) ) {
				stCodeName = "위탁과제";
			}
			
		/**********************************
		 * 평가구분
		 **********************************/
		} else if ("contractTypeCd".equals(type) ) {
			if( "01".equals(code) ) {
				stCodeName = "직무";
			} else if( "02".equals(code) ) {
				stCodeName = "근평";
			}
		/**********************************Yn
		 * 보고서제출유무
		 **********************************/
		} else if ("presentnCd".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "제출";
			} else if( "N".equals(code) ) {
				stCodeName = "미제출";
			}
		/**********************************
		 * 승급심사 업무실적 구분
		 **********************************/
		} else if ("gubunCd".equals(type) ) {
			if( "01".equals(code) ) {
				stCodeName = "개인연구업무실적";
			} else if( "02".equals(code) ) {
				stCodeName = "부서연구업무실적";
			}
		/**********************************
		 * 연차 세부 비목저장유무
		 **********************************/
		} else if ("dtlSbjItemSaveYn".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "Y";
			} else if( "N".equals(code) || "".equals(code) || code == null) {
				stCodeName = "N";
			}
		/**********************************
		 * 연차 계획서 비목저장유무
		 **********************************/
		} else if ("sbjItemSaveYn".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "완료";
			} else if( "N".equals(code) ) {
				stCodeName = "진행중";
			}
			
		/**********************************
		 * 전송상태
		 **********************************/
		} else if ("transYn".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "전송";
			} else if( "N".equals(code) ) {
				stCodeName = "미전송";
			}
		/**********************************
		 * 가상계좌번호 발급상태
		 **********************************/
		} else if ("virtlAcnutYn".equals(type) ) {
			if( "Y".equals(code) ) {
				stCodeName = "발급";
			} else if( "N".equals(code) ) {
				stCodeName = "미발급";
			}
			
		}
		

        return stCodeName;
	}
}
