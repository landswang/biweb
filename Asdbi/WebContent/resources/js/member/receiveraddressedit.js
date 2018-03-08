var addAreas = ["addprovince", "addcity", "addcounty"];  //三个select的name
var addDefaultValues = ["--省份--","--城市--","--区县--"];  //初始值

//初始化
$(document).ready(function(){
	for(i=0; i<addAreas.length; i++) document.getElementById(addAreas[i]).onchange = new Function("setadd("+(i+1)+")");
	setadd(0);
	if ($("#addprovince_inidata").val() != ""){
		$("#addprovince").val($("#addprovince_inidata").val());
		setadd(1);
		if ($("#addcity_inidata").val() != ""){
			$("#addcity").val($("#addcity_inidata").val());
			setadd(2);
			if ($("#addcounty_inidata").val() != "") $("#addcounty").val($("#addcounty_inidata").val());
		}
	}
});

//省失焦事件
function addprovinceonblur(){
	$("#addprovince_inidata").val($("#addprovince").val());
}

//市失焦事件
function addcityonblur(){
	$("#addcity_inidata").val($("#addcity").val());
}

//县失焦事件
function addcountyonblur(){
	$("#addcounty_inidata").val($("#addcounty").val());
}

//验证地区
function checkaddress(){
	if($("#addprovince").get(0).selectedIndex == 0){
		alert("请选择所在省份");
		$("#addprovince").focus();
		return false;
	}
	if($("#addcity").get(0).selectedIndex == 0){
		alert("请选择所在城市");
		$("#addcity").focus();
		return false;
	}
	if($("#addcounty").get(0).selectedIndex == 0){
		alert("请选择所在区县");
		$("#addcounty").focus();
		return false;
	}
	return true;
}

//表单验证
function checkform(){
    if(checkaddress() && checknull("adddetail", "详细地址") && checknull("recipients", "收件人姓名") && checknull("mobile", "联系电话") && checknull("sort", "排序") && checkint("sort", "排序")){
    	preventresubmit();
    	return true;
    }else{
    	return false;
    }
}