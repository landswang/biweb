//初始化
$(document).ready(function(){
	$("#mobilevcode").val("");
});

//表单验证
function checkform(){
	if(checknull("password", "密码") && checklength("password", 6, "密码") && checkpassword("password") && checknull("passwordSure", "密码确认") && checkequal("password", "passwordSure", "密码", "密码确认") && checkmobilevocde() && checknull("mobilevcode", "短信验证码")){
		preventresubmit();
		return true;
	}else{
		return false;
	}
}