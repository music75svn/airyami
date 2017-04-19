<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Spac Editor Form Action Result</title>
<style type="text/css" title="">
body {
	font-size:12px;
}
div.var_box  {
	background-color:#f2f2f2;
	border-width:1px;
	border-style:solid;
	border-color:#d9d9d9;
	padding:5px;
	margin-bottom:10px;
}
</style>
</head>
<body>
<?php
$POST_DATA = print_r($_POST,true);
?>
[POST DATA]<br/>
<div class="var_box"><?=nl2br(htmlspecialchars(stripslashes($POST_DATA)));?></div>

</body>
</html>