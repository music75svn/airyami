<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<body>
  <hr /><br />
  <div style="color:red; margin:5px auto;">
    <b>테스트를 위해서 다음 파일을 다운로드하여 설치해 주십시오.</b><br /><br />
  </div>
  <div style="line-height:1.6em;">
    <% String version = "2.0.5.0"; %>
    <b>D'Amo WebCrypto 설치 프로그램(v<%=version%>) 다운로드</b><br />
    <u><a href='package_download/DAmoWebCryptoSetup_<%=version%>.exe'>Windows 제품군 설치파일</a></u><br />
    <!-- 맥 OS, 리눅스는 당분간 미지원 -->
    <!--
    <u><a href='package_download/WebCrypto-<%=version%>.pkg'>MAC OSX 제품군 설치파일</a></u><br />
    <u><a href='package_download/pkg-webcrypto-i386-<%=version%>.deb'>리눅스 우분투 32bit 설치파일</a></u><br />
    <u><a href='package_download/pkg-webcrypto-amd64-<%=version%>.deb'>리눅스 우분투 64bit 설치파일</a></u><br />
    -->
  </div>
  <br /><hr />
</body>