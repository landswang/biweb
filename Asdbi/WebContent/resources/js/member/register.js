//初始化
$(document).ready(function(){
	$("#mobilevcode").val("");
});

var agreed = false;

//同意条款
function agree(){
  if(agreed){
  	$("#subbtn").css("background", "gray");
  	$("#subbtn").attr("disabled", "true");
  }else{
  	$("#subbtn").css("background", "#FF4400");
  	$("#subbtn").removeAttr("disabled");
  }
  agreed = !agreed;
}

var duanxinsended = false;

var duanxinsecond = 0;

//发送短信验证码
function sendmobilevalidate(){
	if (duanxinsecond == 0){
		var mobile = $("#mobile").val();
		var vcode = $("#vcode").val();
		if(checknull("mobile", "手机号码") && checkmobile("mobile", "手机号码") && checknull("vcode", "图形验证码")){
			$(".sendmobilevalidate").removeAttr("onclick");
			$.ajax({  
				type: "get",      
				url: "/interfaces/duanxin/sendreg?mobile=" + mobile + "&vcode=" + vcode, 
				cache: false,
				success: function(data){
					if (data == ""){
						duanxinsecond = 60;
						$(".sendmobilevalidate").css("color","#E4E4E4");
						$(".sendmobilevalidate").text(duanxinsecond + "s后重新获取");
						duanxinsended = true;
						waitresendduanxin();
					}else{
						alert(data);
						changevocde();
						$(".sendmobilevalidate").attr("onclick", "sendmobilevalidate()");
					}
				}
			});
		}
	}
}

//等待重发短信
function waitresendduanxin(){
	if (duanxinsecond-- == 1){
		$(".sendmobilevalidate").css("color","white");
		$(".sendmobilevalidate").attr("onclick", "sendmobilevalidate()");
		$(".sendmobilevalidate").text("输入图形验证获取短信");
		changevocde();
	} else {
		$(".sendmobilevalidate").text(duanxinsecond + "s后重新获取");
		setTimeout("waitresendduanxin()", 1000);
	}
}

//验证码短信
function checkmobilevocde(){
    if (!duanxinsended){
    	alert("请发送短信验证码");
    	return false;
    }else{
    	return true;
    }
}

function checkaccountandemail(){
	checkaccount();
	if($("#accountstate").text() == "已注册"){
		alert("账号已注册，请重新输入。");
		return false;
	}
	checkemail();
	if($("#emailstate").text() == "已注册"){
		alert("邮箱已注册，请重新输入。");
		return false;
	}
	return true;
}

//表单验证
function checkform(){
	if(checknull("mobile", "手机号码") && checkmobile("mobile", "手机号码") && checkmobilevocde() && checknull("mobilevcode", "短信验证码") && checknull("password", "密码") && checklength("password", 6, "密码") && checkpassword("password") && checknull("passwordSure", "密码确认") && checkequal("password", "passwordSure", "密码", "密码确认")){
    	preventresubmit();
    	return true;
    }else{
    	return false;
    }
}