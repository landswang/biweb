//初始化
$(document).ready(function(){
	$("#mobilevcode").val("");
});

//计算实际转出量
function clacactualmoney(obj){
	if($(obj).val() != ""){
		var extractrate = parseFloat($("#extractrate").text());
		var extractfeefloor = parseFloat($("#extractfeefloor").text());
		var money = parseFloat($(obj).val());
		var fee = money.mul(extractrate);
		if(fee < extractfeefloor) fee = extractfeefloor;
		var moneyactual = money.sub(fee);
		if(moneyactual < 0) moneyactual = 0;
		$("#moneyactual").text(moneyactual);
	}else{
		$("#moneyactual").text("0");
	}
}

//金额验证
function checkmoney(){
	var money = parseFloat($("#money").val());
	var balanceactive = parseFloat($("#balanceactive").text());
	var extractupper = parseFloat($("#extractupper").text());
	var extractfloor = parseFloat($("#extractfloor").text());
	if(money < extractfloor || money > extractupper){
		alert("您输入的提现金额只能在" + extractfloor + "~" + extractupper + "之间");
		return false;
	}
	if(money > balanceactive){
		alert("您输入的提现金额大于您的可用余额了");
		return false;
	}
	var moneyactual = parseFloat($("#moneyactual").text());
	if(moneyactual <= 0){
		alert("您的实际提现额不能小于或等于0");
		return false;
	}
	return true;
}

//表单验证
function checkform(){
    if(checknull("money", "提现金额") && checkfloat("money", "提现金额") && checkmoney() && checknull("tradepassword", "交易密码") && checkmobilevocde() && checknull("mobilevcode", "短信验证码")){
    	preventresubmit();
    	return true;
    }else{
    	return false;
    }
}