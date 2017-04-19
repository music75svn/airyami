<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%
	/**
		예외 처리
		-> isign_sso_login_include.jsp가 직접 호출되면 index 및 business 페이지로 send
	*/
	String err = javax.servlet.http.HttpUtils.getRequestURL(request).toString();
	String SERVICE_BUSINESS_PAGE = session.getAttribute("SERVICE_BUSINESS_PAGE") == null ? "/index.jsp" : session.getAttribute("SERVICE_BUSINESS_PAGE").toString();
	if (err.indexOf("isign_sso_login_include.jsp") != -1) {
		response.sendRedirect(SERVICE_BUSINESS_PAGE);
		return;
	}
	

	String SSID = session.getAttribute("SSID") == null ? "" : session.getAttribute("SSID").toString();
	String AUTHORIZATION_URL = session.getAttribute("AUTHORIZATION_URL") == null ? "" : session.getAttribute("AUTHORIZATION_URL").toString();
	String AUTHORIZATION_SSL_URL = session.getAttribute("AUTHORIZATION_SSL_URL") == null ? "" : session.getAttribute("AUTHORIZATION_SSL_URL").toString();
	String authMethod = session.getAttribute("authMethod") == null ? "" : session.getAttribute("authMethod").toString();
	String USEISIGNPAGE = session.getAttribute("USEISIGNPAGE") == null ? "" : session.getAttribute("USEISIGNPAGE").toString();
	String checkServer = session.getAttribute("checkServer") == null ? "" : session.getAttribute("checkServer").toString();
	String Exception = session.getAttribute("Exception") == null ? "" : session.getAttribute("Exception").toString();
	String EXISTLOGIN = session.getAttribute("EXISTING_LOGIN_PAGE") == null ? "" : session.getAttribute("EXISTING_LOGIN_PAGE").toString();
	

	
	/**
	예외 처리
		-> checkServer를 거치지 않았으면 buseinss 페이지 호출
		-> 인증서버와 통신도중 문제가 발생 되면 기존 로그인 페이지로 리다이렉션
	*/	
	if (true == checkServer.equals("")) {
		response.sendRedirect(SERVICE_BUSINESS_PAGE);
		return;
	} else {
		if (true == Exception.equals("Y")) {
			if (EXISTLOGIN.equals("") == true) {
				// 인증서버와 통신도중 문제가 발생하면 출력되는 메시지
				out.println("<h1> 네트워크에 문제가 있습니다. 확인해주세요. </h1>");
				return;
			} else {
				// 인증서버와 통신도중 문제가 발생하면 EXISTLOGIN으로 리다이렉션
				response.sendRedirect(EXISTLOGIN);
				return;
			}
		}
	}


/************************************************************
 *	공통 로그인  부분
 ************************************************************/	
