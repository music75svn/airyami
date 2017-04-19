<?php
/*
* Spac Editor v13.1.1
* 공식배포처 : http://www.webeditor.kr
* 제자자 이메일 : master@phpwork.kr
* 이프로그램은 개인/기업/영리/비영리 에 관계없이 웹개발에 무료로 자유롭게 사용할 수 있습니다.
* 제작자가 배포한상태 그대로인 경우에 한해 재배포를 허용하며 수정된 소스나, 수정된 소스가 포함된 프로그램, 소스중의 일부분을 타인에게 배포하거나 판매할 수 없습니다.
* 제작자와 별도의 상의없이 본프로그램의 버전및 공식사이트 정보를 보여주는 "Spac Editor는?"(제일 오른쪽에 "?" 형태의 아이콘으로 출력됨) 버튼을 임의로 안보이도록 할 없습니다.(모든 툴바를 감추는 경우는 자동으로 감추어지므로 관계없습니다.)
* 이프로그램의 사용으로 인해 발생한 어떠한 문제도 제작자는 책임지지 않습니다.
* 사용상의 문제나 건의사항은 공식 배포사이트의 게시판을 이용해주시거나 메일로 보내 주시기 바랍니다.
*/
$base_dir				= dirname($_SERVER["PHP_SELF"]);

//--------------- 사용자 설정------------------------------------------------------시작
//[이미지삽입]
$image_path				= "qupload";													//파일 저장 디렉토리의 서버 절대, 또는 상대경로
$image_webpath			= "http://".$_SERVER["SERVER_NAME"].$base_dir."/qupload";			//파일 저장 디렉토리의 웹경로
$image_dir_type			= "month";														//year,month,day (year: $image_webpath/2012) , default : month
$image_allow_file_ext	= array("jpg","gif","png");										//업로드 허용 이미지파일 확장자

//[첨부파일]
$attach_path			= "qupload";													//파일 저장 디렉토리의 서버 절대, 또는 상대경로
$attach_dir_type		= "month";														//year,month,day (year: $attach_path/2012) , default : month
$attach_allow_file_ext	= array("zip","rar","tar","gz","tgz","jpg","gif","png","hwp","pdf","xls","xlsx","ppt","pptx","doc","docx");			//업로드 허용 첨부파일 확장자
$allow_direct_link		= false;														//첨부파일의 외부 다운로드 링크 허용여부(true:허용, false: 허용안함)
//--------------- 사용자 설정------------------------------------------------------끝


if(isset($_COOKIE['USERKEY']) && $_COOKIE['USERKEY'] != "") {
	$USERKEY	= $_COOKIE['USERKEY'];
} else {
	$USERKEY	= time();
	setcookie('USERKEY', $USERKEY, time()+3600);
}
$FILEKEY		= substr(md5($USERKEY),0,15);

