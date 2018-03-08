//初始化
$(document).ready(function(){
	$("#mobilevcode").val("");
});

//表单验证
function checkform(){
    if(checknull("passwordNew", "新密码") && checklength("passwordNew", 6, "新密码") && checkpassword("passwordNew") && checknull("passwordNewSure", "新密码确认") && checkequal("passwordNew", "passwordNewSure", "新密码", "新密码确认") && checkmobilevocde() && checknull("mobilevcode", "短信验证码")){
    	preventresubmit();
    	return true;
    }else{
    	return false;
    }
}