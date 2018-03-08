//初始化
$(document).ready(function(){
	var clipboard = new Clipboard(document.getElementById("btncopywalletaddress"));
    clipboard.on("success", function(e){
        alert("已成功复制到剪贴板");
    });
    clipboard.on("error", function(e){
    	alert("复制失败，请手动复制");
    });
});

//金额区间验证
function checkmoneyrang(){
	var money = parseFloat($("#money").val());
	var moneyfloor = parseFloat($("#moneyfloor").val());
	if (money < moneyfloor){
		alert("充值金额不能低于下限——" + moneyfloor);
		return false;
	}else{
		return true;
	}
}

//订单确认
function rmbputinsure(){
	opendialog(
		"dialog_rmbputinsure",
		"/member/system/dialog/rmbputinsure",
		{putinmethod:(parseInt($("select[name='bankcardid']").val().substr($("select[name='bankcardid']").val().indexOf("-") + 1)) == 21 ? "1" : "2"), money:$("#money").val()},
		null,
		function(){
			getform("form_rbmputin").submit();
		}
	);
}

//表单验证
function checkform(){
	if (checknull("money", "充值金额") && checkfloat("money", "充值金额") && checkmoneyrang() && checknull("mobile", "联系手机")) rmbputinsure();
    return false;
}