if (true == USEISIGNPAGE.equals("Y")) {
	if (authMethod.indexOf("ssl") != -1) {
		response.sendRedirect(AUTHORIZATION_SSL_URL+"LoginServlet?method=idpwForm&ssid="+SSID);
		return;
	} else {
		response.sendRedirect(AUTHORIZATION_URL+"LoginServlet?method=idpwForm&ssid="+SSID);
		return;
	}

} else {
/************************************************************
 *	개별 로그인  부분
 ************************************************************/		
	if (authMethod.indexOf("ssl") == -1) {
%>
<script src="/sso/webcryptoEncrypt.js"></script>

<!-- WebCrypto 로드 및 초기화 부분 -->
<% if (authMethod == "idpw") {%>
<%@ include file="/sso/webcrypto/include_webcrypto/include_wc-e2e-simple.jsp" %>
<%} else { %>
<%@ include file="/sso/webcrypto/include_webcrypto/include_wc-all.jsp" %>
<% } %>
<script language="JavaScript">
	// 이벤트 등록 메소드, 크로스브라우징 고려
	var addEvent = addEvent || (function(_window) {
		if (_window.addEventListener) {
			return function(element, eventName, callback, isCapture) {
				element.addEventListener(eventName, callback, isCapture);
			};
		} else if (_window.attachEvent) {
			return function(element, eventName, callback) {
				element.attachEvent('on' + eventName, callback);
			};
		} else {
			return function(element, eventName, callback) {
				element['on' + eventName] = callback;
			};
		}
	})(window);

	// WebCrypto PKA 모듈 및 UI 초기화
	function setupWebCryptoPKA() {
		// NOTE : 버전 체크 기능이 필요하면 활용하여 적용하고 불필요하면 주석 부분 모두 삭제할 것 (페이지 사이즈 감소 측면)
		/*
		var verPka = webcrypto.pka.getVersion().verPka;
		alert('WC-PKA 버전 : ' + verPka);

		var verE2e = webcrypto.e2e.getVersion().verE2e;
		alert('WC-E2E 버전 : ' + verE2e);
		*/

		// WC-PKA의 PFX 파일 다이얼로그를 위한 iframe include
		includeWebCryptoPfxFileIframe();

		// UI 초기 설정
		webcrypto.pka.ui.initialize();

		// 설치 패키지 동작 확인
		var reqInit = webcrypto.addon.initializeCorsUrl(1500);  // 입력 변수는 응답 대기 시간 msec 단위
		reqInit.onsuccess = function() {
			// 설치 프로그램의 버전 업그레이드 필요 여부 확인
			var necessaryVersion = '2.0.5.0';
			if (webcrypto.addon.checkAddonVersion(necessaryVersion) === false) {
				// 자바스크립트 모듈 버전보다 동작중 프로그램 버전이 낮을 경우 false 발생. 설치 패키지의 업그레이드가 필요한 상황
				if(confirm('D\'Amo WebCrypto 프로그램 버전이 낮습니다. 프로그램 다운로드 페이지로 이동하시겠습니까?')) {
					// TODO : 다운로드 페이지 적용할 것
					location.href = "package_download.jsp";
				}
			}

			// 특정 버전을 고정하여 설치패키지를 유도하고 싶을 때 위의 방식을 빼고 다음처럼 사용 가능
			/*
			var necessaryVersion = '2.0.4.0';
			if (reqInit.version !== necessaryVersion) {
				if(confirm('D\'Amo WebCrypto 프로그램의 재설치가 필요합니다. 다운로드 페이지로 이동하시겠습니까?')) {
					// TODO : 다운로드 페이지 적용할 것
				}
			}
			*/
		};
		reqInit.onerror = function(err) {
			if(confirm('D\'Amo WebCrypto 프로그램이 실행중이 아닙니다. 프로그램 다운로드 페이지로 이동하시겠습니까?')) {
				// TODO : 다운로드 페이지 적용할 것
				location.href = "package_download.jsp";
			}
		};
	}

	var authMethod = "<%=authMethod%>";
	function initializeWithWebCrypto() {
		if (authMethod.indexOf('pki') !== -1) {
			setupWebCryptoPKA();
		}

		// NOTE : 페이지 로딩 후 실행되어야 할 내용이 있다면 함수로 작성하여 여기에서 호출하도록 한다.
	}

	addEvent(window, 'load', initializeWithWebCrypto, false);
</script>
<%
	}
%>
<script>
/*********************
 * Global Variable
 *********************/
msg = "인증타입과 다른 방식을 호출하였습니다. 관리자에게 문의해주세요.";
authMethod = "<%=authMethod%>";

/*********************
 * ssl / idpw 인증 방식
 *********************/
function idpwLogin() {
	var frm = document.loginform;	/* login form 수정 */

	if (authMethod.indexOf("idpw") == -1 && authMethod.indexOf("ssl") == -1) {
		alert(msg);
		return false;
	}

	if (frm.id.value == "") {
		alert("아이디를 입력해주세요.");
		frm.id.focus();
		return false;
	}

	if (frm.pw.value == "") {
		alert("비밀번호를 입력해주세요.");
		frm.pw.focus();
		return false;
	}

	if ( authMethod.indexOf("ssl") != -1 ) {
		frm.action = "<%=AUTHORIZATION_SSL_URL%>LoginServlet?method=idpwProcessEx&ssid=<%=SSID%>";
		frm.submit();
		return false;
	} else {
		frm.action = "<%=AUTHORIZATION_URL%>LoginServlet?method=idpwProcess&ssid=<%=SSID%>";
		
		encryptForm_utf8(frm);
		return false;
	}
}

/*********************
 * pki 인증 방식
 *********************/
function pkiLogin() {
	var frm = document.loginform;	/* login form 수정 */
	
	if (authMethod.indexOf("pki") == -1) {
		alert(msg);
		return false;
	}

	frm.action = "<%=AUTHORIZATION_URL%>LoginServlet?method=pkiProcess&ssid=<%=SSID%>";

	var reqChallenge = webcrypto.pka.makeChallenge();
	reqChallenge.onerror = function(errMsg){
		alert('챌린지값 생성 실패 : ' + errMsg);
	};
	
	reqChallenge.oncomplete = function(challenge){
		frm.challenge.value = challenge;

		// 보여줄 저장매체 설정 ['HDD', 'USB', 'Phone', 'HSM', 'USIM'] 중에서 선택하여 배열로 입력
		var displayMediaTypes = ['HDD', 'USB'];
		if (webcrypto.pka.ui.setStorageType(displayMediaTypes) === false) {
			alert('저장매체 설정 실패! 기본 설정값으로 동작합니다.');
		}

		// 인증서 선택 다이얼로그 오픈
		var caTypes = ['NPKI'];
		var reqCertSelect = webcrypto.pka.ui.openCertSelectionBox(caTypes);
		reqCertSelect.onsuccess = function() {
			var reqResponse = webcrypto.pka.makeResponse(frm.challenge.value);
			reqResponse.onerror = function(errMsg) {
				alert('응답값 생성 실패 : ' + errMsg);
				webcrypto.pka.finalize();
			};
			reqResponse.oncomplete = function(response) {
					frm.response.value = response;
					webcrypto.pka.finalize();
					frm.submit();
			};
		};
	};
	

	return false;
}
</script>
<% } %>
