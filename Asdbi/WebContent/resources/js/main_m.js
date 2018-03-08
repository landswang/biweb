//打开/关闭币菜单
function openbottoncoins(event){
	validetoken();
	$(".bottomcoins").slideToggle(300);
	event.stopPropagation ? event.stopPropagation() : event.cancelBubble = true;
	setTimeout("baidutongji()",0);
}

//验证凭证
function validetoken(){
	var tokens = $("input[name='token']");
	if(tokens.length > 0){
		var tokenvalue = $(tokens[0]).val();
		if(readCookie("tokenvalue") == tokenvalue){
			writeCookie("tokenvalue","",-1);
			window.location.href = window.location.href;
		}else{
			writeCookie("tokenvalue",tokenvalue,1);
		}
	}
}

var _hmt = _hmt || [];

//百度统计
function baidutongji(){
	var hm = document.createElement("script");
	hm.src = "https://hm.baidu.com/hm.js?8c5bcdaea6cbc8f9b27576f7b5341d84";
	var s = document.getElementsByTagName("script")[0]; 
	s.parentNode.insertBefore(hm, s);
}

//修改验证码
function changevocde(){
    $("#vcodeimg").attr("src", "/validatecode.do?width=76&height=25&tm=" + Math.round(Math.random() * 1000000));
}

var duanxinsended = false;

var duanxinsecond = 0;

//发送短信验证码
function sendmobilevalidate(){
	if (duanxinsecond == 0){
		$(".sendmobilevalidate").removeAttr("onclick");
		$.ajax({  
			type: "get",      
			url: "/interfaces/duanxin/send", 
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