//업로드
if($_POST['type'] == "image" || $_POST['type'] == "attach") {
	if(is_uploaded_file($_FILES["up_file"]["tmp_name"])) {
		$sub_path		= date("Ym");
		if($_POST['type'] == "image") {
			if($image_dir_type == "year")	$sub_path = date("Y");
			if($image_dir_type == "day")	$sub_path = date("Ymd");
			$upload_path = $image_path.'/'.$sub_path;
		} else if($_POST['type'] == "attach") {
			if($attach_dir_type == "year")	$sub_path = date("Y");
			if($attach_dir_type == "day")	$sub_path = date("Ymd");
			$upload_path = $attach_path.'/'.$sub_path;
		} else {
			echo "<script>alert('Access Denied!');parent.document.getElementById('btn_insert_image').disabled=false;</script>";
			exit;		
		}
		if(!is_dir($upload_path)) {
			if(!mkdir($upload_path,0707)) {
				echo "<script>alert('directory create fail!');</script>";
				exit;
			}	
			if(!chmod($upload_path,0707)) {
				echo "<script>alert('upload directory access denied!');</script>";
				exit;
			}
		}
		if($_POST['type'] == "image") {
			$fileExt	= strtolower(array_pop(explode(".",$_FILES["up_file"]["name"])));
			if(!in_array($fileExt,$image_allow_file_ext)) {
				echo "<script>alert('upload fail!(not allow file)');parent.document.getElementById('btn_insert_image').disabled=false;</script>";
				exit;
			}
			$imgObj		= getimagesize($_FILES["up_file"]["tmp_name"]);
			if($imgObj[2] > 0 && $imgObj[2] < 4) {
				$org_filename = $_FILES["up_file"]["name"];
				$org_filename_array = explode(".",$org_filename);
				array_pop($org_filename_array);
				$img_alt = implode(".",$org_filename_array);
				$filename = date("YmdHis")."_".$FILEKEY.".".$fileExt;
				if(!move_uploaded_file($_FILES["up_file"]["tmp_name"],$upload_path.'/'.$filename)) {
					echo "<script>alert('file upload fail!');parent.document.getElementById('btn_insert_image').disabled=false;</script>";
					exit;
				}
				@chmod($upload_path.'/'.$filename,0707);
				$fileUrl = $image_webpath.'/'.$sub_path.'/'.$filename;
			} else {
				echo "<script>alert('upload fail!');parent.document.getElementById('btn_insert_image').disabled=false;</script>";
				exit;	
			}
		} else if($_POST['type'] == "attach") {
		      echo "<script>alert(.$upload_path.);</script>";
			$fileExt	= strtolower(array_pop(explode(".",$_FILES["up_file"]["name"])));
			if(!in_array($fileExt,$attach_allow_file_ext)) {
				echo "<script>alert('upload fail!(not allow file)');parent.document.getElementById('btn_insert_image').disabled=false;</script>";
				exit;
			}
			$org_filename = $_FILES["up_file"]["name"];
			$filename = date("YmdHis").sprintf("%06d",rand(0,999999)).'_'.$FILEKEY;
			if(!move_uploaded_file($_FILES["up_file"]["tmp_name"],$upload_path.'/'.$filename)) {
				echo "<script>alert('file upload fail!');parent.document.getElementById('btn_insert_image').disabled=false;</script>";
				exit;
			}
			@chmod($upload_path.'/'.$filename,0707);
			$full_attach_path	= $sub_path.'^'.$filename.'^'.$org_filename;
			$fileLink = "<span style='margin:3px;'><img src='".$base_dir."/images/disk.png' align='absmiddle'> <a href='".$base_dir."/spac_editor_attach.php?type=dnld&file=".$full_attach_path."' title='파일다운로드'><span style='font-weight:bold'>$org_filename</span></a></span>";

			$fileListLink_Size = format_size($_FILES["up_file"]["size"]);				
			$fileListLink_Path = $full_attach_path;
			$fileListLink_OrgName = $org_filename;

		} else {
			echo "<script>alert('Access Denied!');parent.document.getElementById('btn_insert_image').disabled=false;</script>";
			exit;	
		}
	} else {
		echo "<script>alert('file not uploaded!');parent.document.getElementById('btn_insert_image').disabled=false;</script>";
		exit;	
	}
//다운로드
} else if($_GET['type'] == "dnld") {
	if($allow_direct_link != true) {
		if(!preg_match('@^http[s]*:\/\/'.$_SERVER["SERVER_NAME"].'@ui',$_SERVER['HTTP_REFERER'])) {
			die('File Not Found'); 
			exit;
		}
	}
	$file_info	= explode('^',iconv("utf-8","euc-kr",$_GET['file']));
	$fullPath	= $attach_path."/".$file_info[0]."/".$file_info[1];
	if( headers_sent() ) die('Headers Sent'); 
	if(ini_get('zlib.output_compression')) ini_set('zlib.output_compression', 'Off'); 	
	if( file_exists($fullPath) ) { 		
		$fsize = filesize($fullPath); 
		$path_parts = pathinfo($file_info[2]); 
		$ext = strtolower($path_parts["extension"]); 			
		switch ($ext) { 
		  case "pdf": $ctype="application/pdf"; break; 
		  case "exe": $ctype="application/octet-stream"; break; 
		  case "zip": $ctype="application/zip"; break; 
		  case "doc": $ctype="application/msword"; break; 
		  case "docx": $ctype="application/msword"; break; 
		  case "xls": $ctype="application/vnd.ms-excel"; break; 
		  case "xlsx": $ctype="application/vnd.ms-excel"; break; 
		  case "ppt": $ctype="application/vnd.ms-powerpoint"; break; 
		  case "pptx": $ctype="application/vnd.ms-powerpoint"; break; 
		  case "gif": $ctype="image/gif"; break; 
		  case "png": $ctype="image/png"; break; 
		  case "jpeg": 
		  case "jpg": $ctype="image/jpg"; break; 
		  default: $ctype="application/force-download"; 
		}			
		header("Pragma: public");
		header("Expires: 0"); 
		header("Cache-Control: must-revalidate, post-check=0, pre-check=0"); 
		header("Cache-Control: private",false);
		header("Content-Type: $ctype"); 
		header("Content-Disposition: attachment; filename=\"".$file_info[2]."\";" ); 
		header("Content-Transfer-Encoding: binary"); 
		header("Content-Length: ".$fsize); 
		ob_clean(); 
		flush(); 
		readfile($fullPath);
		exit;
	} else {		
		die('File Not Found'); 
		exit;
	}
} else if($_GET['type'] == "dlt") {
	if(!preg_match('@^http:\/\/'.$_SERVER["SERVER_NAME"].'@ui',$_SERVER['HTTP_REFERER'])) {
		echo "<script>File Not Found</script>');";
		die('File Not Found'); 
		exit;
	}
	$file_info	= explode('^',iconv("utf-8","euc-kr",$_GET['file']));
	$file_name_array = explode('_',$file_info[1]);
	$fullPath	= $attach_path."/".$file_info[0]."/".$file_name_array[0]."_".$FILEKEY;
	@unlink($fullPath);
	echo "<script>parent.removeFileLinkString('".$file_info[1]."')</script>";
	exit;
} else {
	echo "<script>alert('Access Denied!');parent.document.getElementById('btn_insert_image').disabled=false;</script>";
	exit;	
}
function format_size($size){
	$sizes = array(" Bytes"," KB"," MB"," GB");
	if($size==0){return('n/a');}
		else {
			return(round($size/pow(1024, ($i=floor(log($size, 1024)))), 2). $sizes[$i]);
		}
}
?>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>spac editor file attachment</title>
<script language="Javascript">
<!--
function insertParent(type) {
	if(type == "image") {
		parent.document.getElementById('image_hyperlink_values').value = "<?=$fileUrl?>";
		parent.document.getElementById('img_alt').value = "<?=$img_alt?>";
		parent.previewWebImage();
	} else if(type == "attach") {
		parent.document.getElementById('image_previews').innerHTML = "<?=$fileLink?>";

		var f_size		= "<?=$fileListLink_Size?>";
		var f_path		= "<?=$full_attach_path?>";
		var f_orgname	= "<?=$org_filename?>";
		var f_name		= f_path.split("^")[1];
		var base_dir	= "<?=$base_dir?>";

		parent.document.getElementById('file_list_box_contents').innerHTML += "<span class='file_list_unit' id='idx_"+f_name+"'>"+f_orgname+"<a href=\"javascript:addFileLink('"+f_path+"')\" title='본문에 다운로드 링크 삽입'> <img src='"+base_dir+"/images/add.png' align='absmiddle'></a><a href=\"javascript:removeFileLink('"+f_path+"')\" title='첨부파일 삭제'><img src='"+base_dir+"/images/delete.png' align='absmiddle'></a></span>";
		parent.viewFileListBox(1);
		parent.document.getElementById('btn_input_image').style.display = "block";
		parent.document.getElementById('btn_insert_image').disabled = false;
	}
}
//-->
</script>
</head>
<body onLoad="insertParent('<?=$_POST['type']?>')">
</body>
</html>

