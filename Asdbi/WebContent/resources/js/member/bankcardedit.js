var linkageareaids = ["addprovince", "addcity"];
var linkageareadefvalues = ["--省份--", "--城市--"];

//初始化
$(document).ready(function(){
	init_linkagearea();
});

//初始化联动城市
function init_linkagearea(){
	setlinkageareas(0);
	if ($("#addprovince_inidata").val() != ""){
		$("#addprovince").val($("#addprovince_inidata").val());
		setlinkageareas(1);
		if ($("#addcity_inidata").val() != ""){
			$("#addcity").val($("#addcity_inidata").val());
		}
	}
}

//设置联动城市初始值
function setlinkageareainidata(obj){
	$("#" + obj.id + "_inidata").val($(obj).val());
}

//验证银行卡号正确性
function validatebankcard(){
	var number = $("#number").val();
	if(number.length >15){
		$.ajax({
	        url: "/member/bankcard/validatebankcard?number=" + number,
	        cache: false,
	        dataType: "text",
	        success: function(bankname){
	        	$("#bankname").val(bankname);
	        },
			error: function(){
	        	$("#bankname").val("");
	        	alert("银行卡接口错误，请联系管理员");
	        }
	    });
	}else{
		$("#bankname").val("");
	}
}

//验证银行名称
function checkbankname(){
	var bankname = $("#bankname").val();
	if($("#bankname").val() == ""){
		alert("您输入的银行卡号长度不足，请输入完整卡号");
		return false;
	}else if(bankname == "未找到银行"){
		alert("您输入的银行卡号错误，请仔细查检并修改");
		return false;
	}else{
		return true;
	}
}

//表单验证
function checkform(){
	if(checknull("number", "银行卡号") && checkbankname() && checkmobilevocde() && checknull("mobilevcode", "短信验证码")){
		preventresubmit();
		return true;
	}else{
		return false;
	}
}