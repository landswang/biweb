//切换买（卖）单/我的买（卖）单选项
function swtichtbtrade(obj, type, index){
	$(obj).siblings().removeClass("cur");
	$(obj).addClass("cur");
	$("." + type).hide();
	var tabtrades = $("." + type);
	$(tabtrades[index]).show();
}

//买卖出数字校验
function validatenumber(obj,type){
	if($(obj).val() != "" && parseFloat($(obj).val()) > 0){
		var objname = $(obj).attr("name");
		var price = $("#" + type + "price").val();
		var count = $("#" + type + "count").val();
		if (objname == "price"){
			var priceinputdecimalsize = parseInt($("#priceinputdecimalsize").val());
			if(price.indexOf(".") > -1 && price.length - price.indexOf(".") - 1 > priceinputdecimalsize){
				price = price.substring(0, price.indexOf(".") + priceinputdecimalsize + 1);
				$("#" + type + "price").val(price);
				price = parseFloat(price);
			}
		}else{
			var countinputdecimalsize = parseInt($("#countinputdecimalsize").val());
			if(count.indexOf(".") > -1 && count.length - count.indexOf(".") - 1 > countinputdecimalsize){
				count = count.substring(0, count.indexOf(".") + countinputdecimalsize + 1);
				$("#" + type + "count").val(count);
				count = parseFloat(count);
			}
		}
		if($("#" + type + "price").val() != "" && $("#" + type + "count").val() != ""){
			var price = parseFloat(price);
			var count = parseFloat(count);
			var money = price.mul(count);
			var moneydecimalsize = parseInt($("#moneydecimalsize").val());
			var moneyStr = money.toString();
			if(moneyStr.indexOf(".") > -1 && moneyStr.length - moneyStr.indexOf(".") - 1 > moneydecimalsize){
				moneyStr = moneyStr.substring(0, moneyStr.indexOf(".") + moneydecimalsize + 1);
				money = parseFloat(moneyStr);
			}
			$("#" + type + "money").text(money);
			var rate = parseFloat($("#" + (type == "sell" ? "money" : "vcoin") + "rate").val());
			var feevalue = type == "sell" ? money.mul(rate) : count.mul(rate);
			$("#" + type + "fee").text(feevalue);
		}
	}else{
		$("#" + type + "money").text("");
		$("#" + type + "fee").text("");
	}
}

//计算交易量
function clactradecount(type){
	if ($("#" + type + "price").val() != "" && $("#" + type + "count").val() == ""){
		var price = parseFloat($("#" + type + "price").val());
		var balance = parseFloat($("#" + type + "balance").text());
		var count = type == "sell" ? balance : balance.div(price);
		$("#" + type + "count").val(count);
	}
}

//交易价验证
function checkprice(etype, ctype){
	var price = parseFloat($("#" + etype + "price").val());
	if(price < 0){
		alert(ctype + "价必须大于0");
		$("#" + etype + "price").focus();
		return false;
	}else{
		return true;
	}
}

//交易量验证
function checkcount(etype, ctype){
	var count = parseFloat($("#" + etype + "count").val());
	if(count < 0){
		alert(ctype + "量必须大于0");
		return false;
	}
	var countfloor = parseFloat($("#countfloor").val());
	if(count < countfloor){
		alert(ctype + "量不能低于交易下限——" + countfloor + "个");
		return false;
	}
	var price = parseFloat($("#" + etype + "price").val());
	var balance = parseFloat($("#" + etype + "balance").text());
	if(etype == "sell"){
		if(count > balance){
			alert(ctype + "量已大于您的余额，请降低" + ctype + "量");
			return false;
		}
	}else{
		if(count.mul(price) > balance){
			alert(ctype + "兑换额已大于您的余额，请降低" + ctype + "价或" + ctype + "量");
			return false;
		}
	}
	return true;
}

//交易错误验证
function checkwrong(etype, ctype){
	var price = parseFloat($("#" + etype + "price").val());
	if(etype == "sell"){
		var bestsellprice = parseFloat($("#bestsellprice").text());
		if(price < bestsellprice * 0.8){
			return confirm(ctype + "价已低于最佳卖价的80%，交易成功后将不可撤回，您确定您是在卖出而非买进？");
		}
	}else{
		var bestbuyprice = parseFloat($("#bestbuyprice").text());
		if(price > bestbuyprice * 1.2){
			return confirm(ctype + "价已高于最佳买价的120%，交易成功后将不可撤回，您确定您是在买进而非卖出？");
		}
	}
	return true;
}

//表单验证
function checkform(etype, ctype){
	if(checknull(etype + "count", ctype + "量") && checkfloat(etype + "count", ctype + "量") && checkcount(etype, ctype) && checknull(etype + "tradepassword", "交易密码") && checkwrong(etype, ctype)){
		preventresubmit();
		writeCookie("tradetopscroll", $(document).scrollTop());
		return true;
	}else{
		return false;
	}
}
