//初始化
$(document).ready(function(){
	$("#mobilevcode").val("");
});

//验证对方
function checkreceiver(){
	if($("#receiverid").val() == $("#memberid").val()){
		alert("对方ID不能是自己的ID");
		return false;
	}else{
		return true;
	}
}

//验证数量
function checkcount(){
	if($("#count").val() == "" || parseFloat($("#count").val()) == 0){
		alert("PUSH数量必须大于0");
		return false;
	}else{
		return true;
	}
}

//验证金额
function checkmoney(){
	var moneyfeefloor = parseFloat($("#moneyfeefloor").val());
	if($("#money").val() == "" || parseFloat($("#money").val()) <= moneyfeefloor){
		alert("约定金额不能低于或等于手续下限" + moneyfeefloor);
		return false;
	}else{
		return true;
	}
}

//表单验证
function checkform(){
    if(checknull("receiverid", "对方ID") && checkreceiver() && checknull("count", "PUSH数量") && checkfloat("count", "PUSH数量") && checkcount() && checknull("money", "约定金额") && checkfloat("money", "约定金额") && checkmoney() && checknull("tradepassword", "交易密码") && checkmobilevocde() && checknull("mobilevcode", "短信验证码")){
    	preventresubmit();
    	return true;
    }else{
    	return false;
    }
}