var myChart;

//初始化
$(document).ready(function(){
	init_topscroll();
	if($("#moneyrate").length > 0){
		init_trademoney("sell");
		init_trademoney("buy");
	}
	if(parseInt($("#trading").val()) == 1){
		setTimeout("refreshcurrentvcoin()", 5000);
		setTimeout("refreshcurrentorderform()", 5000);
		setTimeout("refreshlatelytraderecord()", 5000);
	}
});

//初始化顶部滚动
function init_topscroll(){
	var tradetopscroll = readCookie("tradetopscroll");
	if (tradetopscroll != null){
		$(document).scrollTop(parseInt(tradetopscroll));
		writeCookie("tradetopscroll", 0, -1);
	}
	myChart = echarts.init(document.getElementById('kline'));
	setTimeout("drawkline(" + $("#virtualcoinid").val() + ",'1h')");
}

//初始化买卖金额和手续费
function init_trademoney(type){
	if($("#" + type + "price").val() != "" && $("#" + type + "count").val() != ""){
		var price = parseFloat($("#" + type + "price").val());
		var count = parseFloat($("#" + type + "count").val());
		var money = price.mul(count);
		var rate = parseFloat($("#" + (type == "sell" ? "money" : "vcoin") + "rate").val());
		var feevalue = type == "sell" ? money.mul(rate) : count.mul(rate);
		$("#" + type + "money").text(money);
		$("#" + type + "fee").text(feevalue);
	}
}

//切换K线图
function switchkline(obj, type){
	$("#klineloading").show();
	myChart.clear();
	setTimeout("drawkline(" + $("#virtualcoinid").val() + ",'" + type + "')");
	$(".klimetimetype a").removeClass("cur");
	$(obj).addClass("cur");
}

var refreshvcoinindex = 0;

//刷新虚拟币数据
function refreshcurrentvcoin(){
	$.ajax({  
        url: "/interfaces/trade/currentvcoin",
        data: {virtualcoinid:$("#virtualcoinid").val(), type:2},
        cache: false,
        dataType: "json",
        success: function(virtualcoin){
			$("#openingprice").text(virtualcoin.openingprice);
			$("#currentprice").text(virtualcoin.price);
//			$("#topprice24hour").text(virtualcoin.topprice24hour);
//			$("#bottomprice24hour").text(virtualcoin.bottomprice24hour);
			$("#tradecount24hour").text(virtualcoin.tradecount24hour);
			$("#trademoney24hour").text(parseFloat(parseFloat(virtualcoin.trademoney24hour).toFixed(2)));
			if ($("#bestbuyprice").length > 0){
				$("#bestbuyprice").text(virtualcoin.bestbuyprice);
				$("#bestsellprice").text(virtualcoin.bestsellprice);
			}
			if(refreshvcoinindex++ < 60) setTimeout("refreshcurrentvcoin()", 5000);
        },
		error: function(){} 
    });
}

var refreshorderformindex = 0;

//刷新当前挂单数据
function refreshcurrentorderform(){
	$.ajax({  
	    url: "/interfaces/trade/currentorderform",
	    data: {virtualcoinid:$("#virtualcoinid").val()},
	    cache: false,
	    dataType: "json",
	    success: function(orderforms){
	  	  	var currentorderformtrs = $("#currentorderform tr");
	  	  	for(var i=1; i<currentorderformtrs.length; i++) $(currentorderformtrs[i]).remove();
	  	  	var dangwei = 0;
	  	  	var isbuy = true;
			for(var i=0; i<orderforms.length; i++){
				var orderform = orderforms[i];
				if (orderform.buy != isbuy){
					isbuy = orderform.buy;
					dangwei = 0;
				}
				dangwei++;
				var type = orderform.buy ? "buy" : "sell";
				var tr = "<tr";
				if(i % 2 == 1) tr += " class=\"splittr\"";
				if ($("#best" + type + "price").length > 0){
					tr += " onclick=\"settradeprice('" + (type == "buy" ? "sell" : "buy") + "price'," + orderform.price + ")\"";
				}
				tr += ">";
				tr += "<td class=\"words" + type + "\">" + (type == "buy" ? "买" : "卖") + dangwei + "</td>";
				tr += "<td>" + orderform.price + "</td>";
				tr += "<td>" + orderform.count + "</td>";
				tr += "<td>" + parseFloat(parseFloat(orderform.total).toFixed(2)) + "</td>";
				tr += "</tr>";
				$("#currentorderform").append(tr);
			}
			if(refreshorderformindex++ < 60) setTimeout("refreshcurrentorderform()", 5000);
	    },
	    error: function(){} 
	});
}

var refreshrecordindex = 0;

//刷新最近交易数据
function refreshlatelytraderecord(){
	$.ajax({  
	    url: "/interfaces/trade/latelytraderecord",
	    data: {virtualcoinid:$("#virtualcoinid").val()},
	    cache: false,
	    dataType: "json",
	    success: function(traderecords){
	  	  	var latelytraderecordstrs = $("#latelytraderecords tr");
	  	  	for(var i=1; i<latelytraderecordstrs.length; i++) $(latelytraderecordstrs[i]).remove();
			for(var i=0; i<traderecords.length; i++){
				var traderecord = traderecords[i];
				var tr = "<tr";
				if(i % 2 == 1) tr += " class=\"splittr\"";
				tr += ">";
				tr += "<td>" + traderecord.tradetime + "</td>";
				tr += "<td class=\"words" + (traderecord.type == "买入" ? "buy" : "sell") + "\">" + traderecord.type + "</td>";
				tr += "<td>" + traderecord.price + "</td>";
				tr += "<td>" + traderecord.count + "</td>";
				tr += "<td>" + parseFloat(parseFloat(traderecord.total).toFixed(2)) + "</td>";
				tr += "</tr>";
				$("#latelytraderecords").append(tr);
			}
			if(refreshrecordindex++ < 60) setTimeout("refreshlatelytraderecord()", 5000);
	    },
	    error: function(){} 
	});
}

//切换选项
function switchtab(obj, index){
	$(obj).siblings().removeClass("cur");
	$(obj).addClass("cur");
	$(".middle .tradecoin").hide();
	var tradecoins = $(".middle .tradecoin");
	$(tradecoins[index]).show();
}

//撤销挂单
function revocation(id){
	if(confirm("确定撤销该条挂单吗")){
		writeCookie("tradetopscroll", $(document).scrollTop());
		window.location.replace("/trade/revocation/" + $("#virtualcoinid").val() + "/" + id + ".html");
	}
}

//切换买（卖）单/我的买（卖）单选项
function swtichtbtrade(obj, type, index){
	$(obj).siblings().removeClass("cur");
	$(obj).addClass("cur");
	$("." + type).hide();
	var tabtrades = $("." + type);
	$(tabtrades[index]).show();
}

//设置交易价
function settradeprice(inputid, price){
	$("#" + inputid).val(price);
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
	if(checknull(etype + "price", ctype + "价") && checkfloat(etype + "price", ctype + "价") && checkprice(etype, ctype) && checknull(etype + "count", ctype + "量") && checkfloat(etype + "count", ctype + "量") && checkcount(etype, ctype) && checknull(etype + "tradepassword", "交易密码") && checkwrong(etype, ctype)){
		preventresubmit();
		writeCookie("tradetopscroll", $(document).scrollTop());
		return true;
	}else{
		return false;
	}
}
