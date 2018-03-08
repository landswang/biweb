//初始化
$(document).ready(function(){
    if ($("#vcode").length > 0 && $("#vcode").val() != ""){
        $("#vcode").val("");
        changevocde();
    }
});

var agreed = true;

//同意条款
function agree(){
    if(agreed){
    	$("#loginbtn").css("background", "gray");
    	$("#loginbtn").attr("disabled", "true");
    }else{
    	$("#loginbtn").css("background", "#FF4400");
    	$("#loginbtn").removeAttr("disabled");
    }
    agreed = !agreed;
}

//表单验证
function checkform(){
	if (checknull("username", "账号或手机号码") && checknull("password", "密码") && ($("#vcode").length == 0 ? true : checknull("vcode", "验证码"))){
	    preventresubmit();
		return true;
	}else{
		return false;
	}
}