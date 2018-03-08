//初始化
$(document).ready(function(){
	$("#mobilevcode").val("");
});

//打开/关闭币菜单
function openothercoins(event){
	$(".othercoins").slideToggle(300);
	event.stopPropagation ? event.stopPropagation() : event.cancelBubble = true;
}

//计算实际转出量
function clacactualcount(obj){
	if($(obj).val() != ""){
		var extractrate = parseFloat($("#extractrate").text());
		var extractfeefloor = parseFloat($("#extractfeefloor").text());
		var extractdecimalsize = parseInt($("#extractdecimalsize").text());
		var count = parseFloat($(obj).val());
		var fee = count.mul(extractrate);
		if(fee < extractfeefloor) fee = extractfeefloor;
		var countactual = count.sub(fee).places(extractdecimalsize);
		if(countactual < 0) countactual = 0;
		$("#countactual").text(countactual);
	}else{
		$("#countactual").text("0");
	}
}

//地址验证
function checkaddress(){
	var address = $("#address").val();
	var walletaddress = $("#walletaddress").val();
	if(walletaddress != ""){
		if(address == walletaddress){
			alert("提币钱包地址不能是您自己的地址");
			return false;
		}
	}
	var addressregexp = $("#addressregexp").val();
	if(addressregexp != "") {
		if(address.indexOf(" ") > -1){
			alert("钱包地址中不能包含空格");
			return false;
		}else{
			var regex = new RegExp(addressregexp);
			if(!regex.test(address)){
				alert("钱包地址格式错误，请重新核实该虚拟币地址的规范性。");
				return false;
			}
		}
	}
	return true;
}

//数量验证
function checkcount(){
	var count = parseFloat($("#count").val());
	var countactive = parseFloat($("#countactive").text());
	if(count > countactive){
		alert("您输入的提币数量大于您的可用余额了");
		return false;
	}
	var extractfloor = parseFloat($("#extractfloor").text());
	var extractupper = parseFloat($("#extractupper").text());
	if(count < extractfloor || count > extractupper){
		alert("您输入的提币数量低于下限或高于上限");
		return false;
	}
	var countactual = parseFloat($("#countactual").text());
	if(countactual <= 0){
		alert("您的实际提币数量不能小于或等于0");
		return false;
	}
	var extractdecimalsize = parseInt($("#extractdecimalsize").text());
	if(extractdecimalsize == 0){
		if(!confirm("此币仅支持整数到账，小数部分会在转账中丢失，为避免不必要的损失，请自行计算出最佳提币量，您确定转出" + $("#count").val() + "枚吗？")){
			return false;
		}
	}
	return true;
}

//表单验证
function checkform(){
    if(checknull("address", "钱包地址") && checkaddress() && checknull("count", "提币数量") && checkfloat("count", "提币数量") && checkcount() && checknull("tradepassword", "交易密码") && checkmobilevocde() && checknull("mobilevcode", "短信验证码")){
    	preventresubmit();
    	return true;
    }else{
    	return false;
    }
}