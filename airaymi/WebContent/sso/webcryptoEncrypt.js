// 개발팀 서버 공개키
var pubkey1 = 'MIGIAoGAeBZCbkJVAE1oaUiBOC/ToeE5dCpK64E9jYS8+8+wQvXbSOdPkcJTuUxs/ypW9vaqkabE37c9RVRcbXIcqoQaQ1zvv946rgpF4cV57MfbA+gudGSK+bSkTgx9bAvY3qVrkbWWS1l1u2xYwBPsYl4SltsOROpq1t/hdCQbYKLGr+MCAwEAAQA=';
// 지원팀 서버 공개키
var pubkey2 = 'MIGIAoGAcLXicXHD1eDSIL3D3JLb4xsQ7ooPlbKfVQ8Dg2kyWw4sGkAxPXex29fpc/RSjzRwRmCWTMZwT+r6ArMb4YgIBTzBmy/lBYWsFozwJ/meTQojBNPM+bAdp2aYSwoxsmZ8B1PyAnPDtWGzckB01YB3ZeKGmUpvKdqSYRrLuti4Y50CAwEAAQ==';

var encrypt_header      = "encrypt_";
var double_header       = "double_";

var keyname1 = 'Sample1';
var keyname2 = 'Sample2';
var keyname3 = 'Sample3';
var keyname4 = 'Sample4';

function issacweb_escape(msg){
    var i;
    var ch;
    var encMsg = '';
    var tmp_msg = String(msg);

    for (i = 0; i < tmp_msg.length; i++) {
        ch = tmp_msg.charAt(i);

        if (ch == ' ')
            encMsg += '%20';
        else if (ch == '%')
            encMsg += '%25';
        else if (ch == '&')
            encMsg += '%26';
        else if (ch == '+')
            encMsg += '%2B';
        else if (ch == '=')
            encMsg += '%3D';
        else if (ch == '?')
            encMsg += '%3F';
        else if (ch == '|')
            encMsg += '%7C';
        else
            encMsg += ch;
    }
    return encMsg;
}

// NOTE : 아래 함수는 WebCrypto 미적용. 필요시 수정하여 적용한다.
/*
function encryptForm(form){
	var first = true;
	var catMsg = "";
	var curMsg;

	for(var i=0; i< form.length; i++){
		
		if(form.elements[i].type != "button" && form.elements[i].type != "reset" && form.elements[i].type != "submit"){
			if(form.elements[i].type == "checkbox" || form.elements[i].type == "radio"){
				if(form.elements[i].checked){
						curMsg =  form.elements[i].value;
						form.elements[i].checked = false;
				}else{
						continue;
				}
			}else if(form.elements[i].type == "select-one"){
				var index = form.elements[i].selectedIndex;
				
				if(form.elements[i].options[index].value != ""){
						curMsg = form.elements[i].options[index].value;
				}else{
						curMsg = form.elements[i].options[index].text;
				}
				form.elements[i].selectedIndex = 0;
			}else{
					if(form.elements[i].name	== "issacweb_data")
						continue;
					
					curMsg =  form.elements[i].value;
					form.elements[i].value	= "";
			}
			if(first){
				first = false;
			}else{
				catMsg	= catMsg + "&";
			}
			catMsg	+= issacweb_escape(form.elements[i].name) + "=" + issacweb_escape(curMsg);	
		}
	}

    form.elements["issacweb_data"].value = document.IssacWebEnc.getSubApplet().issacweb_hybrid_encrypt_ex_s(catMsg, pubkey1, keyname1, 1);    

	if(form.elements["issacweb_data"].value	== "") {
		alert("issacweb_data is null");
 		return false;
	}
	
	form.submit( );
}
*/


function encryptForm_utf8(form){
	var first = true;	
	var catMsg = "";
	var curMsg;

	for(var i=0; i< form.length; i++){
		if(form.elements[i].type != "button" && form.elements[i].type != "reset" && form.elements[i].type != "submit"){
			if(form.elements[i].type == "checkbox" || form.elements[i].type == "radio"){
				if(form.elements[i].checked){
					curMsg =  form.elements[i].value;
					form.elements[i].checked = false;
				}else{
					continue;
				}
			}else if(form.elements[i].type == "select-one"){
				var index = form.elements[i].selectedIndex;

				if(form.elements[i].options[index].value != ""){
					curMsg = form.elements[i].options[index].value;
				}else{
					curMsg = form.elements[i].options[index].text;
				}
				form.elements[i].selectedIndex = 0;
			}else{
				if(form.elements[i].name	== "issacweb_data")
					continue;
				curMsg =  form.elements[i].value;
				form.elements[i].value	= "";
			}
			if(first){
				first = false;
			}else{
				catMsg	= catMsg + "&";
			}
			catMsg	+= issacweb_escape(form.elements[i].name) + "=" + issacweb_escape(curMsg);	
		}
	}

	try{
		var reqHybridEnc = webcrypto.e2e.hybridEncrypt(keyname1, catMsg, 'UTF-8', 'SEED', pubkey1, 'RSAES-OAEP', 'RSA-SHA1');
		reqHybridEnc.onerror = function(errMsg) { alert(errMsg); };
		reqHybridEnc.oncomplete = function(result) {
			form.elements["issacweb_data"].value = result;
			if(form.elements["issacweb_data"].value === "") {
				alert("issacweb_data is null");
				return;
			}
			form.submit();
		};
	}catch(e){
		if (e.message) {
			alert(e.message);
		} else {
			alert(e);
		}
	}
}

// NOTE : 아래 함수는 WebCrypto 미적용. 필요시 수정하여 적용한다.
/*
function encryptSeleted(form){
	for(var i=0; i<form.length; i++){
		if(form.elements[i].type != "button"
			&& form.elements[i].type != "reset" 
			&& form.elements[i].type != "submit")
		{
			if(form.elements[i].type == "checkbox" 
				|| form.elements[i].type == "radio"){
				if(form.elements[i].checked){
					// 처리 부분
					if(form.elements[i].name.indexOf(encrypt_header) != -1)	
            form.elements[i].value  = document.IssacWebEnc.getSubApplet().issacweb_encrypt_ex_s(form.elements[i].value, keyname1, 1);
				}else{
						continue;
				}
			}else if(form.elements[i].type == "select-one"){
				var index = form.elements[i].selectedIndex;
				if(form.elements[i].options[index].value != "text1"){
					if(form.elements[i].name.indexOf(encrypt_header) != -1)	
            form.elements[i].value  = document.IssacWebEnc.getSubApplet().issacweb_encrypt_ex_s(form.elements[i].value, keyname1, 1);
				}else{
					if(form.elements[i].name.indexOf(encrypt_header) != -1)	
            form.elements[i].value  = document.IssacWebEnc.getSubApplet().issacweb_encrypt_ex_s(form.elements[i].value, keyname1, 1);
        }
			}else{
					// Text & password field
					if(form.elements[i].name	== "issacweb_data"){
            form.elements[i].value  =  document.IssacWebEnc.getSubApplet().issacweb_hybrid_encrypt_ex_s_utf8("", pubkey1, keyname1, 1);
						continue;
					}
					if(form.elements[i].name.indexOf(encrypt_header) != -1)	
				{
						//alert(keyname1);
						form.elements[i].value	= document.IssacWebEnc.getSubApplet().issacweb_encrypt_ex_s(form.elements[i].value, keyname1, 1);
				}
			}
		}
	}
	form.submit();
}
*/
