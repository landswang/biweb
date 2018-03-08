//初始化
$(document).ready(function(){
	$("#mobilevcode").val("");
});

var duanxinsended = false;

var duanxinsecond = 0;

//发送短信验证码
function sendmobilevalidate(){
	if (duanxinsecond == 0){
		$(".sendmobilevalidate").removeAttr("onclick");
		$.ajax({  
			type: "get",      
			url: "/interfaces/duanxin/sendmblog", 
			cache: false,
			success: function(data){
				if (data == ""){
					duanxinsecond = 60;
					$(".sendmobilevalidate").css("color","#E4E4E4");
					$(".sendmobilevalidate").text(duanxinsecond + "s后重发");
					duanxinsended = true;
					waitresendduanxin();
				}else{
					alert(data);
					if(data == "用户未")
					$(".sendmobilevalidate").attr("onclick", "sendmobilevalidate()");
				}
			}
		});
	}
}

//等待重发短信
function waitresendduanxin(){
	if (duanxinsecond-- == 1){
		$(".sendmobilevalidate").css("color","white");
		$(".sendmobilevalidate").attr("onclick", "sendmobilevalidate()");
		$(".sendmobilevalidate").text("发送短信");
	} else {
		$(".sendmobilevalidate").text(duanxinsecond + "s后重发");
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

//表单验证
function checkform(){
	if (checkmobilevocde() && checknull("mobilevcode", "短信验证码")){
	    preventresubmit();
		return true;
	}else{
		return false;
	}
}