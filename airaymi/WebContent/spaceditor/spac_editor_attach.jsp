<%@ page contentType="text/html; charset=utf-8"%>

<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="com.lexken.framework.util.ByTimestampFileRenamePolicy" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>

<%
String fileName = "";
String originalFileName = "";
String fileLink = "";
String realFolder="F:/project/KGS_BSC/WebContent/spaceditor/upImages";//웹 어플리케이션의 절대경로
String context = request.getContextPath();
String fileUrl = context+"/spaceditor/upImages";

//파일이 업로드되는 폴더를 지정한다
String saveFolder="upImages";
String encType="utf-8";//인코딩 타입
int maxSize = 1*1024*1024;//최대 업로드 될 파일크기 1Mb

//현재 jsp페이지의 웹 어플리케이션상의 절대경로를 구한다
//realFolder= context.getRealPath(saveFolder);
//out.println("the realpath is :" + realFolder + "<br>"); 

try{
    MultipartRequest multi = null;
    //전송을 담당할 콤포넌트를 생성하고 그 파일을 전송한다
    //전송할 파일명을 가지고 있는 객체, 서버상의 절대경로, 최대업로드될 파일 크기, 분자코드,기본보안 적용
    multi = new MultipartRequest(request, realFolder,maxSize, encType, new ByTimestampFileRenamePolicy("BSC"));
    //Form의 파라미터 목록을 가져온다
    Enumeration params =multi.getParameterNames();

    //파라미터를 출력한다
    while(params.hasMoreElements()){
        String name =(String)params.nextElement();//전송되는 파라미터 이름
        String value = multi.getParameter(name);//전송되는 파라미터값
        //out.println(name +" = " +value + "<br>");
    }

    out.println("-----------------------------------------<br>");

    //전송되는 파일 정보를 가져와 출력한다
    Enumeration files = multi.getFileNames();

    //파일 정보가 있다면
    while(files.hasMoreElements()){
        //input 태그의 속성이 file 인 태그의 name 속성값 : 파라미터 이름
        String name =(String)files.nextElement();
        
        //서버에 저장된 파일 이름
        fileName = multi.getFilesystemName(name);
        
        //전송전 원래의 파일 이름
        originalFileName = multi.getOriginalFileName(name);
        
        //전송된 파일의 내용 타입
        String type = multi.getContentType(name);
        
        //전송된 파일 속성이 file 인 태그의  name 속성값을 이용해 파일의 객체 생성
        File file = multi.getFile(name);


        //out.println("파라미터 이름 : " +name +"<br>");
        //out.println("실제 파일 이름 : " +original +"<br>");
        //out.println("저장된 이름 : " +filename +"<br>");
        //out.println("파일 타입 : " +type +"<br>");

        if(file!=null){
            //out.println("크기 : " +file.length());
            //out.println("<br>");
        }
    }
}catch(IOException ioe){
    System.out.println(ioe);
}catch(Exception ex){
    System.out.println(ex);
}


%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>spac editor file attachment</title>
<script language="Javascript">
<!--
function insertParent(type) {
    if(type == "image") {
        parent.document.getElementById('image_hyperlink_values').value = "<%=fileUrl%>/<%=fileName%>";
        parent.document.getElementById('img_alt').value = "<%=originalFileName%>";
        parent.previewWebImage();
    } else if(type == "attach") {
    }
}
//-->
</script>
</head>
<body onLoad="insertParent('image')">
</body>
</html>